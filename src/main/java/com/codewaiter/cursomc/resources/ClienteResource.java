package com.codewaiter.cursomc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewaiter.cursomc.domain.Cliente;
import com.codewaiter.cursomc.services.ClienteService;

@RestController
@RequestMapping(value="/clientes")
public class ClienteResource {
	
	@Autowired
	private ClienteService clienteService;

	@GetMapping(value="/{id}")
	public ResponseEntity<?> listar(@PathVariable Integer id) {
		
		Cliente cliente = clienteService.buscar(id);
		
		return ResponseEntity.ok().body(cliente);
	}
	
}
