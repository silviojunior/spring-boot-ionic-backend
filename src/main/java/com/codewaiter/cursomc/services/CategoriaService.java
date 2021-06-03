package com.codewaiter.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codewaiter.cursomc.domain.Categoria;
import com.codewaiter.cursomc.repositories.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	
	public Categoria buscar(Integer id) {
		
		Optional<Categoria> categoria = categoriaRepository.findById(id);
		
		return categoria.orElse(null);
	}
}
