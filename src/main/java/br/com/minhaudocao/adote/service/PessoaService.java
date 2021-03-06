package br.com.minhaudocao.adote.service;

import br.com.minhaudocao.adote.entity.*;
import br.com.minhaudocao.adote.exception.EmailExistsException;
import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import java.util.List;
import java.util.Optional;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private AuthoritiesRepository authoritiesRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private S3RepositoryImpl s3Repository;

    @Transactional
    public Pessoa save(Pessoa pessoa) throws EmailExistsException {
        if(usersRepository.findById(pessoa.getEmail()).isPresent()){
            throw new EmailExistsException("Email já cadastrado");
        }

        Endereco endereco = pessoa.getEndereco();
        if (endereco != null) {
            Endereco savedEndereco = null;
            if (endereco.getId() != null) {
                savedEndereco = enderecoRepository.findById(endereco.getId()).get();
            } else {
                savedEndereco = enderecoRepository.save(endereco);
            }
            pessoa.setEndereco(savedEndereco);
        }

        Users user = new Users();

        user.setUsername(pessoa.getEmail());
        user.setPassword(encoder.encode(pessoa.getSenha()));
        user.setEnabled(true);
        pessoa.setSenha("");

        Users savedUser = usersRepository.saveAndFlush(user);

        Authorities authority = new Authorities();
        AuthoritiesPK authorityPK = new AuthoritiesPK();
        authorityPK.setUsers(savedUser);
        authorityPK.setAuthority("ROLE_USER");
        authority.setId(authorityPK);

        authoritiesRepository.saveAndFlush(authority);

        return pessoaRepository.saveAndFlush(pessoa);
    }

    @Transactional
    public List<Pessoa> getAll(){
        return pessoaRepository.findAll();
    }

    @Transactional
    public Pessoa getById(Long id) throws ResourceNotFoundException {
        Optional<Pessoa> pessoa =  pessoaRepository.findById(id);
        if(pessoa.isPresent()){
            return pessoa.get();
        }else{
            throw new ResourceNotFoundException("Pessoa com ID " + id + " não encontrada");
        }
    }

    @Transactional
    public void deleteAll(){
        pessoaRepository.deleteAll();
    }

    public void delete(Long id) throws ResourceNotFoundException {
        Optional<Pessoa> toDelete = pessoaRepository.findById(id);
        if(toDelete.isPresent()){
            Pessoa pessoaToDelete = toDelete.get();
            Optional<Users> userToDelete = usersRepository.findById(pessoaToDelete.getEmail());
            List<Authorities> auths = authoritiesRepository.findByUsername(pessoaToDelete.getEmail());
            for (Authorities auth:auths) {
                authoritiesRepository.deleteById(auth.getId());
            }
            usersRepository.delete(userToDelete.get());
            pessoaRepository.deleteById(id);
        }else{
            throw new ResourceNotFoundException("Pessoa não econtrada");
        }


    }

    public Long getIdUser(String email) throws ResourceNotFoundException {
        Optional<Pessoa> optionalPessoa = pessoaRepository.findByEmail(email);
        if(optionalPessoa.isPresent()){
            return optionalPessoa.get().getIdPessoa();
        }else{
            throw new ResourceNotFoundException("Pessoa não encontrada");
        }
    }

    @Transactional
    public void update(Pessoa pessoa) throws ResourceNotFoundException {
        Optional<Pessoa> pessoaToUpdate = pessoaRepository.findById(pessoa.getIdPessoa());
        Optional<Endereco> enderecoToUpdate = enderecoRepository.findById(pessoa.getEndereco().getId());
        if(pessoaToUpdate.isPresent()){

            enderecoToUpdate.get().setCidade(pessoa.getEndereco().getCidade());
            enderecoToUpdate.get().setEstado(pessoa.getEndereco().getEstado());
            enderecoToUpdate.get().setLogradouro(pessoa.getEndereco().getLogradouro());
            enderecoToUpdate.get().setNumero(pessoa.getEndereco().getNumero());
            enderecoToUpdate.get().setCep(pessoa.getEndereco().getCep());
            enderecoToUpdate.get().setBairro(pessoa.getEndereco().getBairro());

            pessoaToUpdate.get().setNome(pessoa.getNome());
            pessoaToUpdate.get().setSobrenome(pessoa.getSobrenome());
            pessoaToUpdate.get().setEmail(pessoa.getEmail());
            pessoaToUpdate.get().setTelefone(pessoa.getTelefone());
            pessoaToUpdate.get().setEndereco(enderecoToUpdate.get());
            pessoaRepository.save(pessoaToUpdate.get());
        }else{
            throw new ResourceNotFoundException("Pessoa não encontrada");
        }
    }
}
