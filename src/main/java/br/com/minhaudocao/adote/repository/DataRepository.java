package br.com.minhaudocao.adote.repository;

import br.com.minhaudocao.adote.entity.Data;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataRepository extends JpaRepository<Data, Long> {

    public List<Data> findByIdEvento(Long idEvento);
}
