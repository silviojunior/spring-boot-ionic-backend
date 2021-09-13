package com.codewaiter.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.codewaiter.cursomc.domain.Cliente;
import com.codewaiter.cursomc.dto.ClienteDTO;
import com.codewaiter.cursomc.repositories.ClienteRepository;
import com.codewaiter.cursomc.services.exceptions.DataIntegrityException;
import com.codewaiter.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	ClienteRepository clienteRepository;
	
	public Cliente find(Integer id){
		
		Optional<Cliente> cliente = clienteRepository.findById(id);
		
		return cliente.orElseThrow(() -> {
			throw new ObjectNotFoundException("Cliente não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName());
		});
	}
	
	public Cliente fromDTO(ClienteDTO clienteDTO) {
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), null, null);
	}
	
	public Cliente update(Cliente cliente) {
		
		Cliente novoCliente = find(cliente.getId());
		updateData(cliente, novoCliente);
		
		return clienteRepository.save(novoCliente);
	}

	private void updateData(Cliente cliente, Cliente novoCliente) {
		novoCliente.setNome(cliente.getNome());
		novoCliente.setEmail(cliente.getEmail());
	}

	public void delete(Integer id) {
		find(id);
		try {
			clienteRepository.deleteById(id);
		}catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há entidades relacionadas!");
		}
	}

	public List<Cliente> findAll() {
		return clienteRepository.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return clienteRepository.findAll(pageRequest);
	}
	
}
