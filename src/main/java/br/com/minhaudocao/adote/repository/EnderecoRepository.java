package br.com.minhaudocao.adote.repository;

import br.com.minhaudocao.adote.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    public List<Endereco> findByCidadeAndBairro(String cidade, String bairro);

    public List<Endereco> findByCidade(String cidade);

    public List<Endereco> findByBairro(String bairro);

}