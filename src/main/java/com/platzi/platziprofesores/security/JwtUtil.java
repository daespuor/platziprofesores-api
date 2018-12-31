package com.platzi.platziprofesores.security;

import java.util.Collections;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {
	
	private static String secretKey="secret";
	public static void addAuthentication(HttpServletResponse res, String username) {
		String token=Jwts.builder()
						 .setSubject(username)
						 .setExpiration(new Date(System.currentTimeMillis() + 300000))
						 .signWith(SignatureAlgorithm.HS512, secretKey)
						 .compact();
		System.out.println(token);
		res.setHeader("Authorization", "Bearer "+token);
	}
	
	public static Authentication getAuthentication(HttpServletRequest req) {
		String token= req.getHeader("Authorization");
		if(token!=null) {
			String username=Jwts.parser()
								.setSigningKey(secretKey)
								.parseClaimsJws(token.replace("Bearer", ""))
								.getBody()
								.getSubject();
			return username!=null?
					new UsernamePasswordAuthenticationToken(username, null,Collections.emptyList())
					: null;
		}else {
			return null;
		}
	}
}
