package com.gadotti.criatorio.dto;

import lombok.Data;
import com.gadotti.criatorio.model.*;
@Data
public class EspecieDTO {
	private String nome;
	private Long id;
	
	
	public EspecieDTO fromObject(Especie e) {
		EspecieDTO dto = new EspecieDTO();
		dto.setNome(e.getNome());
		dto.setId(e.getId());
		return dto;
	}
	
	public Especie toObject() {
		Especie especie = new Especie();
		especie.setNome(this.getNome());
		especie.setId(this.getId());
		return especie;
	}
}
