package br.com.minhaudocao.adote.service;

import br.com.minhaudocao.adote.entity.Endereco;
import br.com.minhaudocao.adote.entity.Formulario;
import br.com.minhaudocao.adote.entity.Instituicao;
import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.repository.EnderecoRepository;
import br.com.minhaudocao.adote.repository.FormularioRepository;
import br.com.minhaudocao.adote.repository.InstituicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import java.util.List;
import java.util.Optional;

@Service
public class FormularioService {

    @Autowired
    private FormularioRepository formularioRepository;

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Transactional
    public Formulario save(Formulario formulario) {
        Instituicao instituicao = formulario.getInstituicao();
        if (instituicao != null) {
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

            Instituicao savedInstituicao = null;
            if (instituicao.getId() != null) {
                savedInstituicao = instituicaoRepository.findById(instituicao.getId()).get();
            } else {
                savedInstituicao = instituicaoRepository.save(instituicao);
            }
            formulario.setInstituicao(savedInstituicao);

        }

        return formularioRepository.save(formulario);
    }

    @Transactional
    public List<Formulario> getAll() {
        return formularioRepository.findAll();
    }

    @Transactional
    public Formulario getById(Long id) throws ResourceNotFoundException {
        Optional<Formulario> formulario =  formularioRepository.findById(id);
        if(formulario.isPresent()){
            return formulario.get();
        }else{
            throw new ResourceNotFoundException("Formulário com ID " + id + " não encontrado");
        }
    }

    @Transactional
    public void deleteAll(){
        formularioRepository.deleteAll();
    }

    @Transactional
    public List<Formulario> getByInstituicao(Long idInstituicao) {
        Instituicao instituicao = new Instituicao();
        instituicao.setId(idInstituicao);
        return formularioRepository.findByInstituicao(instituicao);
    }

    public void delete(Long id) {
        formularioRepository.deleteById(id);
    }

    public void update(Formulario formulario) throws ResourceNotFoundException {

        Optional<Formulario> formularioToUpdate =  formularioRepository.findById(formulario.getId());

        if(formularioToUpdate.isPresent()){
            Optional<Instituicao> instituicaoToUpdate = instituicaoRepository.findById(formulario.getInstituicao().getId());
            Optional<Endereco> enderecoToUpdate = enderecoRepository.findById(formulario.getInstituicao().getEndereco().getId());

            enderecoToUpdate.get().setCidade(formulario.getInstituicao().getEndereco().getCidade());
            enderecoToUpdate.get().setEstado(formulario.getInstituicao().getEndereco().getEstado());
            enderecoToUpdate.get().setLogradouro(formulario.getInstituicao().getEndereco().getLogradouro());
            enderecoToUpdate.get().setNumero(formulario.getInstituicao().getEndereco().getNumero());
            enderecoToUpdate.get().setCep(formulario.getInstituicao().getEndereco().getCep());
            enderecoToUpdate.get().setBairro(formulario.getInstituicao().getEndereco().getBairro());

            instituicaoToUpdate.get().setNome(formulario.getInstituicao().getNome());
            instituicaoToUpdate.get().setDescricao(formulario.getInstituicao().getDescricao());
            instituicaoToUpdate.get().setEmail(formulario.getInstituicao().getEmail());
            instituicaoToUpdate.get().setTelefone(formulario.getInstituicao().getTelefone());
            instituicaoToUpdate.get().setEndereco(enderecoToUpdate.get());

            formularioToUpdate.get().setNome(formulario.getNome());
            formularioToUpdate.get().setTipo(formulario.getTipo());
            formularioToUpdate.get().setObrigatorio(formulario.getObrigatorio());
            formularioToUpdate.get().setOrdem(formulario.getOrdem());
            formularioToUpdate.get().setInstituicao(instituicaoToUpdate.get());

            formularioRepository.save(formularioToUpdate.get());
        }else{
            throw new ResourceNotFoundException("Formulario não encontrada");
        }


    }
}
