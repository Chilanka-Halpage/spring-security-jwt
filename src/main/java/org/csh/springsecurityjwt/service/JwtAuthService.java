package org.csh.springsecurityjwt.service;

import org.csh.springsecurityjwt.model.AuthenticationRequest;
import org.springframework.http.ResponseEntity;

public interface JwtAuthService {
	public ResponseEntity<?> createAuthenticationToken(AuthenticationRequest authenticationRequest)
			throws Exception;
}
