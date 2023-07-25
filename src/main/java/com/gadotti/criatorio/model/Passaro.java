package com.gadotti.criatorio.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.*;


@Entity
@Data
public class Passaro {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String nome;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Especie especie;
	private LocalDate dataNascimento;
	@ManyToOne
	private Passaro pai;
	@ManyToOne
	private Passaro mae;
	@Enumerated(value=EnumType.STRING)
	private Sexo sexo;
	@Column(unique=true)
	private Long numeroAnilha;
	private Boolean sexado;
	private Long numeroMarcacao;
	private LocalDateTime horaInclusao = LocalDateTime.now();
	
	@Enumerated(value=EnumType.STRING)
	private Criatorio criatorio = Criatorio.BOA;
	
	public Passaro() {}
	public Passaro(Long id){	
	}
}



