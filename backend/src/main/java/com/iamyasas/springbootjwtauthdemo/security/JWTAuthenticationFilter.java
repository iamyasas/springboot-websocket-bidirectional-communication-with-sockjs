package com.iamyasas.springbootjwtauthdemo.security;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.iamyasas.springbootjwtauthdemo.security.SecurityConstants;

import io.jsonwebtoken.Jwts;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header == null || !header.startsWith(SecurityConstants.HEADER_PREFIX)) {
			throw new AuthenticationCredentialsNotFoundException("No Authentication or Wrong header prefix");
		}
		String token = header.substring(SecurityConstants.HEADER_PREFIX.length());
		String decodedToken = new String(Base64.getDecoder().decode(token));
		String[] decodedTokenParts = decodedToken.split(":");
		return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(decodedTokenParts[0], decodedTokenParts[1]));
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		ZonedDateTime expirationTimeUTC = ZonedDateTime.now(ZoneOffset.UTC).plus(SecurityConstants.EXPIRATOIN_TIME,ChronoUnit.MILLIS);
		String username = ((User)authResult.getPrincipal()).getUsername();
		String token = Jwts.builder()
				.setSubject(username)
				.setExpiration(Date.from(expirationTimeUTC.toInstant()))
				.signWith(SecurityConstants.getSigningKey())
				.compact();
		response.getWriter().write(token);
		Cookie tokenCookie = new Cookie(SecurityConstants.COOKIE_NAME, token);
		tokenCookie.setHttpOnly(true);
		response.addCookie(tokenCookie);
		
		response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeader(HttpHeaders.ORIGIN));
		response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, String.valueOf(Boolean.TRUE));
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setHeader(HttpHeaders.WWW_AUTHENTICATE, SecurityConstants.BASIC_AUTHENTICATION_CHALLENGE);
	}
	
}
