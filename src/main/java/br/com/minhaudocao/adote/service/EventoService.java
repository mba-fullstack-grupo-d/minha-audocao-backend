package br.com.minhaudocao.adote.service;

import br.com.minhaudocao.adote.entity.*;
import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.repository.DataRepository;
import br.com.minhaudocao.adote.repository.EnderecoRepository;
import br.com.minhaudocao.adote.repository.EventoRepository;
import br.com.minhaudocao.adote.repository.InstituicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private DataRepository dataRepository;

    @Transactional
    public void save(Evento evento) {
        Instituicao instituicao = evento.getInstituicao();
        if (instituicao != null) {
            //Endereco da Instituicao, não é o endereco do evento
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
            evento.setInstituicao(savedInstituicao);
        }
        //Endereco do evento
        Endereco endereco = evento.getEndereco();
        if (endereco != null) {
            Endereco savedEndereco = null;
            if (endereco.getId() != null) {
                savedEndereco = enderecoRepository.findById(endereco.getId()).get();
            } else {
                savedEndereco = enderecoRepository.save(endereco);
            }
            evento.setEndereco(savedEndereco);
        }

        List<Data> datas = evento.getDatas();
        if(datas != null){
            List<Data> savedDatas = null;
            for (Data data: datas) {
                if(data.getDataId() != null){
                    savedDatas.add(dataRepository.findById(data.getDataId()).get());
                } else {
                    savedDatas.add(dataRepository.save(data));
                }
            }
            evento.setDatas(savedDatas);
        }
        eventoRepository.save(evento);
    }

    @Transactional
    public List<Evento> getAll() {
        return eventoRepository.findAll();
    }

    @Transactional
    public Evento getById(Long id) throws ResourceNotFoundException {
        Optional<Evento> evento =  eventoRepository.findById(id);
        if(evento.isPresent()){
            return evento.get();
        }else{
            throw new ResourceNotFoundException("Evento com ID " + id + " não encontrado");
        }
    }

}
