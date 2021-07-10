package br.com.minhaudocao.adote;

import br.com.minhaudocao.adote.entity.Pessoa;
import br.com.minhaudocao.adote.exception.EmailExistsException;
import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.repository.AuthoritiesRepository;
import br.com.minhaudocao.adote.repository.UsersRepository;
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

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private AuthoritiesRepository authoritiesRepository;

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
		if(usersRepository.findAll() != null){
			authoritiesRepository.deleteAll();
			usersRepository.deleteAll();
		}
	}

	@Test
	public void savePessoaSuccess() {
		try {
			pessoaService.save(pessoa, null);
		} catch (EmailExistsException e) {
			e.printStackTrace();
		}

		assertNotNull(pessoaService.getAll());
		assertEquals(pessoa.getNome(), pessoaService.getAll().get(0).getNome());
	}

	@Test
	public void savePessoaFailure() {
		pessoa.setNome(null);
		assertThrows(DataIntegrityViolationException.class, ()-> pessoaService.save(pessoa, null));
	}

	@Test
	public void getByIdSuccess(){
		Pessoa savedPessoa = null;
		try {
			savedPessoa = pessoaService.save(pessoa, null);
		} catch (EmailExistsException e) {
			e.printStackTrace();
		}
		Pessoa finalSavedPessoa = savedPessoa;
		assertDoesNotThrow(() -> pessoaService.getById(finalSavedPessoa.getIdPessoa()));
	}

	@Test
	public void getByIdFailure(){
		assertThrows(ResourceNotFoundException.class, () -> pessoaService.getById(1l));
	}

}
