package br.com.minhaudocao.adote.repository;

import br.com.minhaudocao.adote.entity.Endereco;
import br.com.minhaudocao.adote.entity.Instituicao;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.minhaudocao.adote.entity.Pet;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {

    public List<Pet> findByInstituicao(Instituicao instituicao);

    public List<Pet> findByInstituicaoAndIdadeAndGeneroAndEspecie(
            Instituicao instituicao,
            Integer idade,
            String genero,
            String especie
    );

    public List<Pet> findByInstituicaoAndGeneroAndEspecie(
            Instituicao instituicao,
            String genero,
            String especie
    );

    public List<Pet> findByInstituicaoAndIdadeAndEspecie(
            Instituicao instituicao,
            Integer idade,
            String especie
    );

    public List<Pet> findByInstituicaoAndIdadeAndGenero(
            Instituicao instituicao,
            Integer idade,
            String genero
    );

    public List<Pet> findByIdadeAndGeneroAndEspecie(
            Integer idade,
            String genero,
            String especie
    );

    public List<Pet> findByInstituicaoAndIdade(
            Instituicao instituicao,
            Integer idade
    );

    public List<Pet> findByInstituicaoAndEspecie(
            Instituicao instituicao,
            String especie
    );

    public List<Pet> findByInstituicaoAndGenero(
            Instituicao instituicao,
            String genero
    );

    public List<Pet> findByIdadeAndGenero(
            Integer idade,
            String genero
    );

    public List<Pet> findByIdadeAndEspecie(
            Integer idade,
            String especie
    );

    public List<Pet> findByGeneroAndEspecie(
            String genero,
            String especie
    );

    public List<Pet> findByIdade(
            Integer idade
    );

    public List<Pet> findByEspecie(
            String especie
    );

    public List<Pet> findByGenero(
            String genero
    );
}