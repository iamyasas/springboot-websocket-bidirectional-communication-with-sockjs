package com.iamyasas.springbootjwtauthdemo.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.Jwts;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	
	private String[] allowedHeaders;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
		this.allowedHeaders = new String[] {HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE};
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		if (request.getMethod().equals(HttpMethod.OPTIONS.toString())) {
			response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeader(HttpHeaders.ORIGIN));
			response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, String.join(",", allowedHeaders));
			response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, String.valueOf(Boolean.TRUE));
			return;
		}
		
		Cookie tokenCookie = WebUtils.getCookie(request, SecurityConstants.COOKIE_NAME);
		if (tokenCookie == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			//chain.doFilter(request, response);
			return;
		}
		
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = getAuthenticationToken(request);
		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		chain.doFilter(request, response);
	}
	
	private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request) {
		Cookie tokenCookie = WebUtils.getCookie(request, SecurityConstants.COOKIE_NAME);
		String username = Jwts.parser().setSigningKey(SecurityConstants.getSigningKey())
				.parseClaimsJws(tokenCookie.getValue())
				.getBody()
				.getSubject();
		
		return new UsernamePasswordAuthenticationToken(username, null, null);
	}
}
