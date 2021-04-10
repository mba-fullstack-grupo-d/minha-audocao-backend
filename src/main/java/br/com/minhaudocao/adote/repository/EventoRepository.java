package br.com.minhaudocao.adote.repository;

import br.com.minhaudocao.adote.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventoRepository extends JpaRepository<Evento, Long> {

}
