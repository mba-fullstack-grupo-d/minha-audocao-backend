package br.com.minhaudocao.adote;

import br.com.minhaudocao.adote.entity.*;
import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.service.EventoService;
import br.com.minhaudocao.adote.service.PetService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EventoTests {

    @Autowired
    private EventoService eventoService;

    private Evento evento;

    @BeforeEach
    public void createEvento(){
        evento = new Evento();
        evento.setNome("Evento de Adoção");
        evento.setDescricao("Venha adotar");

        Instituicao instituicao = new Instituicao();
        instituicao.setNome("Minha Audoção");
        instituicao.setDescricao("Plataforma para adoções");
        instituicao.setTelefone("999999");
        instituicao.setImagem("imagem.png");
        instituicao.setEmail("contato@minhaudocao.com.br");
        instituicao.setSenha("123456");

        Endereco endereco = new Endereco();
        endereco.setCep(123456);
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setLogradouro("Rua Augusta");
        endereco.setNumero(10);

        instituicao.setEndereco(endereco);
        evento.setInstituicao(instituicao);

        Endereco enderecoEvento = new Endereco();
        enderecoEvento.setCep(123456);
        enderecoEvento.setCidade("São Bernardo");
        enderecoEvento.setEstado("SP");
        enderecoEvento.setLogradouro("Avenida Kennedy");
        enderecoEvento.setNumero(1000);
        evento.setEndereco(enderecoEvento);

        Data data1 = new Data();
        data1.setData(LocalDate.now());
        data1.setHoraInicio("8");
        data1.setHoraFim("10");

        Data data2 = new Data();
        data2.setData(LocalDate.of(2021,5,1));
        data2.setHoraInicio("10");
        data2.setHoraFim("14");

        List<Data> datas = List.of(data1, data2);
        evento.setDatas(datas);
    }

    @AfterEach
    public void deleteDataBase(){
        if(eventoService.getAll() != null){
            eventoService.deleteAll();
        }
    }

    @Test
    public void saveEventoSuccess() {
        eventoService.save(evento);

        assertNotNull(eventoService.getAll());
        assertEquals(evento.getNome(), eventoService.getAll().get(0).getNome());
    }

    @Test
    public void saveEventoFailure() {
        evento.setNome(null);
        assertThrows(DataIntegrityViolationException.class, ()-> eventoService.save(evento));
    }

    @Test
    public void getByIdSuccess(){
        Evento savedEvento = eventoService.save(evento);
        assertDoesNotThrow(() -> eventoService.getById(savedEvento.getId()));
    }

    @Test
    public void getByIdFailure(){
        assertThrows(ResourceNotFoundException.class, () -> eventoService.getById(1l));
    }

    @Test
    public void differentEventoSameInstituicao(){
        Evento novoEvento = new Evento();
        novoEvento.setNome("Feira geral");
        novoEvento.setDescricao("nova feira");
        novoEvento.setDatas(evento.getDatas());
        novoEvento.setEndereco(evento.getEndereco());
        novoEvento.setInstituicao(evento.getInstituicao());

        Evento savedEvento = eventoService.save(evento);
        Evento savedNovoEvento = eventoService.save(novoEvento);

        assertNotEquals(savedEvento.getId(), savedNovoEvento.getId());
        assertEquals(savedEvento.getInstituicao().getId(), savedNovoEvento.getInstituicao().getId());
        assertEquals(savedEvento.getEndereco().getId(), savedNovoEvento.getEndereco().getId());
        assertEquals(savedEvento.getDatas().get(0).getId(), savedNovoEvento.getDatas().get(0).getId());
    }
}
