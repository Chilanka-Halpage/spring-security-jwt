package org.csh.springsecurityjwt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthenticationResponse{
	
	private String username;
	private String role;
	private String jwt;
		
}
