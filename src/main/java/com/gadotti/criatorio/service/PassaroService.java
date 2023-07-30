package com.gadotti.criatorio.service;

import java.io.FileNotFoundException;
import java.util.*;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.gadotti.criatorio.dto.*;
import com.gadotti.criatorio.model.*;
import com.gadotti.criatorio.repository.*;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.*;

@Service
public class PassaroService {

	@Autowired
	private final PassaroRepository passaroRepository;
	private final EspecieRepository especieRepository;
	@Autowired
	private Environment env;

	PassaroService(PassaroRepository passaroRepository, EspecieRepository especieRepository, Environment env) {
		this.passaroRepository = passaroRepository;
		this.especieRepository = especieRepository;
		this.env = env;
	}

	public PassaroDTO getPassaroById(Long id) {
		Optional<Passaro> optionalPassaro = passaroRepository.findById(id);
		Passaro passaro;
		if (optionalPassaro.isPresent()) {
			passaro = optionalPassaro.get();
		} else {
			passaro = null;
		}
		PassaroDTO passaroDTO = passaro != null ? PassaroDTO.fromObject(passaro) : null;

		if (passaroDTO != null) {
			return passaroDTO;
		} else {
			throw new RuntimeException("Pássaro não encontrado! ");
		}
	}

	public List<PassaroDTO> getAllPassaro() {

		List<Passaro> passaros = passaroRepository.findAll();
		List<PassaroDTO> listDTO = new ArrayList<PassaroDTO>();
		for (Passaro p : passaros) {
			listDTO.add(PassaroDTO.fromObject(p));
		}
		if (listDTO.size() > 0) {
			return listDTO;
		} else {
			throw new RuntimeException("Nenhum pássaro encontrado");
		}
	}

	public List<PassaroDTO> getAllPassaroBySexoAndEspecieId(String sexo, Long especieId) {
		sexo = sexo.toUpperCase();
		Especie especie = null;
		if (especieRepository.findById(especieId).isPresent()) {
			especie = especieRepository.findById(especieId).get();
		}
		if (especie == null)
			throw new RuntimeException("Espécie não encontrada");
		List<Passaro> passaros = passaroRepository.findAllBySexoAndEspecie(Sexo.valueOf(sexo), especie);
		List<PassaroDTO> listDTO = new ArrayList<PassaroDTO>();
		for (Passaro p : passaros) {
			listDTO.add(PassaroDTO.fromObject(p));
		}
		if (listDTO.size() > 0) {
			return listDTO;
		} else {
			throw new RuntimeException("Nenhum pássaro " + sexo + " encontrado");
		}
	}

	public List<PassaroDTO> getAllPassaroBySexoAndEspecieNome(String sexo, String especieNome) {
		sexo = sexo.toUpperCase();
		Especie especie = especieRepository.findByNome(especieNome);
		if (especie == null)
			throw new RuntimeException("Espécie não encontrada");
		List<Passaro> passaros = passaroRepository.findAllBySexoAndEspecie(Sexo.valueOf(sexo), especie);
		List<PassaroDTO> listDTO = new ArrayList<PassaroDTO>();
		for (Passaro p : passaros) {
			listDTO.add(PassaroDTO.fromObject(p));
		}
		if (listDTO.size() > 0) {
			return listDTO;
		} else {
			throw new RuntimeException("Nenhum pássaro " + sexo + " encontrado");
		}
	}

	public void setPassaro(PassaroDTO passaroDTO) throws Exception {
		Passaro passaro = passaroDTO.toObject();
		Especie especie = null;
		if (passaroDTO.getEspecie() != null) {
			especie = especieRepository.findByNome(passaroDTO.getEspecie().toUpperCase());
		}
		try {
			if (especie != null) {
				passaro.setEspecie(especie);
			} else {
				Especie newEspecie = new Especie(passaroDTO.getEspecie().toUpperCase());
				passaro.setEspecie(newEspecie);
			}
			if (passaroDTO.getNome() != null) {
				passaro.setNome(passaroDTO.getNome().toUpperCase());
			}
			passaro.setHoraInclusao(LocalDateTime.now());
			Optional<Passaro> pai = passaroRepository.findByNome(passaroDTO.getPai());
			Optional<Passaro> mae = passaroRepository.findByNome(passaroDTO.getMae());
			if (pai.isPresent()) {
				passaro.setPai(pai.get());
			}
			if (mae.isPresent()) {
				passaro.setMae(mae.get());
			}
			System.out.println("registo" + passaro.getDataRegistro());
			System.out.println("nascimento" + passaro.getDataNascimento());
			if (passaro.getDataNascimento() != null && passaro.getDataRegistro() == null) {
				passaro.setDataRegistro(passaro.getDataNascimento());
			}
			if (passaro.getDataRegistro() != null && passaro.getDataNascimento() == null) {
				passaro.setDataNascimento(passaro.getDataRegistro());
			}
			passaroRepository.save(passaro);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public void deletePassaroById(Long id) throws IllegalArgumentException,PSQLException {
		passaroRepository.deleteById(id);
	}

	private Passaro buscaPassaro(Long id) {
		Optional<Passaro> op = passaroRepository.findById(id);
		Passaro passaro = null;
		if (op.isPresent()) {
			passaro = op.get();
		}
		return passaro;
	}

	public ArvoreGenealogicaDTO getArvoreGenealogica(Long passaroId) {
		System.out.println("id: " + passaroId);
		Optional<Passaro> op = passaroRepository.findById(passaroId);
		Passaro passaro = null;
		ArvoreGenealogicaDTO arvore = new ArvoreGenealogicaDTO();
		if (op.isPresent()) {
			passaro = op.get();
		}
		if (passaro != null) {
			if (passaro.getPai() != null) {
				Passaro pai = passaro.getPai();
				arvore.setPai(buscaPassaro(pai.getId()).getNome());
				if (pai.getPai() != null) {
					Passaro nonoPai = pai.getPai();
					arvore.setNonoPai(buscaPassaro(nonoPai.getId()).getNome());
					if (nonoPai.getPai() != null) {
						Passaro bisnonoNonoPai = nonoPai.getPai();
						arvore.setBisnonoNonoPai(buscaPassaro(bisnonoNonoPai.getId()).getNome());
					}
					if (nonoPai.getMae() != null) {
						Passaro bisnonaNonoPai = nonoPai.getMae();
						arvore.setBisnonaNonoPai(buscaPassaro(bisnonaNonoPai.getId()).getNome());
					}

				}
				if (pai.getMae() != null) {
					Passaro nonaPai = pai.getMae();
					arvore.setNonaPai(buscaPassaro(nonaPai.getId()).getNome());
					if (nonaPai.getPai() != null) {
						Passaro bisnonoNonaPai = nonaPai.getPai();
						arvore.setBisnonoNonaPai(buscaPassaro(bisnonoNonaPai.getId()).getNome());
					}

					if (nonaPai.getMae() != null) {
						Passaro bisnonaNonaPai = nonaPai.getMae();
						arvore.setBisnonaNonaPai(buscaPassaro(bisnonaNonaPai.getId()).getNome());

					}
				}
			}
			if (passaro.getMae() != null) {
				Passaro mae = passaro.getMae();
				arvore.setMae(buscaPassaro(mae.getId()).getNome());
				if (mae.getPai() != null) {
					Passaro nonoMae = mae.getPai();
					arvore.setNonoMae(buscaPassaro(nonoMae.getId()).getNome());
					if (nonoMae.getPai() != null) {
						Passaro bisnonoNonoMae = nonoMae.getPai();
						arvore.setBisnonoNonoMae(buscaPassaro(bisnonoNonoMae.getId()).getNome());
					}
					if (nonoMae.getMae() != null) {
						Passaro bisnonaNonoMae = nonoMae.getMae();
						arvore.setBisnonaNonoMae(buscaPassaro(bisnonaNonoMae.getId()).getNome());
					}
				}
				if (mae.getMae() != null) {
					Passaro nonaMae = mae.getMae();
					arvore.setNonaMae(buscaPassaro(nonaMae.getId()).getNome());
					if (nonaMae.getPai() != null) {
						Passaro bisnonoNonaMae = nonaMae.getPai();
						arvore.setBisnonoNonaMae(buscaPassaro(bisnonoNonaMae.getId()).getNome());
					}
					if (nonaMae.getMae() != null) {
						Passaro bisnonaNonaMae = nonaMae.getMae();
						arvore.setBisnonaNonaMae(buscaPassaro(bisnonaNonaMae.getId()).getNome());
					}
				}
			}

		}
		return arvore;
	}

	public String printCollectionPassaros(List<PassaroDTO> listPassaroDTO) {
		com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4.rotate());
		String reportsFolder = getReportsFolder();		
		String fileName = "/relatorioPassaros" + LocalDateTime.now().toString().replaceAll("-|:|\\.", "_") + ".pdf";
		try {
			PdfWriter.getInstance(document, new FileOutputStream(reportsFolder + fileName));
			document.open();

			Field[] campos = listPassaroDTO.get(0).getClass().getDeclaredFields();

			PdfPTable table = new PdfPTable(campos.length - 1);

			PdfPCell cell = new PdfPCell();

			cell.setPhrase(new Phrase("CRIATÓRIO BOA ESTAÇÃO"));
			cell.setColspan(campos.length);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			cell.setPhrase(new Phrase("RELATÓRIO DE PASSARIFORMES"));
			cell.setColspan(campos.length - 1);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			for (Field field : campos) {
				if (field.getName() == "id")
					continue;
				DisplayName displayName = field.getAnnotation(DisplayName.class);
				String content = displayName == null ? field.getName() : displayName.value();
				table.addCell(content);
			}

			for (PassaroDTO dto : listPassaroDTO) {
				for (Field field : campos) {
					if (field.getName() == "id")
						continue;
					field.setAccessible(true);
					try {
						Object value = field.get(dto);
						String content = "";

						if (value instanceof LocalDate) {
							LocalDate date = (LocalDate) value;
							value = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
						}
						if (value != null) {
							if (field.getName() == "pai" || field.getName() == "mae") {
								Optional<Passaro> optional = passaroRepository.findByNome(value.toString());
								Passaro p = null;
								if (optional.isPresent()) {
									p = optional.get();
									value = p.getNome();
								} else {
									value = "";
								}
							}
							if (field.getType() == Boolean.class) {
								value = value.toString() == "true" ? "S" : "N";
							}
							content = value.toString();
						}
						cell.setColspan(1);
						cell.setPhrase(new Phrase(content, new Font(FontFamily.HELVETICA, 8)));
						table.addCell(cell);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			document.add(table);
			document.close();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (DocumentException de) {
			de.printStackTrace();
		}
		return fileName;
	}
	
	private String getReportsFolder() {
		String[] properties = env.getProperty("spring.web.resources.static-locations", String[].class);
		for (String property : properties) {
			if (property.contains("file:")){
				return property.replace("file:", "");
			}
		}
		return "";
	}
}
