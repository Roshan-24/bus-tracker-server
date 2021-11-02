package edu.nitt.delta.bustracker.utils;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTokenUtil {

	@Value("${jwt.secret}")
	private String secret;

	public String generateToken(String mobileNumber) {
		return Jwts.builder()
			.setClaims(Jwts.claims().setSubject(mobileNumber))
			.signWith(SignatureAlgorithm.HS512, secret)
			.compact();
	}

	public String getMobileNumber(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
	}
}