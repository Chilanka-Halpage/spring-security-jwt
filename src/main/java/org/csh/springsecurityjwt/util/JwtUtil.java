package org.csh.springsecurityjwt.util;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class JwtUtil {

	private String secretKey;
	private int expirationTime;

	@Value("${jwt.secret-key}")
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	@Value("${jwt.expiration-time}")
	public void setExpirationTime(int expirationTime) {
		this.expirationTime = expirationTime;
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private boolean isValidRole(String token, UserDetails userDetails) {
		Claims claims = extractAllClaims(token);
		String role = claims.get("Role", String.class).toUpperCase();
		return userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + role));
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		Collection<? extends GrantedAuthority> role = userDetails.getAuthorities();
		if (role.contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
			claims.put("Role", "Admin");
		else if (role.contains(new SimpleGrantedAuthority("ROLE_CLIENT")))
			claims.put("Role", "Client");
		else if (role.contains(new SimpleGrantedAuthority("ROLE_ACCOUNTANT")))
			claims.put("Role", "Accountant");
		return createToken(claims, userDetails.getUsername());
	}

	private String createToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expirationTime))// Token expires in 1 minute
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && isValidRole(token, userDetails)
				&& !isTokenExpired(token));
	}
}
