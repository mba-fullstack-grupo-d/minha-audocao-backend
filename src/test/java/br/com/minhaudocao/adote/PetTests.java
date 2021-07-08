package br.com.minhaudocao.adote;

import br.com.minhaudocao.adote.entity.Endereco;
import br.com.minhaudocao.adote.entity.Instituicao;
import br.com.minhaudocao.adote.entity.Pet;
import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.service.PetService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PetTests {


    @Autowired
    private PetService petService;

    private Pet pet;

    @BeforeEach
    public void createPet(){
        pet = new Pet();
        pet.setNome("Carlito");
        pet.setRaca("vira lata");
        pet.setEspecie("cachorro");
        pet.setAdotado(false);
        pet.setIdade(10);
        pet.setDescricao("Mascote do Minha Audoção");

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
        pet.setInstituicao(instituicao);
    }

    @AfterEach
    public void deleteDataBase(){
        if(petService.getAll() != null){
            petService.deleteAll();
        }
    }

    @Test
    public void savePetSuccess() {
        petService.save(pet);

        assertNotNull(petService.getAll());
        assertEquals(pet.getNome(), petService.getAll().get(0).getNome());
    }

    @Test
    public void savePetFailure() {
        pet.setNome(null);
        assertThrows(DataIntegrityViolationException.class, ()-> petService.save(pet));
    }

    @Test
    public void getByIdSuccess(){
        Pet savedPet = petService.save(pet);
        assertDoesNotThrow(() -> petService.getById(savedPet.getId()));
    }

    @Test
    public void getByIdFailure(){
        assertThrows(ResourceNotFoundException.class, () -> petService.getById(1l));
    }
    
    @Test
    public void differentPetSameInstituicao(){
        Pet novoPet = new Pet();
        novoPet.setNome("Roger");
        novoPet.setRaca("vira lata");
        novoPet.setEspecie("gato");
        novoPet.setAdotado(false);
        novoPet.setIdade(10);
        novoPet.setDescricao("Belo gatinho");
        novoPet.setInstituicao(pet.getInstituicao());
        
        Pet savedPet = petService.save(pet);
        Pet savedNovoPet = petService.save(novoPet);
        
        assertNotEquals(savedPet.getId(), savedNovoPet.getId());
        assertEquals(savedPet.getInstituicao().getId(), savedNovoPet.getInstituicao().getId());
    }

}
