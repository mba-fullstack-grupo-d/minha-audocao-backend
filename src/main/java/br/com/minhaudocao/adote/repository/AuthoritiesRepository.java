package br.com.minhaudocao.adote.repository;

import br.com.minhaudocao.adote.entity.Authorities;
import br.com.minhaudocao.adote.entity.AuthoritiesPK;
import br.com.minhaudocao.adote.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthoritiesRepository extends JpaRepository<Authorities, AuthoritiesPK> {

    @Query(value = "select * from authorities a where a.username = ?1", nativeQuery = true)
    public List<Authorities> findByUsername(String username);
}
