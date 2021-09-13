package com.codewaiter.cursomc.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.codewaiter.cursomc.domain.Cliente;
import com.codewaiter.cursomc.dto.ClienteDTO;
import com.codewaiter.cursomc.dto.NovoClienteDTO;
import com.codewaiter.cursomc.services.ClienteService;

@RestController
@RequestMapping(value="/clientes")
public class ClienteResource {
	
	@Autowired
	private ClienteService clienteService;

	@GetMapping(value="/{id}")
	public ResponseEntity<Cliente> listar(@PathVariable Integer id) {
		
		Cliente cliente = clienteService.find(id);
		
		return ResponseEntity.ok().body(cliente);
	}
	
	@GetMapping
	public ResponseEntity<List<ClienteDTO>> findAll() {
		
		List<Cliente> clientes = clienteService.findAll();
		List<ClienteDTO> listaClientes = new ArrayList<ClienteDTO>();
		
		for(Cliente cli : clientes) {
			listaClientes.add(new ClienteDTO(cli));
		}
		/**
		 * Outra abordagem:
		 * listaClientes = clientes.stream().map(cliente -> new ClienteDTO(cliente)).collect(Collectors.toList());
		 */
		return ResponseEntity.ok().body(listaClientes);
	}
	
	@GetMapping(value="/page")
	public ResponseEntity<Page<ClienteDTO>> findPage(
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction
			) {
		
		Page<Cliente> paginasClientes = clienteService.findPage(page, linesPerPage, orderBy, direction);
		Page<ClienteDTO> listaClientes = paginasClientes.map(cliente -> new ClienteDTO(cliente)); 

		return ResponseEntity.ok().body(listaClientes);
	}
	
	@PostMapping
	public ResponseEntity<Void> insert(@Valid @RequestBody NovoClienteDTO novoClienteDTO){
		
		Cliente cliente = clienteService.fromDTO(novoClienteDTO);
		cliente = clienteService.insert(cliente);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cliente.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping(value="/{id}")
	public ResponseEntity<Void> update(@PathVariable Integer id, @Valid @RequestBody ClienteDTO clienteDTO){
		
		clienteDTO.setId(id);
		Cliente cliente = clienteService.fromDTO(clienteDTO);
		cliente = clienteService.update(cliente);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		
		clienteService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
}
