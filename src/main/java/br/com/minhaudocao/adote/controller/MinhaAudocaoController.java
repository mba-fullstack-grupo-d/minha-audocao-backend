package br.com.minhaudocao.adote.controller;

import java.util.List;
import br.com.minhaudocao.adote.entity.*;
import br.com.minhaudocao.adote.exception.EmailExistsException;
import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.model.AuthenticationRequest;
import br.com.minhaudocao.adote.model.AuthenticationResponse;
import br.com.minhaudocao.adote.service.*;
import br.com.minhaudocao.adote.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(path = "/api")
@CrossOrigin(origins = "*")
@RestController
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

    @Autowired
    private S3Service s3Service;

    @Autowired
    private FotoService fotoService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;


    @PostMapping(path = "/pessoa/add")
    public ResponseEntity<Pessoa> addNewUser(@RequestBody Pessoa pessoa) {
        try {
            pessoaService.save(pessoa);
        } catch (EmailExistsException e){
            Pessoa response = new Pessoa();
            response.setEmail(pessoa.getEmail() + " já usado");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(pessoa);
        }
        return ResponseEntity.ok().body(pessoa);
    }

    @PostMapping("/pet/add")
    @PreAuthorize("hasRole('INSTITUICAO') or hasRole('ADMIN')")
    public ResponseEntity<Pet> addNewPet(@RequestBody Pet pet) {
        try {
            petService.save(pet);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(pet);
        }
        return ResponseEntity.ok().body(pet);
    }

    @PostMapping("/instituicao/add")
    public ResponseEntity<Instituicao> addNewInstituicao(@RequestBody Instituicao instituicao) {
        try {
            instituicaoService.save(instituicao);
        } catch (EmailExistsException e){
            Instituicao response = new Instituicao();
            response.setEmail(instituicao.getEmail() + " já usado");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(instituicao);
        }
        return ResponseEntity.ok().body(instituicao);
    }

    @PostMapping("/endereco/add")
    public ResponseEntity<Endereco> addNewEndereco(@RequestBody Endereco endereco) {
        try {
            enderecoService.save(endereco);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(endereco);
        }
        return ResponseEntity.ok().body(endereco);
    }

    @PostMapping("/formulario/add")
    @PreAuthorize("hasRole('INSTITUICAO') or hasRole('ADMIN')")
    public ResponseEntity<Formulario> addNewFormulario(@RequestBody Formulario formulario) {
        try {
            formularioService.save(formulario);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(formulario);
        }
        return ResponseEntity.ok().body(formulario);
    }

    @PostMapping("/evento/add")
    @PreAuthorize("hasRole('INSTITUICAO') or hasRole('ADMIN')")
    public ResponseEntity<Evento> addNewEvento(@RequestBody Evento evento) {
        try {
            eventoService.save(evento);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(evento);
        }
        return ResponseEntity.ok().body(evento);
    }

    @GetMapping(path = "/pessoa/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public @ResponseBody ResponseEntity<Pessoa> getPessoa(@PathVariable("id") Long id) {
        try {
            Pessoa pessoa = pessoaService.getById(id);
            return ResponseEntity.ok().body(pessoa);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/pessoa/all")
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('USER') or hasRole('INSTITUICAO') or hasRole('ADMIN')")
    public @ResponseBody ResponseEntity<Formulario> getFormulario(@PathVariable("id") Long id) {
        try {
            Formulario formulario  = formularioService.getById(id);
            return ResponseEntity.ok().body(formulario);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/formulario/all")
    @PreAuthorize("hasRole('USER') or hasRole('INSTITUICAO') or hasRole('ADMIN')")
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

    @PostMapping("/uploadFoto")
    @PreAuthorize("hasRole('USER') or hasRole('INSTITUICAO') or hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<String> uploadFile(@RequestPart(value = "file") MultipartFile file) {
        try {
            return ResponseEntity.ok().body(s3Service.uploadFile(file));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteFoto")
    @PreAuthorize("hasRole('USER') or hasRole('INSTITUICAO') or hasRole('ADMIN')")
    @ResponseBody
    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
        return this.s3Service.deleteFile(fileUrl);
    }

    @PostMapping("/fotopet/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Foto> addNewFotoPet(@RequestBody Foto foto) {
        try {
            fotoService.save(foto);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(foto);
        }
        return ResponseEntity.ok().body(foto);
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
            String jwt = jwtTokenUtil.generateToken(userPrincipal);
            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        }
        catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponse("Senha ou Email incorretos"));
        }
    }

}
