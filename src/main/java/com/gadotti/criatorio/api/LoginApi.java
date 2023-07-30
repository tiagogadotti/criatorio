package com.gadotti.criatorio.api;

import java.time.LocalDateTime;
import java.util.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gadotti.criatorio.repository.UsuarioRepository;
import com.gadotti.criatorio.model.*;
@RestController
@RequestMapping("/api")
public class LoginApi {

	@Autowired
	private final UsuarioRepository usuarioRepository;
	
	private static final Logger log = LogManager.getLogger(LoginApi.class);
	
	LoginApi(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
		log.info("username: {}", username);
		log.info("password: {}", password);
		
		Usuario usuario = usuarioRepository.findByUsernameAndPassword(username, password);		
		if( usuario != null) {
			System.out.println("usuario:" +usuario);
			UUID newToken = UUID.randomUUID();
			usuario.setToken(newToken);
			LocalDateTime tokenExpiration = LocalDateTime.now().plusSeconds(600); 
		
			usuario.setTokenExpiration(tokenExpiration);
			log.info("usuario: {}", usuario);
			usuarioRepository.save(usuario);
			Map<String, String> json = new HashMap<String, String>();
			json.put("token", newToken.toString());
			
			return new ResponseEntity<Map<String, String>>(json, HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("Erro ao logar", HttpStatus.UNAUTHORIZED);
	}
	@PostMapping("checkToken")
	public ResponseEntity<Boolean>checkToken(String token) {
	
		Usuario usuario = usuarioRepository.findByToken(UUID.fromString(token));
		if (usuario == null) { 
			return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
		}
		if(usuario.getTokenExpiration().isBefore(LocalDateTime.now())) {
			return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
		}
		usuario.setTokenExpiration(LocalDateTime.now().plusSeconds(600));
		usuarioRepository.save(usuario);
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
	
}
