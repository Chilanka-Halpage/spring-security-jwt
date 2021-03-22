package org.csh.springsecurityjwt.service.impl;

import org.csh.springsecurityjwt.model.AuthenticationRequest;
import org.csh.springsecurityjwt.model.AuthenticationResponse;
import org.csh.springsecurityjwt.security.MyUserDetailService;
import org.csh.springsecurityjwt.service.JwtAuthService;
import org.csh.springsecurityjwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthServiceImpl implements JwtAuthService {

	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	MyUserDetailService userDetailsService;
	@Autowired
	JwtUtil jwtTokenUtil;
	@Override
	public ResponseEntity<?> createAuthenticationToken(AuthenticationRequest authenticationRequest) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}


		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtTokenUtil.generateToken(userDetails);
		
		final String role = userDetails.getAuthorities().stream().map(r -> r.getAuthority()).findFirst().orElse("NULL");

		return ResponseEntity.ok(new AuthenticationResponse(userDetails.getUsername(),role,jwt));

	}

}
