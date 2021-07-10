package br.com.minhaudocao.adote.repository;

import br.com.minhaudocao.adote.entity.Formulario;
import br.com.minhaudocao.adote.entity.Instituicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface FormularioRepository extends JpaRepository<Formulario, Long> {

    public List<Formulario> findByInstituicao(Instituicao instituicao);
}
