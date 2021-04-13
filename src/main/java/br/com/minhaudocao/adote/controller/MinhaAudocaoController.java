package br.com.minhaudocao.adote.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.Normalizer;
import java.util.List;

import br.com.minhaudocao.adote.entity.*;
import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.minhaudocao.adote.repository.PessoaRepository;
import br.com.minhaudocao.adote.repository.PetRepository;

@Controller
@RequestMapping(path = "/api")
public class MinhaAudocaoController {

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private PetService petService;

    @Autowired
    private InstituicaoService instituicaoService;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private FormularioService formularioService;

    @Autowired
    private EventoService eventoService;

    @PostMapping(path = "/pessoa/add")
    public ResponseEntity<Pessoa> addNewUser(Pessoa pessoa) {
        try {
            pessoaService.save(pessoa);
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
            return ResponseEntity.badRequest().body(pessoa);
        }
        return ResponseEntity.ok().body(pessoa);
    }

    @PostMapping("/pet/add")
    public ResponseEntity<Pet> addNewPet(@RequestBody Pet pet) {
        try {
            petService.save(pet);
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
            return ResponseEntity.badRequest().body(pet);
        }
        return ResponseEntity.ok().body(pet);
    }

    @PostMapping("/instituicao/add")
    public ResponseEntity<Instituicao> addNewInstituicao(@RequestBody Instituicao instituicao) {
        try {
            instituicaoService.save(instituicao);
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
            return ResponseEntity.badRequest().body(instituicao);
        }
        return ResponseEntity.ok().body(instituicao);
    }

    @PostMapping("/endereco/add")
    public ResponseEntity<Endereco> addNewEndereco(@RequestBody Endereco endereco) {
        try {
            enderecoService.save(endereco);
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
            return ResponseEntity.badRequest().body(endereco);
        }
        return ResponseEntity.ok().body(endereco);
    }

    @PostMapping("/formulario/add")
    public ResponseEntity<Formulario> addNewFormulario(@RequestBody Formulario formulario) {
        try {
            formularioService.save(formulario);
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
            return ResponseEntity.badRequest().body(formulario);
        }
        return ResponseEntity.ok().body(formulario);
    }

    @PostMapping("/evento/add")
    public ResponseEntity<Evento> addNewEvento(@RequestBody Evento evento) {
        try {
            eventoService.save(evento);
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
            return ResponseEntity.badRequest().body(evento);
        }
        return ResponseEntity.ok().body(evento);
    }

    @GetMapping(path = "/pessoa/{id}")
    public @ResponseBody ResponseEntity<Pessoa> getPessoa(@PathVariable("id") Long id) {
        try {
            Pessoa pessoa = pessoaService.getById(id);
            return ResponseEntity.ok().body(pessoa);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/pessoa/all")
    public @ResponseBody
    List<Pessoa> getAllUsers() {
        return pessoaService.getAll();
    }

    @GetMapping(path = "/pet/{id}")
    public @ResponseBody ResponseEntity<Pet> getPet(@PathVariable("id") Long id) {
        try {
            Pet pet = petService.getById(id);
            return ResponseEntity.ok().body(pet);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/pet/all")
    public @ResponseBody
    List<Pet> getAllPets() {
        return petService.getAll();
    }

    @GetMapping(path = "/instituicao/{id}")
    public @ResponseBody ResponseEntity<Instituicao> getInstituicao(@PathVariable("id") Long id) {
        try {
            Instituicao instituicao = instituicaoService.getById(id);
            return ResponseEntity.ok().body(instituicao);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/instituicao/all")
    public @ResponseBody
    List<Instituicao> getAllInstituicoes() {
        return instituicaoService.getAll();
    }

    @GetMapping(path = "/formulario/{id}")
    public @ResponseBody ResponseEntity<Formulario> getFormulario(@PathVariable("id") Long id) {
        try {
            Formulario formulario  = formularioService.getById(id);
            return ResponseEntity.ok().body(formulario);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/formulario/all")
    public @ResponseBody
    List<Formulario> getAllFormularios() {
        return formularioService.getAll();
    }

    @GetMapping(path = "/evento/{id}")
    public @ResponseBody ResponseEntity<Evento> getEvento(@PathVariable("id") Long id) {
        try {
            Evento evento  = eventoService.getById(id);
            return ResponseEntity.ok().body(evento);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/evento/all")
    public @ResponseBody List<Evento> getAllEventos() {
        return eventoService.getAll();
    }

}
