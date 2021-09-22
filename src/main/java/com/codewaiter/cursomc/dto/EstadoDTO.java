package com.codewaiter.cursomc.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import com.codewaiter.cursomc.domain.Estado;

public class EstadoDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	@NotEmpty(message="Preenchimento Obrigatório")
	private String nome;
	
	 public EstadoDTO() {

	 }

	public EstadoDTO(Integer id, @NotEmpty(message="Preenchimento Obrigatório") String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}
	
	public EstadoDTO(Estado estado) {
		this.id = estado.getId();
		this.nome = estado.getNome();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
