package com.gadotti.criatorio.dto;

import lombok.Data;

@Data
public class ArvoreGenealogicaDTO {
	private String pai = null;
	private String mae = null;
	private String nonoPai = null;
	private String nonaPai = null;
	private String nonoMae = null;
	private String nonaMae = null;
	private String bisnonoNonoPai= null;
	private String bisnonoNonaPai = null;
	private String bisnonaNonoPai = null;
	private String bisnonaNonaPai = null;
	private String bisnonoNonoMae = null;
	private String bisnonoNonaMae = null;
	private String bisnonaNonoMae = null;
	private String bisnonaNonaMae = null;
}
