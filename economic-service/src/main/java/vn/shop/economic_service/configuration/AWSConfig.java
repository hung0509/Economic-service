package vn.shop.economic_service.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AWSConfig {
    @Value("${aws.access_key}")
    private String accessKey; //khóa truy cập

    @Value("${aws.secret-key}")
    private String secretKey;//Khóa bảo mật

    @Value("${aws.region}")
    private String region; //Khu vực

    //Xác thực thông tin với AWS
    @Bean
    public AmazonS3 getS3Client(){
        //dùng để kết nối với AWS với các thông tin xác thực
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withRegion(region)
                .build();
    }
}
