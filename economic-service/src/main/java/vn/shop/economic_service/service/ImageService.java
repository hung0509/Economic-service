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
import vn.shop.economic_service.Repository.ImageRepository;
import vn.shop.economic_service.dto.response.ImageResponse;
import vn.shop.economic_service.entity.Image;
import vn.shop.economic_service.exception.AppException;
import vn.shop.economic_service.exception.ErrorCode;
import vn.shop.economic_service.mapper.ImageMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    ImageRepository imageRepository;
    ImageMapper imageMapper;
    AmazonS3 amazonS3;

    @NonFinal
    @Value("${aws.bucket}")
    String AWS_BUCKET;

    @NonFinal
    @Value("${aws.folder}")
    String AWS_FOLDER;


    @Transactional
    public List<ImageResponse> getAll(){
       var images = imageRepository.findAll();
       return images.stream().map(imageMapper::toImageResponse).toList();
    }

    @Transactional
    public ImageResponse update(String id, MultipartFile file){
        var image = imageRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.IMAGE_NOT_EXIST));

        image.setUrl(uploadImage(file));
        image = imageRepository.save(image);

        return imageMapper.toImageResponse(image);
    }

    //Khi upload ra sẽ lưu đường dẫn vaào db để truy cập.
    private String uploadImage(MultipartFile file){
        File tempFile = convertFile(file);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/jpeg"); // Hoặc loại nội dung phù hợp khác

        String keyName = AWS_FOLDER + "/" + file.getName();
        PutObjectRequest request = new PutObjectRequest(AWS_BUCKET, keyName, tempFile);
        request.setMetadata(metadata);
        amazonS3.putObject(request);//Đẩy hình ảnh lên trên bucket

        URL url = amazonS3.getUrl(AWS_BUCKET, keyName);
        //Ở đây đang để ở public access
        //nếu block access đi ta cần cấu hình IAM role...(Tìm hiểu thêm)
        return url.toString();
    }

    private File convertFile(MultipartFile file){
        File convFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return convFile;
    }

}
