package com.gadotti.criatorio.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gadotti.criatorio.model.*;

public interface UsuarioRepository extends JpaRepository<Usuario, String>{
	Usuario findByUsernameAndPassword(String username, String password);
	Usuario findByToken(UUID token);
}
