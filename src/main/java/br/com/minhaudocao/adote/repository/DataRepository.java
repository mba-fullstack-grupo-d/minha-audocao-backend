package br.com.minhaudocao.adote.repository;

import br.com.minhaudocao.adote.entity.Data;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataRepository extends JpaRepository<Data, Long> {
}
