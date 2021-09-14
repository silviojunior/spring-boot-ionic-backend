package com.codewaiter.cursomc.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codewaiter.cursomc.domain.Produto;
import com.codewaiter.cursomc.dto.ProdutoDTO;
import com.codewaiter.cursomc.resources.utils.URL;
import com.codewaiter.cursomc.services.ProdutoService;

@RestController
@RequestMapping(value="/produtos")
public class ProdutoResource {
	
	@Autowired
	private ProdutoService produtoService;

	@GetMapping(value="/{id}")
	public ResponseEntity<Produto> listar(@PathVariable Integer id) {
		
		Produto produto = produtoService.find(id);
		
		return ResponseEntity.ok().body(produto);
	}
	
	@GetMapping
	public ResponseEntity<Page<ProdutoDTO>> findPage(
			@RequestParam(value="nome", defaultValue="") String nome, 
			@RequestParam(value="categorias", defaultValue="") String categorias, 
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction
			) {
		
		String nomeProduto = URL.decodeParam(nome);
		List<Integer> idsCategorias = URL.decodeIntList(categorias);
		
		Page<Produto> paginasProdutos = produtoService.search(nomeProduto, idsCategorias, page, linesPerPage, orderBy, direction);
		Page<ProdutoDTO> listaProdutos = paginasProdutos.map(produto -> new ProdutoDTO(produto)); 

		return ResponseEntity.ok().body(listaProdutos);
	}
}
