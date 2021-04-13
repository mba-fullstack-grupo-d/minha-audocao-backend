package br.com.minhaudocao.adote;

import br.com.minhaudocao.adote.entity.Endereco;
import br.com.minhaudocao.adote.entity.Instituicao;
import br.com.minhaudocao.adote.entity.Pet;
import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.service.InstituicaoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InstituicaoTests {

    @Autowired
    private InstituicaoService instituicaoService;

    private Instituicao instituicao;

    @BeforeEach
    public void createInstituicao(){
        instituicao = new Instituicao();
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
    }

    @AfterEach
    public void deleteDataBase(){
        if(instituicaoService.getAll() != null){
            instituicaoService.deleteAll();
        }
    }

    @Test
    public void saveInstituicaoSuccess() {
        instituicaoService.save(instituicao);

        assertNotNull(instituicaoService.getAll());
        assertEquals(instituicao.getNome(), instituicaoService.getAll().get(0).getNome());
    }

    @Test
    public void saveInstituicaoFailure() {
        instituicao.setNome(null);
        assertThrows(DataIntegrityViolationException.class, ()-> instituicaoService.save(instituicao));
    }

    @Test
    public void getByIdSuccess(){
        Instituicao savedInstituicao = instituicaoService.save(instituicao);
        assertDoesNotThrow(() -> instituicaoService.getById(savedInstituicao.getId()));
    }

    @Test
    public void getByIdFailure(){
        assertThrows(ResourceNotFoundException.class, () -> instituicaoService.getById(1l));
    }



}
