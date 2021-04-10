package br.com.minhaudocao.adote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import br.com.minhaudocao.adote.entity.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}