package com.gadotti.criatorio.dto;

import java.time.LocalDate;

import com.gadotti.criatorio.model.Criatorio;
import com.gadotti.criatorio.model.Especie;
import com.gadotti.criatorio.model.Passaro;
import com.gadotti.criatorio.model.Sexo;

import lombok.Data;

@Data
public class PassaroDTO {
	@DisplayName("ID")
	private Long id;
	@DisplayName("Nome")
	private String nome;
	@DisplayName("Espécie")
	private String especie;
	@DisplayName("Nº Anilha")
	private Long numeroAnilha;
	@DisplayName("Nº Marc.")
	private Long numeroMarcacao;
	@DisplayName("Nasc.")
	private LocalDate dataNascimento;
	@DisplayName("Sexo")
	private String sexo;
	@DisplayName("Pai")
	private String pai;
	@DisplayName("Mãe")
	private String mae;
	@DisplayName("Sexado")
	private Boolean sexado;
	@DisplayName("Criatório")
	private String criatorio;

	public static PassaroDTO fromObject(Passaro p) {
		PassaroDTO dto = new PassaroDTO();
		dto.setId(p.getId());
		dto.setNome(p.getNome());
		if (p.getEspecie() != null) {
			dto.setEspecie(p.getEspecie().getNome());
		}
		dto.setDataNascimento(p.getDataNascimento());
		if (p.getPai() != null) {
			dto.setPai(p.getPai().getNome());
		}
		if (p.getMae() != null) {
			dto.setMae(p.getMae().getNome());
		}
		if (p.getSexo() != null) {
			dto.setSexo(p.getSexo().toString());
		}
		dto.setNumeroAnilha(p.getNumeroAnilha());
		dto.setSexado(p.getSexado());
		dto.setNumeroMarcacao(p.getNumeroMarcacao());
		if (p.getCriatorio() != null) {
			dto.setCriatorio(p.getCriatorio().toString().toUpperCase());
		}
		return dto;
	}

	public Passaro toObject() {
		Passaro p = new Passaro();
		p.setId(this.getId());
		p.setNome(this.getNome());
		p.setEspecie(new Especie(this.getEspecie()));
		p.setDataNascimento(this.getDataNascimento());
		p.setSexo(Sexo.valueOf(getSexo()));
		p.setNumeroAnilha(this.getNumeroAnilha());
		p.setSexado(this.getSexado());
		p.setNumeroMarcacao(this.getNumeroMarcacao());
		if (this.criatorio != null)
			p.setCriatorio(Criatorio.valueOf(this.getCriatorio().toUpperCase()));
		return p;
	}

}
