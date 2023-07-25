package com.gadotti.criatorio.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gadotti.criatorio.dto.*;
import com.gadotti.criatorio.model.Especie;
import com.gadotti.criatorio.repository.EspecieRepository;

@RestController
@RequestMapping("/api/passaros")
public class EspecieApi {
	@Autowired
	private final EspecieRepository especieRepository;
	
	EspecieApi(EspecieRepository especieRepository){
		this.especieRepository = especieRepository;
	}
	
	
	@GetMapping("/getAllEspecie")
	public ResponseEntity<?> getAllEspecie() {
		List<Especie> especies = especieRepository.findAll();
		
		List<EspecieDTO> listDTO = new ArrayList<EspecieDTO>();
		
		for(Especie e : especies) {
			listDTO.add(new EspecieDTO().fromObject(e));
		}	
		
		if (listDTO.size()> 0) {
			return new ResponseEntity<List<EspecieDTO>>(listDTO, HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("Nenhuma espécie cadastrada", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value="/setEspecie", produces="text/plain")
	public ResponseEntity<?> setEspecie(@RequestBody EspecieDTO especieDTO){
		Especie especie = especieRepository.findByNome(especieDTO.getNome().toUpperCase());
		try {
			if (especie == null){
				Especie newEspecie = new Especie(especieDTO.getNome().toUpperCase());
				especieRepository.save(newEspecie);
				return new ResponseEntity<String>("Espécie salva com sucesso", HttpStatus.OK);
			}else {
				return new ResponseEntity<String>("Espécie já existe", HttpStatus.CONFLICT);
			}
				
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível salvar: \n" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
}
