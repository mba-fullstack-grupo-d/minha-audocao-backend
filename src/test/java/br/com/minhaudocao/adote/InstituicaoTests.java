package br.com.minhaudocao.adote;

import br.com.minhaudocao.adote.entity.Endereco;
import br.com.minhaudocao.adote.entity.Instituicao;
import br.com.minhaudocao.adote.entity.Pet;
import br.com.minhaudocao.adote.exception.EmailExistsException;
import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.repository.AuthoritiesRepository;
import br.com.minhaudocao.adote.repository.UsersRepository;
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

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AuthoritiesRepository authoritiesRepository;

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
        if(usersRepository.findAll() != null){
            authoritiesRepository.deleteAll();
            usersRepository.deleteAll();
        }
    }

    @Test
    public void saveInstituicaoSuccess() {
        try {
            instituicaoService.save(instituicao);
        } catch (EmailExistsException e) {
            e.printStackTrace();
        }

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
        Instituicao savedInstituicao = null;
        try {
            savedInstituicao = instituicaoService.save(instituicao);
            Instituicao finalSavedInstituicao = savedInstituicao;
            assertDoesNotThrow(() -> instituicaoService.getById(finalSavedInstituicao.getId()));
        } catch (EmailExistsException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void getByIdFailure(){
        assertThrows(ResourceNotFoundException.class, () -> instituicaoService.getById(1l));
    }



}
