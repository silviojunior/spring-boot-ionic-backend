package com.codewaiter.cursomc.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewaiter.cursomc.domain.Cidade;
import com.codewaiter.cursomc.domain.Estado;
import com.codewaiter.cursomc.dto.CidadeDTO;
import com.codewaiter.cursomc.dto.EstadoDTO;
import com.codewaiter.cursomc.services.CidadeService;
import com.codewaiter.cursomc.services.EstadoService;

@RestController
@RequestMapping(value="/estados")
public class EstadoResource {
	
	@Autowired
	private EstadoService estadoService;
	
	@Autowired
	private CidadeService cidadeService;
	
	@GetMapping
	public ResponseEntity<List<EstadoDTO>> findAll() {
		
		List<Estado> listaEstados = estadoService.findAll();
		List<EstadoDTO> listaEstadosDTO = listaEstados.stream().map(estado -> new EstadoDTO(estado)).collect(Collectors.toList());

		return ResponseEntity.ok().body(listaEstadosDTO);
	}
	
	@GetMapping(value="/{estado_id}/cidades")
	public ResponseEntity<List<CidadeDTO>> findAll(@PathVariable("estado_id") Integer estadoId) {
		
		List<Cidade> listaCidades = cidadeService.findCidades(estadoId);
		List<CidadeDTO> listaCidadesDTO = listaCidades.stream().map(cidade -> new CidadeDTO(cidade)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listaCidadesDTO);
	}
}
