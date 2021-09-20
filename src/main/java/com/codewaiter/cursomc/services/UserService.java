package com.codewaiter.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.codewaiter.cursomc.security.User;

public class UserService {
	
	public static User authenticatedUser() {
		try {
			return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}
}
