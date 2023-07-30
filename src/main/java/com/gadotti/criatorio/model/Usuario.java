package com.gadotti.criatorio.model;
import jakarta.persistence.*;
import java.util.*;

import lombok.Data;

import java.time.*;

@Data
@Entity
public class Usuario {
	@Id
	private String username;
	private String password;
	private UUID token;
	@Column(name = "token_expiration")	
	private LocalDateTime tokenExpiration;
}
