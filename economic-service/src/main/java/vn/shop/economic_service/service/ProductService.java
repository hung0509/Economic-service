package vn.shop.economic_service.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.shop.economic_service.Repository.*;
import vn.shop.economic_service.dto.request.ProductRequest;
import vn.shop.economic_service.dto.response.ProductResponse;
import vn.shop.economic_service.entity.ProductSize;
import vn.shop.economic_service.entity.Size;
import vn.shop.economic_service.entity.key.ProductSizeKey;
import vn.shop.economic_service.exception.AppException;
import vn.shop.economic_service.exception.ErrorCode;
import vn.shop.economic_service.mapper.ProductMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ProductService{
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    DiscountRepository discountRepository;
    ProductSizeRepository productSizeRepository;
    SizeRepository sizeRepository;
    ProductMapper productMapper;
    AmazonS3 amazonS3;

    @NonFinal
    @Value("${aws.bucket}")
    String AWS_BUCKET;

    @NonFinal
    @Value("${aws.folder}")
    String AWS_FOLDER;

    public List<ProductResponse> getAll(int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        var product = productRepository.findAll(pageable);
        return product.stream().map(productMapper::toProductResponse).toList();
    }

    public List<ProductResponse> getAllByDiscount(int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        var product = productRepository.fidAllByDiscount(pageable);
        return product.stream().map(productMapper::toProductResponse).toList();
    }

    public ProductResponse get(String id){
        var product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXIST));

        ProductResponse productResponse =  productMapper.toProductResponse(product);
        productResponse.setCategory(product.getCategory().getName());
        return productResponse;
    }

    public List<ProductResponse> getByCategory(String category, int page, int size){

        var ca = categoryRepository.findById(category.toUpperCase(Locale.ROOT))
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXIST));
        Pageable pageable = PageRequest.of(page, size);
        var product = productRepository.findAllByCategory(ca, pageable);
        return product.stream().map(productMapper::toProductResponse).toList();
    }

    @Transactional
    public void remove(String id){
        var product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXIST));
        if(Objects.nonNull(product))
            productRepository.delete(product);
    }

    @Transactional
    public ProductResponse update(ProductRequest request, String id) throws IOException {
        // Tìm sản phẩm theo ID
        var product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXIST));

        // Cập nhật thông tin sản phẩm từ request
        product = productMapper.toProduct(request);
        product.setId(id);

        // Tìm category theo ID
        var category = categoryRepository.findById(request.getCategory())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXIST));

        // Tìm discount theo mã code
        var discount = discountRepository.findByCode(request.getDiscount());

         //Upload hình ảnh và lấy URL
        if(request.getImages() != null && !request.getImages().isEmpty()){
            String url = uploadImage(request.getImages());
            product.setImages(url);
        }

        product.setUpdated_at(LocalDate.now());
        product.setCategory(category);
        product.setDiscount(discount);
        // Lưu sản phẩm và trả về phản hồi
        product = productRepository.save(product);

        //Xử lý tới size
        Set<ProductSize> productSizes = product.getSizes();

        if(request.getSizes()!=null){
            for (ProductRequest.SizeRequest sizeRequest : request.getSizes()) {
                Size size = sizeRepository.findById(sizeRequest.getName()).orElseThrow(
                        () -> new AppException(ErrorCode.SIZE_NOT_EXIST));

                boolean existSize = false;
                for(ProductSize productSize: productSizes){
                    if(productSize.getSize().getName().equals(sizeRequest.getName())){
                        //Nếu đã có trong ProductSize thì cập nhật ố lượng
                        productSize.setStockQuantity(sizeRequest.getStockQuantity());
                        existSize = true;
                        break;
                    }
                }
                if(!existSize){ //Nếu chưa có thêm mới size này vào với số lợng ...
                    ProductSize productSize = new ProductSize();
                    ProductSizeKey productSizeKey = new ProductSizeKey(product.getId(), size.getName());
                    productSize.setId(productSizeKey);
                    productSize.setProduct(product);
                    productSize.setSize(size);
                    productSize.setStockQuantity(sizeRequest.getStockQuantity());
                    productSizes.add(productSize);
                }
            }
        }
        // Lưu các ProductSize
        productSizeRepository.saveAll(productSizes);
        product.setSizes(productSizes);

        return productMapper.toProductResponse(product);
    }

    @Transactional
    public ProductResponse create(ProductRequest request) throws IOException {
        var product = productMapper.toProduct(request);

        var category = categoryRepository.findById(request.getCategory())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXIST));

        var discount = discountRepository.findByCode(request.getDiscount());

        if(request.getImages() != null && !request.getImages().isEmpty()){
            String url = uploadImage(request.getImages());
            product.setImages(url);
        }

        product.setCreated_at(LocalDate.now());
        product.setUpdated_at(LocalDate.now());
        product.setCategory(category);
        product.setDiscount(discount);

        product = productRepository.save(product); // Xử lý lưu 1 sản phẩm

        //Xử lý tới size
        Set<ProductSize> productSizes = new HashSet<>();
        log.info(request.getSizes().get(0).getName());
        if(request.getSizes()!=null){
            for (ProductRequest.SizeRequest sizeRequest : request.getSizes()) {
                var size = sizeRepository.findById(sizeRequest.getName())
                        .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_EXIST));

                if(size != null) {
                    ProductSize productSize = new ProductSize();
                    ProductSizeKey productSizeKey = new ProductSizeKey(product.getId(), size.getName());
                    productSize.setId(productSizeKey);
                    productSize.setProduct(product);
                    productSize.setSize(size);
                    productSize.setStockQuantity(sizeRequest.getStockQuantity());
                    productSizes.add(productSize);
                }
            }
        }
        // Lưu các ProductSize
        productSizeRepository.saveAll(productSizes);
        product.setSizes(productSizes);
        return productMapper.toProductResponse(product);
    }

    private String uploadImage(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        InputStream inputStream = file.getInputStream();

        //Kiểm tra nếu không phải là ảnh thì không cho phép tiếp tục
            if (!contentType.equals("image/jpeg")
                && !contentType.equals("image/png")
                && !contentType.equals("image/webp")
                && !contentType.equals("image/gif")
                && !contentType.equals("image/bmp")) {
            throw new AppException(ErrorCode.NOT_VALID_FORMAT_IMAGE);
        }

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType); // Hoặc loại nội dung phù hợp khác

        String keyName = AWS_FOLDER + "/" + file.getOriginalFilename();
        PutObjectRequest request = new PutObjectRequest(AWS_BUCKET, keyName, inputStream, metadata);
        amazonS3.putObject(request);//Đẩy hình ảnh lên trên bucket

        URL url = amazonS3.getUrl(AWS_BUCKET, keyName);
        //Ở đây đang để ở public access
        //nếu block access đi ta cần cấu hình IAM role...(Tìm hiểu thêm)
        return url.toString();
    }

}
