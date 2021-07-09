package br.com.minhaudocao.adote.repository;

import br.com.minhaudocao.adote.entity.Authorities;
import br.com.minhaudocao.adote.entity.AuthoritiesPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthoritiesRepository extends JpaRepository<Authorities, AuthoritiesPK> {
}
