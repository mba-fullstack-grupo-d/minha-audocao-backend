package br.com.minhaudocao.adote;

import br.com.minhaudocao.adote.entity.Endereco;
import br.com.minhaudocao.adote.entity.Formulario;
import br.com.minhaudocao.adote.entity.Instituicao;
import br.com.minhaudocao.adote.entity.Pet;
import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.service.FormularioService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.text.Normalizer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FormularioTests {

    @Autowired
    private FormularioService formularioService;

    private Formulario formulario;

    @BeforeEach
    public void createFormulario(){
        formulario = new Formulario();
        formulario.setNome("Você tem cachorros?");
        formulario.setObrigatorio(true);
        formulario.setOrdem(1);
        formulario.setTipo("texto livre");

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
        formulario.setInstituicao(instituicao);
    }

    @AfterEach
    public void deleteDataBase(){
        if(formularioService.getAll() != null){
            formularioService.deleteAll();
        }
    }

    @Test
    public void saveFormularioSuccess() {
        formularioService.save(formulario);

        assertNotNull(formularioService.getAll());
        assertEquals(formulario.getNome(), formularioService.getAll().get(0).getNome());
    }

    @Test
    public void saveFormularioFailure() {
        formulario.setNome(null);
        assertThrows(DataIntegrityViolationException.class, ()-> formularioService.save(formulario));
    }

    @Test
    public void getByIdSuccess(){
        Formulario savedFormulario = formularioService.save(formulario);
        assertDoesNotThrow(() -> formularioService.getById(savedFormulario.getId()));
    }

    @Test
    public void getByIdFailure(){
        assertThrows(ResourceNotFoundException.class, () -> formularioService.getById(1l));
    }

    @Test
    public void differentFormularioSameInstituicao(){
        Formulario novoFormulario = new Formulario();
        novoFormulario.setNome("Você tem gatos?");
        novoFormulario.setObrigatorio(true);
        novoFormulario.setOrdem(2);
        novoFormulario.setTipo("texto livre");
        novoFormulario.setInstituicao(formulario.getInstituicao());

        Formulario savedFormulario = formularioService.save(formulario);
        Formulario savedNovoFormulario = formularioService.save(novoFormulario);

        assertNotEquals(savedFormulario.getId(), savedNovoFormulario.getId());
        assertEquals(savedFormulario.getInstituicao().getId(), savedNovoFormulario.getInstituicao().getId());
    }

}
