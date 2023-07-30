package com.gadotti.criatorio.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.gadotti.criatorio.dto.*;
import com.gadotti.criatorio.service.*;

@RestController
@RequestMapping("/api/passaros")
public class PassaroApi {

	@Autowired
	private final PassaroService passaroService;

	PassaroApi(PassaroService passaroService) {
		this.passaroService = passaroService;
	}

	@GetMapping("/getPassaroById")
	public ResponseEntity<?> getPassaroById(@RequestParam Long id) {
		try {
			PassaroDTO passaroDTO = passaroService.getPassaroById(id);
			return new ResponseEntity<PassaroDTO>(passaroDTO, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Map<String, String>>(Map.of("mensagem", "Erro ao encontrar pássaro"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getAllPassaro")
	public ResponseEntity<?> getAllPassaro() {
		try {
			List<PassaroDTO> listDTO = passaroService.getAllPassaro();
			return new ResponseEntity<List<PassaroDTO>>(listDTO, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Map<String, String>>(Map.of("mensagem", "Nenhum pássaro encontrado"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getAllPassaroBySexoAndEspecieId")
	public ResponseEntity<?> getAllPassaroBySexoAndEspecie(@RequestParam String sexo,
			@RequestParam("especie_id") Long especieId) {
		try {
			List<PassaroDTO> listDTO = passaroService.getAllPassaroBySexoAndEspecieId(sexo, especieId);
			return new ResponseEntity<List<PassaroDTO>>(listDTO, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, String>>(
					Map.of("mensagem", "Nenhum pássaro do sexo " + sexo + " encontrado"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getAllPassaroBySexoAndEspecieNome")
	public ResponseEntity<?> getAllPassaroBySexoAndEspecieNome(@RequestParam String sexo,
			@RequestParam("especie_nome") String especieNome) {
		try {
			List<PassaroDTO> listDTO = passaroService.getAllPassaroBySexoAndEspecieNome(sexo, especieNome);
			return new ResponseEntity<List<PassaroDTO>>(listDTO, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, String>>(
					Map.of("mensagem", "Nenhum pássaro do sexo " + sexo + " encontrado"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getArvoreGenealogica")
	public ResponseEntity<?> getArvoreGenealogica(@RequestParam("id") Long passaroId) {

		ArvoreGenealogicaDTO arvore = passaroService.getArvoreGenealogica(passaroId);
		return new ResponseEntity<ArvoreGenealogicaDTO>(arvore, HttpStatus.OK);
	}

	@PostMapping("/setPassaro")
	public ResponseEntity<?> setPassaro(@RequestBody PassaroDTO passaroDTO) {
		if (checkFields(passaroDTO)) {
			passaroDTO = cleanFields(passaroDTO);
		}
		try {
			passaroService.setPassaro(passaroDTO);
			return new ResponseEntity<Map<String, String>>(Map.of("mensagem", "Pássaro salvo com sucesso"),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, String>>(
					Map.of("mensagem", "Não foi possível salvar o pássaro. Contultar logs"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private static boolean checkFields(PassaroDTO dto) {
		if (dto.getNome().isBlank())
			return false;
		return true;
	}

	private static PassaroDTO cleanFields(PassaroDTO dto) {
		dto.setNome(dto.getNome().toUpperCase());
		if (dto.getSexo() != null) {
			dto.setSexo(dto.getSexo().toUpperCase());
		}
		if (dto.getCriatorio() != null) {
			dto.setCriatorio(dto.getCriatorio().toUpperCase());
		}
		return dto;
	}

	@PostMapping("/printCollectionPassaro")
	public ResponseEntity<?> printPassaros(@RequestBody List<Long> listId) {

		System.out.println("Lista: " + listId);

		List<PassaroDTO> listPassaroDTO = new ArrayList<PassaroDTO>();
		PassaroDTO dto = new PassaroDTO();
		for (Long id : listId) {
			dto = passaroService.getPassaroById(id);
			listPassaroDTO.add(dto);
		}

		String link = passaroService.printCollectionPassaros(listPassaroDTO);

		//
		return new ResponseEntity<Map<String, String>>(Map.of("link", link), HttpStatus.OK);
	}

	@DeleteMapping("/deletePassaro")
	public ResponseEntity<?> deletePassaro(@RequestBody Map<String, String> passaroId) {
		try {
			Long id = Long.parseLong(passaroId.get("id"));
			System.out.println("id no server> " + id);
			passaroService.deletePassaroById(id);
			return new ResponseEntity<String>("Deletado com sucesso", HttpStatus.OK);
		} catch (IllegalArgumentException iae) {
			iae.printStackTrace();
			return new ResponseEntity<String>("Problemas ao excluir. Pássaro não encontrado", HttpStatus.CONFLICT);
		} catch (PSQLException psqle) {
			System.out.println("Caiu no catch!");
			psqle.printStackTrace();
			return new ResponseEntity<String>("Problemas ao excluir. Pássaro é referenciado como pai/mãe",
					HttpStatus.CONFLICT);
		}
	}

}
