package com.codewaiter.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.codewaiter.cursomc.domain.Cliente;
import com.codewaiter.cursomc.domain.enums.TipoCliente;
import com.codewaiter.cursomc.dto.NovoClienteDTO;
import com.codewaiter.cursomc.repositories.ClienteRepository;
import com.codewaiter.cursomc.resources.exception.FieldMessage;
import com.codewaiter.cursomc.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, NovoClienteDTO>{

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	public void initialize(ClienteInsert clienteInsert) {
		
	}
	
	@Override
	public boolean isValid(NovoClienteDTO novoClienteDTO, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();
		
		if(novoClienteDTO.getTipo().equals(TipoCliente.PESSOA_FISICA.getCod()) && !BR.isValidSsn(novoClienteDTO.getCpfOuCnpj())){
			list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
		}
		
		if(novoClienteDTO.getTipo().equals(TipoCliente.PESSOA_JURIDICA.getCod()) && !BR.isValidTfn(novoClienteDTO.getCpfOuCnpj())){
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
		}
		
		Cliente cliAux = clienteRepository.findByEmail(novoClienteDTO.getEmail());
		
		if(cliAux != null) {
			list.add(new FieldMessage("email","Email já existente."));
		}
		
		for(FieldMessage e: list)
		{
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
		}
		return list.isEmpty();
	}

}
