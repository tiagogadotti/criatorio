package com.gadotti.criatorio.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Especie {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(unique=true)
	private String nome;
	
	public Especie() {}
	public Especie(String nome) {
		this.nome = nome;
	}
	public Especie(Long id) {
		this.id = id;
	}
}
