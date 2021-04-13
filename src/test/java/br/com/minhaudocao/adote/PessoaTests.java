package br.com.minhaudocao.adote;

import br.com.minhaudocao.adote.entity.Pessoa;
import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.service.PessoaService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PessoaTests {


	@Autowired
	private PessoaService pessoaService;

	private Pessoa pessoa;

	@BeforeEach
	public void createPessoa(){
		pessoa = new Pessoa();
		pessoa.setNome("Paolo");
		pessoa.setSobrenome("Zilioti");
		pessoa.setEmail("paolo@zilioti.dev");
		pessoa.setImagem("image.jpg");
		pessoa.setSenha("123456");
	}

	@AfterEach
	public void deleteDataBase(){
		if(pessoaService.getAll() != null){
			pessoaService.deleteAll();
		}
	}

	@Test
	public void savePessoaSuccess() {
		pessoaService.save(pessoa);

		assertNotNull(pessoaService.getAll());
		assertEquals(pessoa.getNome(), pessoaService.getAll().get(0).getNome());
	}

	@Test
	public void savePessoaFailure() {
		pessoa.setNome(null);
		assertThrows(DataIntegrityViolationException.class, ()-> pessoaService.save(pessoa));
	}

	@Test
	public void getByIdSuccess(){
		Pessoa savedPessoa = pessoaService.save(pessoa);
		assertDoesNotThrow(() -> pessoaService.getById(savedPessoa.getIdPessoa()));
	}

	@Test
	public void getByIdFailure(){
		assertThrows(ResourceNotFoundException.class, () -> pessoaService.getById(1l));
	}

}
