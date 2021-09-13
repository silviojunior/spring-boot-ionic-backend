package com.codewaiter.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.codewaiter.cursomc.domain.enums.TipoCliente;
import com.codewaiter.cursomc.dto.NovoClienteDTO;
import com.codewaiter.cursomc.resources.exception.FieldMessage;
import com.codewaiter.cursomc.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, NovoClienteDTO>{

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
		
		for(FieldMessage e: list)
		{
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
		}
		return list.isEmpty();
	}

}
