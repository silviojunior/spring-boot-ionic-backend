package com.codewaiter.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.codewaiter.cursomc.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido pedido);
	
	void sendEmail(SimpleMailMessage msg);
}