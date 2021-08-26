package com.codewaiter.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codewaiter.cursomc.domain.Pedido;
import com.codewaiter.cursomc.repositories.PedidoRepository;
import com.codewaiter.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	PedidoRepository pedidoRepository;
	
	public Pedido buscar(Integer id) {
		
		Optional<Pedido> pedido = pedidoRepository.findById(id);
		
		return pedido.orElseThrow(() -> {
			throw new ObjectNotFoundException("Pedido Não Encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName());
		});
	}
}
