package br.com.minhaudocao.adote.service;

import br.com.minhaudocao.adote.entity.*;
import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private S3RepositoryImpl s3Repository;

    @Autowired
    private FotoRepository fotoRepository;

    @Transactional
    public Pet save(Pet pet) {
        Instituicao instituicao = pet.getInstituicao();
        if(instituicao != null){
            Endereco endereco = instituicao.getEndereco();
            if(endereco != null){
                Endereco savedEndereco = null;
                if(endereco.getId() != null){
                    savedEndereco = enderecoRepository.findById(endereco.getId()).get();
                }else{
                    savedEndereco = enderecoRepository.save(endereco);
                }
                instituicao.setEndereco(savedEndereco);
            }

            Instituicao savedInstituicao = null;
            if(instituicao.getId() != null){
                savedInstituicao = instituicaoRepository.findById(instituicao.getId()).get();
            }else{
                savedInstituicao = instituicaoRepository.save(instituicao);
            }
            pet.setInstituicao(savedInstituicao);

        }

        Pet savedPet =  petRepository.save(pet);

        if(pet.getFotos() != null){
            for (MultipartFile foto: pet.getFotos()) {
                Foto uriFoto = new Foto();
                uriFoto.setUriFoto(s3Repository.uploadFileTos3bucket(foto));
                uriFoto.setPet(savedPet);
                fotoRepository.save(uriFoto);
            }
        }

        return savedPet;
    }

    @Transactional
    public List<Pet> getAll(){
        return petRepository.findAll();
    }

    @Transactional
    public Pet getById(Long id) throws ResourceNotFoundException {
        Optional<Pet> pet =  petRepository.findById(id);
        if(pet.isPresent()){
            return pet.get();
        }else{
            throw new ResourceNotFoundException("Pet com ID " + id + " não encontrado");
        }
    }

    @Transactional
    public void deleteAll(){
        petRepository.deleteAll();
    }

}
