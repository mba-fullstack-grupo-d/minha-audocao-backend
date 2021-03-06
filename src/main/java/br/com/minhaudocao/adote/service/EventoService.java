package br.com.minhaudocao.adote.service;

import br.com.minhaudocao.adote.entity.*;
import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.model.EventoInstituicaoSearchRequest;
import br.com.minhaudocao.adote.repository.DataRepository;
import br.com.minhaudocao.adote.repository.EnderecoRepository;
import br.com.minhaudocao.adote.repository.EventoRepository;
import br.com.minhaudocao.adote.repository.InstituicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.ArrayList;
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
    public List<Evento> getByInstituicao(Long idInstituicao){
        Instituicao instituicao = new Instituicao();
        instituicao.setId(idInstituicao);
        List<Evento> eventos = eventoRepository.findByInstituicao(instituicao);
        for (Evento evento: eventos) {
            evento.setDatas(dataRepository.findByIdEvento(evento.getId()));
        }
        return eventos;
    }

    @Transactional
    public void deleteAll(){
        eventoRepository.deleteAll();
    }

    public void delete(Long id) throws ResourceNotFoundException {
        Optional<Evento> optionalToDelete = eventoRepository.findById(id);
        if(optionalToDelete.isPresent()){
            List<Data> datas = dataRepository.findByIdEvento(id);
            if(!datas.isEmpty()){
                for (Data data: datas) {
                    dataRepository.delete(data);
                }
            }
            eventoRepository.deleteById(id);
        }else{
            throw new ResourceNotFoundException("Evento não encontrado");
        }
    }

    public void deleteData(Long id) {
        dataRepository.deleteById(id);
    }

    public List<Evento> search(EventoInstituicaoSearchRequest search) {
        List<Endereco> enderecos = null;
        List<Evento> eventos = null;
        List<Instituicao> instituicaos = null;

        if(search.getBairro() != null && search.getCidade() != null) {
            enderecos = enderecoRepository.findByCidadeAndBairro(search.getCidade(), search.getBairro());
        }else if(search.getBairro() != null){
            enderecos = enderecoRepository.findByBairro(search.getBairro());
        }else if(search.getCidade() != null){
            enderecos = enderecoRepository.findByCidade(search.getCidade());
        }

        if(search.getNome() != null){
            instituicaos = instituicaoRepository.findByNome(search.getNome());
        }

        if(enderecos != null && instituicaos != null){
            for(Endereco endereco: enderecos){
                for(Instituicao instituicao:instituicaos){
                    if(eventos == null){
                        eventos = eventoRepository.findByInstituicaoAndEndereco(instituicao, endereco);
                    }else{
                        eventos.addAll(eventoRepository.findByInstituicaoAndEndereco(instituicao, endereco));
                    }
                }
            }
        }else if(enderecos != null){
            for(Endereco endereco: enderecos){
                if(eventos == null){
                    eventos = eventoRepository.findByEndereco(endereco);
                }else {
                    eventos.addAll(eventoRepository.findByEndereco(endereco));
                }
            }
        }else if(instituicaos != null){
            for(Instituicao instituicao:instituicaos){
                if(eventos == null){
                    eventos = eventoRepository.findByInstituicao(instituicao);
                }else{
                    eventos.addAll(eventoRepository.findByInstituicao(instituicao));
                }
            }
        }

        if(eventos != null){
            for (Evento evento: eventos) {
                evento.setDatas(dataRepository.findByIdEvento(evento.getId()));
            }
        }

        return eventos;
    }

    @Transactional
    public void update(Evento evento) throws ResourceNotFoundException {
        Optional<Evento> eventoToUpdate = eventoRepository.findById(evento.getId());

        if(eventoToUpdate.isPresent()){
            Optional<Instituicao> instituicaoToUpdate = instituicaoRepository.findById(evento.getInstituicao().getId());
            Optional<Endereco> enderecoInstituicaoToUpdate = enderecoRepository.findById(evento.getInstituicao().getEndereco().getId());

            enderecoInstituicaoToUpdate.get().setCidade(evento.getInstituicao().getEndereco().getCidade());
            enderecoInstituicaoToUpdate.get().setEstado(evento.getInstituicao().getEndereco().getEstado());
            enderecoInstituicaoToUpdate.get().setLogradouro(evento.getInstituicao().getEndereco().getLogradouro());
            enderecoInstituicaoToUpdate.get().setNumero(evento.getInstituicao().getEndereco().getNumero());
            enderecoInstituicaoToUpdate.get().setCep(evento.getInstituicao().getEndereco().getCep());
            enderecoInstituicaoToUpdate.get().setBairro(evento.getInstituicao().getEndereco().getBairro());
            instituicaoToUpdate.get().setEndereco(enderecoInstituicaoToUpdate.get());

            Optional<Endereco> enderecoToUpdate = enderecoRepository.findById(evento.getEndereco().getId());

            enderecoToUpdate.get().setCidade(evento.getEndereco().getCidade());
            enderecoToUpdate.get().setEstado(evento.getEndereco().getEstado());
            enderecoToUpdate.get().setLogradouro(evento.getEndereco().getLogradouro());
            enderecoToUpdate.get().setNumero(evento.getEndereco().getNumero());
            enderecoToUpdate.get().setCep(evento.getEndereco().getCep());
            enderecoToUpdate.get().setBairro(evento.getEndereco().getBairro());

            eventoToUpdate.get().setNome(evento.getNome());
            eventoToUpdate.get().setDescricao(evento.getDescricao());
            eventoToUpdate.get().setInstituicao(evento.getInstituicao());
            eventoToUpdate.get().setEndereco(enderecoToUpdate.get());

            List<Data> savedDatas = new LinkedList<>();

            if(!evento.getDatas().isEmpty()){
                for(Data dataToUpdate: evento.getDatas()){
                    Optional<Data> dataResponse = dataRepository.findById(dataToUpdate.getId());
                    dataResponse.get().setData(dataToUpdate.getData());
                    dataResponse.get().setHoraInicio(dataToUpdate.getHoraInicio());
                    dataResponse.get().setHoraFim(dataToUpdate.getHoraFim());
                    savedDatas.add(dataRepository.save(dataResponse.get()));
                }
            }
            eventoToUpdate.get().setDatas(savedDatas);
            eventoRepository.save(eventoToUpdate.get());
        }else{
            throw new ResourceNotFoundException("Evento não encontrado");
        }
    }
}
