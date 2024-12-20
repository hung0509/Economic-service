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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.shop.economic_service.Repository.CategoryRepository;
import vn.shop.economic_service.Repository.DiscountRepository;
import vn.shop.economic_service.Repository.ImageRepository;
import vn.shop.economic_service.Repository.ProductRepository;
import vn.shop.economic_service.dto.request.ProductRequest;
import vn.shop.economic_service.dto.response.ProductResponse;
import vn.shop.economic_service.entity.Image;
import vn.shop.economic_service.exception.AppException;
import vn.shop.economic_service.exception.ErrorCode;
import vn.shop.economic_service.mapper.ProductMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ProductService{
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    DiscountRepository discountRepository;
    ProductMapper productMapper;
    AmazonS3 amazonS3;
    private final ImageRepository imageRepository;

    @NonFinal
    @Value("${aws.bucket}")
    String AWS_BUCKET;

    @NonFinal
    @Value("${aws.folder}")
    String AWS_FOLDER;

    public ProductResponse get(String id){
        var product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXIST));

        ProductResponse productResponse =  productMapper.toProductResponse(product);
        productResponse.setCategory(product.getCategory().getName());
        return productResponse;
    }

    @Transactional
    public ProductResponse create(ProductRequest request) throws IOException {
        var product = productMapper.toProduct(request);

        var category = categoryRepository.findById(request.getCategory())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXIST));
        var discount = discountRepository.findByCode(request.getDiscount());

        Set<Image> images = new HashSet<>();
        Image image = null;
        String url = uploadImage(request.getImages());

        if(Objects.nonNull(url)){
            image = Image.builder()
                    .url(url)
                    .build();

        log.info(url);
        }else {
            image = Image.builder()
                    .build();
        }
        image.setProduct(product);
        images.add(image);

        product.setCreated_at(LocalDate.now());
        product.setUpdated_at(LocalDate.now());
        product.setCategory(category);
        product.setDiscount(discount);
        product.setImages(images);

        product = productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    private String uploadImage(MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();
        String contentType = file.getContentType();
        InputStream inputStream = file.getInputStream();

        //Kiểm tra nếu không phải là ảnh thì không cho phép tiếp tục
        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
            throw new AppException(ErrorCode.NOT_VALID_FORMAT_IMAGE);
        }

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/jpeg"); // Hoặc loại nội dung phù hợp khác

        String keyName = AWS_FOLDER + "/" + file.getName();
        PutObjectRequest request = new PutObjectRequest(AWS_BUCKET, keyName, inputStream, metadata);
        amazonS3.putObject(request);//Đẩy hình ảnh lên trên bucket

        URL url = amazonS3.getUrl(AWS_BUCKET, keyName);
        //Ở đây đang để ở public access
        //nếu block access đi ta cần cấu hình IAM role...(Tìm hiểu thêm)
        return url.toString();
    }

}
