package br.com.minhaudocao.adote.service;

import br.com.minhaudocao.adote.entity.Pessoa;
import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Transactional
    public Pessoa save(Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

    @Transactional
    public List<Pessoa> getAll(){
        return pessoaRepository.findAll();
    }

    @Transactional
    public Pessoa getById(Long id) throws ResourceNotFoundException {
        Optional<Pessoa> pessoa =  pessoaRepository.findById(id);
        if(pessoa.isPresent()){
            return pessoa.get();
        }else{
            throw new ResourceNotFoundException("Pessoa com ID " + id + " não encontrada");
        }
    }

    @Transactional
    public void deleteAll(){
        pessoaRepository.deleteAll();
    }

}
