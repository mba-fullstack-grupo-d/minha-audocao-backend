package br.com.minhaudocao.adote.service;

import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.repository.S3RepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Service {

    @Autowired
    private S3RepositoryImpl s3Repository;

    public String uploadFile(MultipartFile multipartFile) throws ResourceNotFoundException {
        try {
            return s3Repository.uploadFileTos3bucket(multipartFile);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Erro ao salvar foto");
        }
    }

    public String deleteFile(String fileUrl) {
        return s3Repository.deleteFileFromS3Bucket(fileUrl);
    }
}
