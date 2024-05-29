package org.devnot.awss3connect.modules.s3.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class S3Service {

     private AmazonS3 s3client;

     @Value("${bucketName}")
     private String bucketName;


     @Value("${bucketNamePublic}")
     private String bucketNamePublic;


     @Value("${jwt.secret}")
     private String secret;



     public S3Service(AmazonS3 s3client) {
         this.s3client = s3client;
     }


     public void uploadFile(String path, MultipartFile file) throws IOException {

         s3client.putObject(bucketName, path, file.getInputStream(), null );
     }

     public S3Object getFile(String path){
         return s3client.getObject(bucketName, path);
     }

     public String getFileExtension(String fileName) {
         int lastIndex = fileName.lastIndexOf('.');
         if (lastIndex == -1 || lastIndex == fileName.length() - 1) {
             return ""; // If no extension or dot is at the end, return an empty string
         } else {
             return fileName.substring(lastIndex + 1); // Return substring after the last dot
         }
     }

     public MediaType getMediaType(String fileExtension) {
         Map<String, MediaType> mediaTypes = new HashMap<>();
         mediaTypes.put("txt", MediaType.TEXT_PLAIN);
         mediaTypes.put("pdf", MediaType.APPLICATION_PDF);
         mediaTypes.put("png", MediaType.IMAGE_PNG);
         mediaTypes.put("jpg", MediaType.IMAGE_JPEG);
         mediaTypes.put("gif", MediaType.IMAGE_GIF);

         return mediaTypes.get(fileExtension.toLowerCase());
     }


     public void deleteFile(String path) {
         System.out.println();
         s3client.deleteObject(bucketName, path);
     }

     public Resource downloadFile(String path) {
         return new InputStreamResource(getFile(path).getObjectContent());
     }

    public List<String> listFiles() {
         List<String> keys = new ArrayList<>();
         ObjectListing objectListing = s3client.listObjects(bucketName);
         for (S3ObjectSummary os : objectListing.getObjectSummaries()) {
             keys.add(os.getKey());
         }
         return keys;
     }
   
     public boolean fileExists(String path) {
         return s3client.doesObjectExist(bucketName, path);
     }

     // Public S3 Methods
   
     public void uploadFilePublic(String path, MultipartFile file) throws IOException {

         s3client.putObject(bucketNamePublic, path, file.getInputStream(), null );
     }

     public S3Object getFilePublic(String path){
         return s3client.getObject(bucketNamePublic, path);
     }


     public void deleteFilePublic(String path) {
         System.out.println();
         s3client.deleteObject(bucketNamePublic, path);
     }

     public Resource downloadFilePublic(String path) {
         return new InputStreamResource(getFile(path).getObjectContent());
     }

     public List<String> listFilesPublic() {
         List<String> keys = new ArrayList<>();
         ObjectListing objectListing = s3client.listObjects(bucketNamePublic);
         Integer index = 1;
         for (S3ObjectSummary os : objectListing.getObjectSummaries()) {
             String s = index + " - Arquivo: " + os.getKey() + " - " + "Ultima Alteração: " + os.getLastModified() + " - " + "Tamanho: " + os.getSize()+ " bytes";
             keys.add(s);
             index++;
         }
         return keys;
     }
   
     public boolean fileExistsPublic(String path) {
         return s3client.doesObjectExist(bucketNamePublic, path);
     }
}

