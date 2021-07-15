package br.com.minhaudocao.adote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import br.com.minhaudocao.adote.entity.Pessoa;

import java.util.Optional;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    public Optional<Pessoa> findByEmail(String email);
}