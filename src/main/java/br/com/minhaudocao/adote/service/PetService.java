package br.com.minhaudocao.adote.service;

import br.com.minhaudocao.adote.entity.*;
import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.model.PetSearchRequest;
import br.com.minhaudocao.adote.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        if(pet.getUriFotos() != null){
            for (String uriFoto: pet.getUriFotos()) {
                Foto foto = new Foto();
                foto.setPet(savedPet);
                foto.setUriFoto(uriFoto);
                fotoRepository.save(foto);
            }
        }

        return savedPet;
    }

    @Transactional
    public List<Pet> getAll(){
        List<Pet> pets = petRepository.findAll();
        for (Pet pet:pets) {
            ArrayList<Foto> fotos = fotoRepository.findByPet(pet);
            if(!fotos.isEmpty()){
                pet.setUriFotos(fotos.stream().map(Foto::getUriFoto).collect(Collectors.toList()));
            }
        }
        return pets;
    }

    @Transactional
    public Pet getById(Long id) throws ResourceNotFoundException {
        Optional<Pet> pet =  petRepository.findById(id);
        if(pet.isPresent()){
            Pet foundPet = pet.get();
            ArrayList<Foto> fotos = fotoRepository.findByPet(foundPet);
            if(!fotos.isEmpty()){
                foundPet.setUriFotos(fotos.stream().map(Foto::getUriFoto).collect(Collectors.toList()));
            }
            return foundPet;
        }else{
            throw new ResourceNotFoundException("Pet com ID " + id + " não encontrado");
        }
    }

    @Transactional
    public void deleteAll(){
        petRepository.deleteAll();
    }

    @Transactional
    public List<Pet> getByInstituicao(Long idInstituicao) {
        Instituicao instituicao = new Instituicao();
        instituicao.setId(idInstituicao);
        List<Pet> pets = petRepository.findByInstituicao(instituicao);
        for (Pet pet:pets) {
            ArrayList<Foto> fotos = fotoRepository.findByPet(pet);
            if(!fotos.isEmpty()){
                pet.setUriFotos(fotos.stream().map(Foto::getUriFoto).collect(Collectors.toList()));
            }
        }
        return pets;
    }

    public List<Pet> search(PetSearchRequest petSearch){
        List<Endereco> enderecos = null;
        List<Instituicao> instituicaos = null;
        List<Pet> pets = null;

        if(petSearch.getBairro() != null && petSearch.getCidade() != null) {
            enderecos = enderecoRepository.findByCidadeAndBairro(petSearch.getCidade(), petSearch.getBairro());
        }else if(petSearch.getBairro() != null){
            enderecos = enderecoRepository.findByBairro(petSearch.getBairro());
        }else if(petSearch.getCidade() != null){
            enderecos = enderecoRepository.findByCidade(petSearch.getCidade());
        }

        if(petSearch.getNomeInstituicao() != null && enderecos != null){
            for (Endereco endereco: enderecos) {
                if(instituicaos == null){
                    instituicaos = instituicaoRepository.findByNomeAndEndereco(petSearch.getNomeInstituicao(), endereco);
                }else {
                    instituicaos.addAll(instituicaoRepository.findByNomeAndEndereco(petSearch.getNomeInstituicao(), endereco));
                }
            }
        }else if(enderecos != null){
            for (Endereco endereco: enderecos) {
                if(instituicaos == null){
                    instituicaos = instituicaoRepository.findByEndereco(endereco);
                }else{
                    instituicaos.addAll(instituicaoRepository.findByEndereco(endereco));
                }

            }
        }else if (petSearch.getNomeInstituicao() != null){
            instituicaos = instituicaoRepository.findByNome(petSearch.getNomeInstituicao());
        }

        if(petSearch.getEspecie() != null && petSearch.getGenero() != null && petSearch.getIdade() != null && instituicaos != null){
            for (Instituicao instituicao: instituicaos) {
                if(pets == null){
                    pets = petRepository.findByInstituicaoAndIdadeAndGeneroAndEspecie(instituicao, petSearch.getIdade(), petSearch.getGenero(), petSearch.getEspecie());
                }else {
                    pets.addAll(petRepository.findByInstituicaoAndIdadeAndGeneroAndEspecie(instituicao, petSearch.getIdade(), petSearch.getGenero(), petSearch.getEspecie()));
                }
            }
        }else if(instituicaos != null && petSearch.getEspecie() != null && petSearch.getGenero() != null){
            for (Instituicao instituicao: instituicaos) {
                if(pets == null){
                    pets = petRepository.findByInstituicaoAndGeneroAndEspecie(instituicao, petSearch.getGenero(), petSearch.getEspecie());
                }else{
                    pets.addAll(petRepository.findByInstituicaoAndGeneroAndEspecie(instituicao, petSearch.getGenero(), petSearch.getEspecie()));
                }
            }
        }else if(instituicaos != null && petSearch.getEspecie() != null && petSearch.getIdade() != null){
            for (Instituicao instituicao: instituicaos) {
                if(pets == null){
                    pets = petRepository.findByInstituicaoAndIdadeAndEspecie(instituicao, petSearch.getIdade(), petSearch.getEspecie());
                }else{
                    pets.addAll(petRepository.findByInstituicaoAndIdadeAndEspecie(instituicao, petSearch.getIdade(), petSearch.getEspecie()));
                }
            }
        }else if(instituicaos != null && petSearch.getGenero() != null && petSearch.getIdade() != null){
            for (Instituicao instituicao: instituicaos) {
                if (pets == null) {
                    pets = petRepository.findByInstituicaoAndIdadeAndGenero(instituicao, petSearch.getIdade(), petSearch.getGenero());
                }else {
                    pets.addAll(petRepository.findByInstituicaoAndIdadeAndGenero(instituicao, petSearch.getIdade(), petSearch.getGenero()));
                }
            }
        }else if(petSearch.getGenero() != null && petSearch.getEspecie() != null && petSearch.getIdade() != null){
            pets = petRepository.findByIdadeAndGeneroAndEspecie(petSearch.getIdade(), petSearch.getGenero(), petSearch.getEspecie());
        }else if(instituicaos != null && petSearch.getIdade() != null){
            for(Instituicao instituicao:instituicaos){
                if (pets == null) {
                    pets = petRepository.findByInstituicaoAndIdade(instituicao, petSearch.getIdade());
                }else{
                    pets.addAll(petRepository.findByInstituicaoAndIdade(instituicao, petSearch.getIdade()));
                }
            }
        }else if(instituicaos != null && petSearch.getEspecie() != null){
            for(Instituicao instituicao:instituicaos){
                if (pets == null) {
                    pets = petRepository.findByInstituicaoAndEspecie(instituicao, petSearch.getEspecie());
                }else{
                    pets.addAll(petRepository.findByInstituicaoAndEspecie(instituicao, petSearch.getEspecie()));
                }
            }
        }else if(instituicaos != null && petSearch.getGenero() != null){
            for (Instituicao instituicao:instituicaos) {
                if (pets == null) {
                    pets = petRepository.findByInstituicaoAndGenero(instituicao, petSearch.getGenero());
                }else{
                    pets.addAll(petRepository.findByInstituicaoAndGenero(instituicao, petSearch.getGenero()));
                }
            }
        }else if(petSearch.getIdade() != null && petSearch.getGenero() != null){
            pets = petRepository.findByIdadeAndGenero(petSearch.getIdade(), petSearch.getGenero());
        }else if(petSearch.getIdade() != null && petSearch.getEspecie() != null){
            pets = petRepository.findByIdadeAndEspecie(petSearch.getIdade(), petSearch.getEspecie());
        }else if(petSearch.getEspecie() != null && petSearch.getGenero() !=null){
            pets = petRepository.findByGeneroAndEspecie(petSearch.getGenero(), petSearch.getEspecie());
        }else if(instituicaos != null){
            for (Instituicao instituicao: instituicaos) {
                if (pets == null) {
                    pets = petRepository.findByInstituicao(instituicao);
                }else{
                    pets.addAll(petRepository.findByInstituicao(instituicao));
                }
            }
        }else if(petSearch.getIdade() != null){
            pets = petRepository.findByIdade(petSearch.getIdade());
        }else if(petSearch.getEspecie() != null){
            pets = petRepository.findByEspecie(petSearch.getEspecie());
        }else if(petSearch.getGenero() != null){
            pets = petRepository.findByGenero(petSearch.getGenero());
        }

        for (Pet pet:pets) {
            ArrayList<Foto> fotos = fotoRepository.findByPet(pet);
            if(!fotos.isEmpty()){
                pet.setUriFotos(fotos.stream().map(Foto::getUriFoto).collect(Collectors.toList()));
            }
        }

        return pets;
    }

    public void delete(Long id) throws ResourceNotFoundException {
        Optional<Pet> optionalToDelete = petRepository.findById(id);
        if(optionalToDelete.isPresent()){
            List<Foto> fotos = fotoRepository.findByPet(optionalToDelete.get());
            if(!fotos.isEmpty()){
                for (Foto foto: fotos) {
                    fotoRepository.delete(foto);
                }
            }
            petRepository.deleteById(id);
        }else{
            throw new ResourceNotFoundException("Pet não encontrado");
        }
    }

    @Transactional
    public void update(Pet pet) throws ResourceNotFoundException {
        Optional<Pet> petToUpdate = petRepository.findById(pet.getId());
        if(petToUpdate.isPresent()){
            petToUpdate.get().setNome(pet.getNome());
            petToUpdate.get().setEspecie(pet.getEspecie());
            petToUpdate.get().setDescricao(pet.getDescricao());
            petToUpdate.get().setAdotado(pet.getAdotado());
            petToUpdate.get().setGenero(pet.getGenero());
            petToUpdate.get().setIdade(pet.getIdade());
            petToUpdate.get().setCastrado(pet.getCastrado());
            petToUpdate.get().setVacinado(pet.getVacinado());
            petRepository.save(petToUpdate.get());
        }else{
            throw new ResourceNotFoundException("Pet não encontrado");
        }
    }
}
