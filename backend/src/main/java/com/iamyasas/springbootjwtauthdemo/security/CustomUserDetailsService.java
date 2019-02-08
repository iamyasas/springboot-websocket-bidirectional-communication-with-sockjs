package com.iamyasas.springbootjwtauthdemo.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.iamyasas.springbootjwtauthdemo.models.ApplicationUser;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		ApplicationUser applicationUser = loadApplicationUserByName(username);
		return User.withUsername(applicationUser.getUsername()).password(applicationUser.getPassword()).roles("USER").build();

	}
	
	public ApplicationUser loadApplicationUserByName(String username) {
		
		return new ApplicationUser(username, "{noop}pass");
	}
}
