package com.codewaiter.cursomc.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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

import com.codewaiter.cursomc.domain.Categoria;
import com.codewaiter.cursomc.dto.CategoriaDTO;
import com.codewaiter.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaService categoriaService;

	@GetMapping(value="/{id}")
	public ResponseEntity<Categoria> find(@PathVariable Integer id) {
		
		Categoria categoria = categoriaService.find(id);	
		return ResponseEntity.ok().body(categoria);
	}

	@GetMapping
	public ResponseEntity<List<CategoriaDTO>> findAll() {
		
		List<Categoria> categorias = categoriaService.findAll();
		List<CategoriaDTO> listaCategorias = new ArrayList<CategoriaDTO>();
		
		for(Categoria cat : categorias) {
			listaCategorias.add(new CategoriaDTO(cat));
		}
		/**
		 * Outra abordagem:
		 * listaCategorias = categorias.stream().map(categoria -> new CategoriaDTO(categoria)).collect(Collectors.toList());
		 */
		return ResponseEntity.ok().body(listaCategorias);
	}
	
	@GetMapping(value="/page")
	public ResponseEntity<Page<CategoriaDTO>> findPage(
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction
			) {
		
		Page<Categoria> paginasCategorias = categoriaService.findPage(page, linesPerPage, orderBy, direction);
		Page<CategoriaDTO> listaCategorias = paginasCategorias.map(categoria -> new CategoriaDTO(categoria)); 

		return ResponseEntity.ok().body(listaCategorias);
	}
	
	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody Categoria categoria){
		
		categoria = categoriaService.insert(categoria);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(categoria.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping(value="/{id}")
	public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody Categoria categoria){
		
		categoria.setId(id);
		categoria = categoriaService.update(categoria);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		
		categoriaService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
