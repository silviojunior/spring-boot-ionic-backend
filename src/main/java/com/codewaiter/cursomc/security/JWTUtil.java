package com.codewaiter.cursomc.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.expiration}")
	private Long jwtExpiration;

	public String generateToken(String login) {
		return Jwts.builder().setSubject(login).setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
				.signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes()).compact();
	}

	public boolean tokenValido(String token) {

		Claims claims = getClaims(token);
		if (claims != null) {
			String username = claims.getSubject();
			Date expirationDate = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());
			if (username != null & expirationDate != null && now.before(expirationDate)) {
				return true;
			}
		}

		return false;
	}

	private Claims getClaims(String token) {

		try {
			return Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			return null;
		}

	}

	public String getUsername(String token) {

		Claims claims = getClaims(token);
		if (claims != null) {
			return claims.getSubject();
		}
		return null;
	}

}
