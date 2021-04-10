package br.com.minhaudocao.adote.repository;

import br.com.minhaudocao.adote.entity.Data;
import br.com.minhaudocao.adote.entity.DataId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataRepository extends JpaRepository<Data, DataId> {
}
