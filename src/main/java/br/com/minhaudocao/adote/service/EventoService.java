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

import java.util.LinkedList;
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
    public Evento save(Evento evento) {
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
        Evento savedEvento = eventoRepository.saveAndFlush(evento);

        if(datas != null){
            List<Data> savedDatas = new LinkedList<>();
            for (Data data: datas) {
                if(data.getId() != null){
                    savedDatas.add(dataRepository.findById(data.getId()).get());
                } else {
                    data.setIdEvento(savedEvento.getId());
                    savedDatas.add(dataRepository.save(data));
                }
            }
            savedEvento.setDatas(savedDatas);
        }
        return savedEvento;
    }

    @Transactional
    public List<Evento> getAll() {
        List<Evento> eventos = eventoRepository.findAll();

        for (Evento evento: eventos) {
            evento.setDatas(dataRepository.findByIdEvento(evento.getId()));
        }

        return eventos;
    }

    @Transactional
    public Evento getById(Long id) throws ResourceNotFoundException {
        Optional<Evento> evento =  eventoRepository.findById(id);
        if(evento.isPresent()){
            Evento foundEvento = evento.get();
            foundEvento.setDatas(dataRepository.findByIdEvento(foundEvento.getId()));
            return foundEvento;
        }else{
            throw new ResourceNotFoundException("Evento com ID " + id + " não encontrado");
        }
    }

    @Transactional
    public void deleteAll(){
        eventoRepository.deleteAll();
    }

}
