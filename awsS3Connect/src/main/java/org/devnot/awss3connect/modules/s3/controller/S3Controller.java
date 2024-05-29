package org.devnot.awss3connect.modules.s3.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.devnot.awss3connect.modules.s3.services.S3Service;
import java.io.IOException;

@RestController
@RequestMapping()
public class S3Controller {

     private final S3Service s3Service;


     public S3Controller(S3Service s3Service) {
         this.s3Service = s3Service;
     }
     @PostMapping("/login")
     public String login() {
         return "Login successful";
     }

     @PostMapping(path = "/upload/", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
     public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam String path)throws IOException {
         s3Service.uploadFile(path, file);
         return "Arquivo Enviado Com Sucesso";
     }

     @GetMapping("/download/")
     public ResponseEntity<Resource> downloadFile(@RequestParam String path) throws IOException {
         return ResponseEntity.ok()
                 .contentType(MediaType.APPLICATION_OCTET_STREAM)
                 .body(s3Service.downloadFile(path));
     }

     @GetMapping("/view/")
     public ResponseEntity<InputStreamResource> viewFile(@RequestParam String path) {
         var s3Object = s3Service.getFile(path);
         var content = s3Object.getObjectContent();
         var fileExtension = s3Service.getFileExtension(path);
         return ResponseEntity.ok()
                 .contentType(s3Service.getMediaType(fileExtension)) // This content type can change by your file :)
                 .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\""+path+"\"")
                 .body(new InputStreamResource(content));
     }

     @DeleteMapping("/delete/")
     public String deleteFile(@RequestParam String path) {
         s3Service.deleteFile(path);
         return "Arquivo Deletado Com Sucesso";
     }

     @PostMapping(path = "/uploadPublic/", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
     public String uploadFilePublic(@RequestParam("file") MultipartFile file, @RequestParam String path)throws IOException {
         s3Service.uploadFilePublic(path, file);
         return "Arquivo Enviado Com Sucesso";
     }

     @GetMapping("/downloadPublic/")
     public ResponseEntity<Resource> downloadFilePublic(@RequestParam String path) throws IOException {
         return ResponseEntity.ok()
                 .contentType(MediaType.APPLICATION_OCTET_STREAM)
                 .body(s3Service.downloadFilePublic(path));
     }

     @GetMapping("/viewPublic/")
     public ResponseEntity<InputStreamResource> viewFilePublic(@RequestParam String path) {
         var s3Object = s3Service.getFilePublic(path);
         var content = s3Object.getObjectContent();
         var fileExtension = s3Service.getFileExtension(path);
         return ResponseEntity.ok()
                 .contentType(s3Service.getMediaType(fileExtension)) // This content type can change by your file :)
                 .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\""+path+"\"")
                 .body(new InputStreamResource(content));
     }

     @DeleteMapping("/deletePublic/")
     public String deleteFilePublic(@RequestParam String path) {
         s3Service.deleteFilePublic(path);
         return "Arquivo Deletado Com Sucesso";
     }

     @GetMapping("/list/")
     public String listFiles() {
         return s3Service.listFiles().toString();
     }

     @GetMapping("/listPublic/")
     public String listFilesPublic() {
         return s3Service.listFilesPublic().toString();
     }

     @GetMapping("/exists/")
     public String fileExists(@RequestParam String path) {
         return s3Service.fileExists(path) ? "Arquivo Existe" : "Arquivo Não Existe";
     }

     @GetMapping("/existsPublic/")
     public String fileExistsPublic(@RequestParam String path) {
         return s3Service.fileExistsPublic(path) ? "Arquivo Existe" : "Arquivo Não Existe";
     }
}

