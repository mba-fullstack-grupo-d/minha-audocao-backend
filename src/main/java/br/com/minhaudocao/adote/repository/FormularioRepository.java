package br.com.minhaudocao.adote.repository;

import br.com.minhaudocao.adote.entity.Formulario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormularioRepository extends JpaRepository<Formulario, Long> {
}
