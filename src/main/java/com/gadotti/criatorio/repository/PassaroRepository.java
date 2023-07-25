package com.gadotti.criatorio.repository;
import java.util.*;
import com.gadotti.criatorio.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassaroRepository extends JpaRepository<Passaro, Long>{
	List<Passaro> findAllBySexoAndEspecie(Sexo sexo, Especie especie);
	Optional<Passaro> findByNome(String nome);
}
