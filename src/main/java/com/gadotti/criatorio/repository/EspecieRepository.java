package com.gadotti.criatorio.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gadotti.criatorio.model.Especie;

public interface EspecieRepository extends JpaRepository<Especie, Long>{
	Especie findByNome(String nome);
}
