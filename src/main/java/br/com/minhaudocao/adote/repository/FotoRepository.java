package br.com.minhaudocao.adote.repository;

import br.com.minhaudocao.adote.entity.Foto;
import br.com.minhaudocao.adote.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface FotoRepository extends JpaRepository<Foto, Long> {

    public ArrayList<String> findByPet(Pet pet);
}
