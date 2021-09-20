package com.codewaiter.cursomc.resources;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewaiter.cursomc.security.JWTUtil;
import com.codewaiter.cursomc.security.User;
import com.codewaiter.cursomc.services.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

	@Autowired
	JWTUtil jwtUtil;
	
	@PostMapping(value = "/refresh_token")
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		User user = UserService.authenticatedUser();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Bearer " + token);
		return ResponseEntity.noContent().build();
	}

}
