package com.iamyasas.springbootjwtauthdemo.security;

import javax.crypto.SecretKey;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class SecurityConstants {
	public static final String HEADER_PREFIX = "Basic ";
	public static final String BASIC_AUTHENTICATION_CHALLENGE = "Basic realm=\"ncell.com\"";
	public static final String COOKIE_NAME = "token";
	public static final long EXPIRATOIN_TIME = 864_000_000L;
	
	private static SecretKey secretKey;
	
	public static SecretKey getSigningKey() {
		if (secretKey == null) {
			secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		}
		return secretKey; 
	}
}
