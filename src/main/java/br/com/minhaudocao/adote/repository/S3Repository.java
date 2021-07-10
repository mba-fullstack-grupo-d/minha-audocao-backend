package br.com.minhaudocao.adote.repository;

import br.com.minhaudocao.adote.exception.PhotoNotUploadedException;
import org.springframework.web.multipart.MultipartFile;

public interface S3Repository {
   String uploadFileTos3bucket(MultipartFile multipartFile) throws PhotoNotUploadedException;
   String deleteFileFromS3Bucket(String fileUrl);
}
