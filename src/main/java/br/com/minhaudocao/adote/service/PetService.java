package br.com.minhaudocao.adote.service;

import br.com.minhaudocao.adote.entity.Endereco;
import br.com.minhaudocao.adote.entity.Instituicao;
import br.com.minhaudocao.adote.entity.Pessoa;
import br.com.minhaudocao.adote.entity.Pet;
import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.repository.EnderecoRepository;
import br.com.minhaudocao.adote.repository.InstituicaoRepository;
import br.com.minhaudocao.adote.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        return petRepository.save(pet);
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
