package com.gadotti.criatorio.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gadotti.criatorio.service.PassaroService;

@RestController
public class TestApi {

	@Autowired
	private final PassaroService passaroService;
	
	TestApi(PassaroService passaroService){
		this.passaroService = passaroService;
	}
	
	@GetMapping("/api/test")
	public void test() {
	}
}
