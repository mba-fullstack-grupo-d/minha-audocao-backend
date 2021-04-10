package br.com.minhaudocao.adote.service;

import br.com.minhaudocao.adote.entity.Endereco;
import br.com.minhaudocao.adote.entity.Formulario;
import br.com.minhaudocao.adote.entity.Instituicao;
import br.com.minhaudocao.adote.repository.EnderecoRepository;
import br.com.minhaudocao.adote.repository.FormularioRepository;
import br.com.minhaudocao.adote.repository.InstituicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FormularioService {

    @Autowired
    private FormularioRepository formularioRepository;

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Transactional
    public void save(Formulario formulario) {
        Instituicao instituicao = formulario.getInstituicao();
        if (instituicao != null) {
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

            Instituicao savedInstituicao = null;
            if (instituicao.getId() != null) {
                savedInstituicao = instituicaoRepository.findById(instituicao.getId()).get();
            } else {
                savedInstituicao = instituicaoRepository.save(instituicao);
            }
            formulario.setInstituicao(savedInstituicao);

        }

        formularioRepository.save(formulario);
    }

    @Transactional
    public List<Formulario> getAll() {
        return formularioRepository.findAll();
    }


}
