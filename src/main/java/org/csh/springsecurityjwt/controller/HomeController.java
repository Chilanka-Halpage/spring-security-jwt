package org.csh.springsecurityjwt.controller;

import org.csh.springsecurityjwt.model.AuthenticationRequest;
import org.csh.springsecurityjwt.service.JwtAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
	@Autowired
	JwtAuthService jwtAuthService; 

	@RequestMapping("/hello")
	public String hellWorld() {
		return "Hello World";
	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {
		return jwtAuthService.createAuthenticationToken(authenticationRequest);
	}
}
