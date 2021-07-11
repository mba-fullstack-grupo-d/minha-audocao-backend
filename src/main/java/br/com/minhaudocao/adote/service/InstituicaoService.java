package br.com.minhaudocao.adote.service;

import br.com.minhaudocao.adote.entity.*;
import br.com.minhaudocao.adote.exception.EmailExistsException;
import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InstituicaoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AuthoritiesRepository authoritiesRepository;

    @Autowired
    private FormularioRepository formularioRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetService petService;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private EventoService eventoService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private S3RepositoryImpl s3Repository;

    @Transactional
    public Instituicao save(Instituicao instituicao) throws EmailExistsException {
        if(usersRepository.findById(instituicao.getEmail()).isPresent()){
            throw new EmailExistsException("Email já cadastrado");
        }


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
        Users user = new Users();

        user.setUsername(instituicao.getEmail());
        user.setPassword(encoder.encode(instituicao.getSenha()));
        user.setEnabled(true);
        instituicao.setSenha("");

        Users savedUser = usersRepository.saveAndFlush(user);

        Authorities authority = new Authorities();
        AuthoritiesPK authorityPK = new AuthoritiesPK();
        authorityPK.setUsers(savedUser);
        authorityPK.setAuthority("ROLE_INSTITUICAO");
        authority.setId(authorityPK);

        authoritiesRepository.saveAndFlush(authority);

        return instituicaoRepository.save(instituicao);
    }

    @Transactional
    public List<Instituicao> getAll(){
        return instituicaoRepository.findAll();
    }

    @Transactional
    public Instituicao getById(Long id) throws ResourceNotFoundException {
        Optional<Instituicao> instituicao =  instituicaoRepository.findById(id);
        if(instituicao.isPresent()){
            return instituicao.get();
        }else{
            throw new ResourceNotFoundException("Instituição com ID " + id + " não encontrada");
        }
    }

    @Transactional
    public void deleteAll(){
        instituicaoRepository.deleteAll();
    }

    @Transactional
    public void delete(Long id) throws ResourceNotFoundException {
        Optional<Instituicao> optionalToDelete = instituicaoRepository.findById(id);
        if(optionalToDelete.isPresent()){
            List<Formulario> formularios = formularioRepository.findByInstituicao(optionalToDelete.get());
            if(!formularios.isEmpty()){
                for (Formulario formulario: formularios) {
                    formularioRepository.delete(formulario);
                }
            }
            List<Pet> pets = petRepository.findByInstituicao(optionalToDelete.get());
            for (Pet pet:pets) {
                petService.delete(pet.getId());
            }
            List<Evento> eventos = eventoRepository.findByInstituicao(optionalToDelete.get());
            for (Evento evento: eventos){
                eventoService.delete(evento.getId());
            }
            instituicaoRepository.delete(optionalToDelete.get());
        }else{
            throw new ResourceNotFoundException("Instituição não encontrada");
        }
    }
}

