package br.com.minhaudocao.adote.repository;

import br.com.minhaudocao.adote.entity.Foto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FotoRepository extends JpaRepository<Foto, Long> {
}
