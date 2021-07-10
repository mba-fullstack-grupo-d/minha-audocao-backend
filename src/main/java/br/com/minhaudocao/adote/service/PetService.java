package br.com.minhaudocao.adote.service;

import br.com.minhaudocao.adote.entity.*;
import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.model.PetSearchRequest;
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
                savedPet.setUriFoto(uriFoto.getUriFoto());
            }
        }

        return savedPet;
    }

    @Transactional
    public List<Pet> getAll(){
        List<Pet> pets = petRepository.findAll();
        for (Pet pet:pets) {
            pet.setUriFotos(fotoRepository.findByPet(pet));
        }
        return pets;
    }

    @Transactional
    public Pet getById(Long id) throws ResourceNotFoundException {
        Optional<Pet> pet =  petRepository.findById(id);
        if(pet.isPresent()){
            Pet foundPet = pet.get();
            foundPet.setUriFotos(fotoRepository.findByPet(foundPet));
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
            pet.setUriFotos(fotoRepository.findByPet(pet));
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

                }else {

                }
                pets.addAll(petRepository.findByInstituicaoAndIdadeAndGeneroAndEspecie(instituicao, petSearch.getIdade(), petSearch.getGenero(), petSearch.getEspecie()));
            }
        }else if(instituicaos != null && petSearch.getEspecie() != null && petSearch.getGenero() != null){
            for (Instituicao instituicao: instituicaos) {
                pets.addAll(petRepository.findByInstituicaoAndGeneroAndEspecie(instituicao, petSearch.getGenero(), petSearch.getEspecie()));
            }
        }else if(instituicaos != null && petSearch.getEspecie() != null && petSearch.getIdade() != null){
            for (Instituicao instituicao: instituicaos) {
                pets.addAll(petRepository.findByInstituicaoAndIdadeAndEspecie(instituicao, petSearch.getIdade(), petSearch.getEspecie()));
            }
        }else if(instituicaos != null && petSearch.getGenero() != null && petSearch.getIdade() != null){
            for (Instituicao instituicao: instituicaos) {
                pets.addAll(petRepository.findByInstituicaoAndIdadeAndGenero(instituicao, petSearch.getIdade(), petSearch.getGenero()));
            }
        }else if(petSearch.getGenero() != null && petSearch.getEspecie() != null && petSearch.getIdade() != null){
            pets = petRepository.findByIdadeAndGeneroAndEspecie(petSearch.getIdade(), petSearch.getGenero(), petSearch.getEspecie());
        }else if(instituicaos != null && petSearch.getIdade() != null){

        }else if(instituicaos != null && petSearch.getEspecie() != null){

        }else if(instituicaos != null && petSearch.getGenero() != null){

        }else if(petSearch.getIdade() != null && petSearch.getGenero() != null){

        }else if(petSearch.getIdade() != null && petSearch.getEspecie() != null){

        }else if(petSearch.getEspecie() != null && petSearch.getGenero() !=null){

        }else if(instituicaos != null){

        }else if(petSearch.getIdade() != null){

        }else if(petSearch.getEspecie() != null){

        }else if(petSearch.getGenero() != null){

        }


        return pets;
    }
}
