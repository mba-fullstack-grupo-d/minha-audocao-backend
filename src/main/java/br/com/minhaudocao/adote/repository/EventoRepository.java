package br.com.minhaudocao.adote.repository;

import br.com.minhaudocao.adote.entity.Evento;
import br.com.minhaudocao.adote.entity.Instituicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface EventoRepository extends JpaRepository<Evento, Long> {

    public List<Evento> findByInstituicao(Instituicao instituicao);

}
