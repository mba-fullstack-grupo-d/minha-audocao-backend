package br.com.minhaudocao.adote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.minhaudocao.adote.entity.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {

}