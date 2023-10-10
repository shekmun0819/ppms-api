package com.example.ppms.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.ppms.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private static final String SECRET_KEY = "6B58703273357638792F423F4528472B4B6250655368566D597133743677397A24432646294A404D635166546A576E5A7234753778214125442A472D4B615064";

	public String extractUsername(String token) {

		return extractClaim(token, Claims::getSubject);

	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String generateToken(User user) {
		return generateToken(new HashMap<>(), user);
	}

	public String generateToken(Map<String, Object> extraClaims, User user) {
		return Jwts
				.builder()
				.setClaims(extraClaims)
				.setSubject(user.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // token will be expired in
																							// 24h
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.claim("userInfo", new User(user.getEmail(), user.getName(), user.getRoles(), user.getId()))
				.compact();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		System.out.println(token);
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {

		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {

		return extractClaim(token, Claims::getExpiration);
	}

	public Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token).getBody();
	}

	private Key getSignInKey() {
		// TODO Auto-generated method stub
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
