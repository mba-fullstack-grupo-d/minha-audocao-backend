package br.com.minhaudocao.adote.service;

import br.com.minhaudocao.adote.entity.Endereco;
import br.com.minhaudocao.adote.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Transactional
    public void save(Endereco endereco) {
        enderecoRepository.save(endereco);
    }
}
