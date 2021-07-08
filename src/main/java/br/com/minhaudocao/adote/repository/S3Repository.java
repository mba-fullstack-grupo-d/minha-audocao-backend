package br.com.minhaudocao.adote.repository;

import org.springframework.web.multipart.MultipartFile;

public interface S3Repository {
   String uploadFileTos3bucket(MultipartFile multipartFile);
   String deleteFileFromS3Bucket(String fileUrl);
}
