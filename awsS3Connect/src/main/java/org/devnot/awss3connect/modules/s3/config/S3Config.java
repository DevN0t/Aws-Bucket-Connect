package org.devnot.awss3connect.modules.s3.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

 @Configuration
public class S3Config {

     @Value("${bucketSecret}")
     private String bucketSecret;

     @Value("${bucketKey}")
     private String bucketKey;

     @Value("${bucketRegion}")
     private String bucketRegion;

     @Bean
     public AmazonS3 s3client(){


         BasicAWSCredentials awsCreds = new BasicAWSCredentials(bucketKey, bucketSecret);
         var awsS3Config = AmazonS3ClientBuilder.standard()
                 .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                 .withRegion(bucketRegion)
                 .build();
         return awsS3Config;
     }
    

}
