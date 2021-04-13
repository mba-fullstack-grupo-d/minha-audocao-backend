package br.com.minhaudocao.adote.service;

import br.com.minhaudocao.adote.entity.Endereco;
import br.com.minhaudocao.adote.entity.Pessoa;
import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Transactional
    public void save(Endereco endereco) {
        enderecoRepository.save(endereco);
    }

    @Transactional
    public Endereco getById(Long id) throws ResourceNotFoundException {
        Optional<Endereco> endereco =  enderecoRepository.findById(id);
        if(endereco.isPresent()){
            return endereco.get();
        }else{
            throw new ResourceNotFoundException("Endereço com ID " + id + " não encontrado");
        }
    }
}
