package br.com.minhaudocao.adote.service;

import br.com.minhaudocao.adote.entity.Pessoa;
import br.com.minhaudocao.adote.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Transactional
    public void save(Pessoa pessoa) {
        pessoaRepository.save(pessoa);
    }

    @Transactional
    public List<Pessoa> getAll(){
        return pessoaRepository.findAll();
    }

}
