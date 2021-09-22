package com.codewaiter.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codewaiter.cursomc.domain.Cidade;
import com.codewaiter.cursomc.repositories.CidadeRepository;

@Service
public class CidadeService {

	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	public List<Cidade> findCidades(Integer estadoId){
		
		return cidadeRepository.findCidades(estadoId);
	}
}
