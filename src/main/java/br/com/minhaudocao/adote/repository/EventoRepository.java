package br.com.minhaudocao.adote.repository;

import br.com.minhaudocao.adote.entity.Endereco;
import br.com.minhaudocao.adote.entity.Evento;
import br.com.minhaudocao.adote.entity.Instituicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface EventoRepository extends JpaRepository<Evento, Long> {

    public List<Evento> findByInstituicao(Instituicao instituicao);

    public List<Evento> findByNomeAndEndereco(String nome, Endereco endereco);

    public List<Evento> findByInstituicaoAndEndereco(Instituicao instituicao, Endereco endereco);

    public List<Evento> findByNome(String nome);

    public List<Evento> findByEndereco(Endereco endereco);

}
