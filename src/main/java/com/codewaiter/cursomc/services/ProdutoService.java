package com.codewaiter.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.codewaiter.cursomc.domain.Categoria;
import com.codewaiter.cursomc.domain.Produto;
import com.codewaiter.cursomc.repositories.CategoriaRepository;
import com.codewaiter.cursomc.repositories.ProdutoRepository;
import com.codewaiter.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	ProdutoRepository produtoRepository;
	
	@Autowired
	CategoriaRepository categoriaRepository;
	
	public Produto find(Integer id) {
		
		Optional<Produto> produto = produtoRepository.findById(id);
		
		return produto.orElseThrow(() -> {
			throw new ObjectNotFoundException("Produto Não Encontrado! Id: " + id + ", Tipo: " + Produto.class.getName());
		});
	}
	
	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		
		return produtoRepository.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);
	}
}