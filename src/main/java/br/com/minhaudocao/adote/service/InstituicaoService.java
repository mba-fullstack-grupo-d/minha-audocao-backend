package br.com.minhaudocao.adote.service;

import br.com.minhaudocao.adote.entity.Endereco;
import br.com.minhaudocao.adote.entity.Instituicao;
import br.com.minhaudocao.adote.entity.Pet;
import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.repository.EnderecoRepository;
import br.com.minhaudocao.adote.repository.InstituicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InstituicaoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    @Transactional
    public void save(Instituicao instituicao) {
        Endereco endereco = instituicao.getEndereco();
        if (endereco != null) {
            Endereco savedEndereco = null;
            if (endereco.getId() != null) {
                savedEndereco = enderecoRepository.findById(endereco.getId()).get();
            } else {
                savedEndereco = enderecoRepository.save(endereco);
            }
            instituicao.setEndereco(savedEndereco);
        }
        instituicaoRepository.save(instituicao);
    }

    @Transactional
    public List<Instituicao> getAll(){
        return instituicaoRepository.findAll();
    }

    @Transactional
    public Instituicao getById(Long id) throws ResourceNotFoundException {
        Optional<Instituicao> instituicao =  instituicaoRepository.findById(id);
        if(instituicao.isPresent()){
            return instituicao.get();
        }else{
            throw new ResourceNotFoundException("Instituição com ID " + id + " não encontrada");
        }
    }

}

