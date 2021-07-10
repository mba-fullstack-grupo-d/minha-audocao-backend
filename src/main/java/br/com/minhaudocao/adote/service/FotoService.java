package br.com.minhaudocao.adote.service;

import br.com.minhaudocao.adote.entity.Foto;
import br.com.minhaudocao.adote.exception.PhotoNotUploadedException;
import br.com.minhaudocao.adote.repository.FotoRepository;
import br.com.minhaudocao.adote.repository.S3RepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FotoService {

    @Autowired
    private FotoRepository fotoRepository;

    @Autowired
    private S3RepositoryImpl s3Repository;

    @Transactional
    public Foto save(Foto foto) {
        return fotoRepository.save(foto);
    }

    @Transactional
    public void delete(Foto foto){
        fotoRepository.deleteById(foto.getId());
    }
}
