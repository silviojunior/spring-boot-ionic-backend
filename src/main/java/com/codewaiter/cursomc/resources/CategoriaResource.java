package com.codewaiter.cursomc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewaiter.cursomc.domain.Categoria;
import com.codewaiter.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaService categoriaService;

	@GetMapping(value="/{id}")
	public ResponseEntity<?> listar(@PathVariable Integer id) {
		
		Categoria categoria = categoriaService.buscar(id);
		
		return ResponseEntity.ok().body(categoria);
	}
	
}
