package com.codewaiter.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codewaiter.cursomc.domain.Cidade;
import com.codewaiter.cursomc.domain.Cliente;
import com.codewaiter.cursomc.domain.Endereco;
import com.codewaiter.cursomc.domain.enums.Perfil;
import com.codewaiter.cursomc.domain.enums.TipoCliente;
import com.codewaiter.cursomc.dto.ClienteDTO;
import com.codewaiter.cursomc.dto.NovoClienteDTO;
import com.codewaiter.cursomc.repositories.ClienteRepository;
import com.codewaiter.cursomc.repositories.EnderecoRepository;
import com.codewaiter.cursomc.security.User;
import com.codewaiter.cursomc.services.exceptions.AuthorizationException;
import com.codewaiter.cursomc.services.exceptions.DataIntegrityException;
import com.codewaiter.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public Cliente find(Integer id){
		
		User user = UserService.authenticatedUser();
		
		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso Negado!");
		}
		Optional<Cliente> cliente = clienteRepository.findById(id);
		
		return cliente.orElseThrow(() -> {
			throw new ObjectNotFoundException("Cliente não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName());
		});
	}
	
	public Cliente fromDTO(ClienteDTO clienteDTO) {
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(NovoClienteDTO novoClienteDTO) {
		
		Cliente cli = new Cliente(null, novoClienteDTO.getNome(), novoClienteDTO.getEmail(), novoClienteDTO.getCpfOuCnpj(), TipoCliente.toEnum(novoClienteDTO.getTipo()), passwordEncoder.encode(novoClienteDTO.getSenha()));
		Cidade cid = new Cidade(novoClienteDTO.getCidadeId(), null, null);
		Endereco end = new Endereco(null, novoClienteDTO.getLogradouro(), novoClienteDTO.getNumero(), novoClienteDTO.getComplemento(), novoClienteDTO.getBairro(), novoClienteDTO.getCep(), cli, cid);
		
		cli.getEnderecos().add(end);
		cli.getTelefones().add(novoClienteDTO.getTelefone1());
		
		if(novoClienteDTO.getTelefone2() != null)
			cli.getTelefones().add(novoClienteDTO.getTelefone2());
		
		if(novoClienteDTO.getTelefone3() != null)
			cli.getTelefones().add(novoClienteDTO.getTelefone3());

		return cli;
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
			throw new DataIntegrityException("Não é possível excluir porque há pedidos relacionados!");
		}
	}

	public List<Cliente> findAll() {
		return clienteRepository.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return clienteRepository.findAll(pageRequest);
	}

	@Transactional
	public Cliente insert(Cliente cliente) {
		cliente.setId(null);
		cliente = clienteRepository.save(cliente);
		enderecoRepository.saveAll(cliente.getEnderecos());
		
		return cliente;
	}
	
}
