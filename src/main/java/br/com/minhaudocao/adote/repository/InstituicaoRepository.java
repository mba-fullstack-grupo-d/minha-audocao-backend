package br.com.minhaudocao.adote.repository;

import br.com.minhaudocao.adote.entity.Endereco;
import br.com.minhaudocao.adote.entity.Instituicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InstituicaoRepository extends JpaRepository<Instituicao, Long> {

    public List<Instituicao> findByNome(String nome);

    public List<Instituicao> findByNomeAndEndereco(String nome, Endereco endereco);

    public List<Instituicao> findByEndereco(Endereco endereco);

    public Optional<Instituicao> findByEmail(String email);
}