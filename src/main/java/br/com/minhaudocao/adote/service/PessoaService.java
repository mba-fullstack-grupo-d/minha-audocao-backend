package br.com.minhaudocao.adote.service;

import br.com.minhaudocao.adote.entity.*;
import br.com.minhaudocao.adote.exception.EmailExistsException;
import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.repository.AuthoritiesRepository;
import br.com.minhaudocao.adote.repository.EnderecoRepository;
import br.com.minhaudocao.adote.repository.PessoaRepository;
import br.com.minhaudocao.adote.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        return pessoaRepository.save(pessoa);
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

}
