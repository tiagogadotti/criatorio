package com.gadotti.criatorio.api;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gadotti.criatorio.model.Sexo;
import com.gadotti.criatorio.repository.PassaroRepository;

@RestController
public class TesteAPI {

	@Autowired
	private final PassaroRepository r;
	
	TesteAPI(PassaroRepository r) {
		this.r = r;
	}
	@PostMapping(value="/api/passaros/test")
	public void test() {
		System.out.print("printCollectionPassaros" + LocalDateTime.now().toString().replaceAll("-|:|\\.", "_") + ".pdf");
	}
}
