package com.codewaiter.cursomc.services;

import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.codewaiter.cursomc.domain.Cliente;
import com.codewaiter.cursomc.domain.ItemPedido;
import com.codewaiter.cursomc.domain.PagamentoComBoleto;
import com.codewaiter.cursomc.domain.Pedido;
import com.codewaiter.cursomc.domain.enums.EstadoPagamento;
import com.codewaiter.cursomc.repositories.ItemPedidoRepository;
import com.codewaiter.cursomc.repositories.PagamentoRepository;
import com.codewaiter.cursomc.repositories.PedidoRepository;
import com.codewaiter.cursomc.security.User;
import com.codewaiter.cursomc.services.exceptions.AuthorizationException;
import com.codewaiter.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	PedidoRepository pedidoRepository;
	
	@Autowired
	BoletoService boletoService;
	
	@Autowired
	PagamentoRepository pagamentoRepository;
	
	@Autowired
	ProdutoService produtoService;
	
	@Autowired
	ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	ClienteService clienteService;
	
	@Autowired
	EmailService emailService;
	
	public Pedido find(Integer id) {
		
		Optional<Pedido> pedido = pedidoRepository.findById(id);
		
		return pedido.orElseThrow(() -> {
			throw new ObjectNotFoundException("Pedido Não Encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName());
		});
	}

	public @Valid Pedido insert(@Valid Pedido pedido) {
		
		pedido.setId(null);
		pedido.setInstante(new Date());
		pedido.setCliente(clienteService.find(pedido.getCliente().getId()));
		pedido.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		pedido.getPagamento().setPedido(pedido);
		
		if(pedido.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagtoBoleto = (PagamentoComBoleto) pedido.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagtoBoleto, pedido.getInstante());
		}
		pedido = pedidoRepository.save(pedido);
		pagamentoRepository.save(pedido.getPagamento());

		for(ItemPedido ip : pedido.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(pedido);
		}
		itemPedidoRepository.saveAll(pedido.getItens());
		emailService.sendOrderConfirmationHtmlEmail(pedido);
		
		return pedido;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		
		User user = UserService.authenticatedUser();
		
		if(user == null) {
			throw new AuthorizationException("Acesso Negado!");
		}
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente =  clienteService.find(user.getId());
		
		return pedidoRepository.findByCliente(cliente,pageRequest);
	}
	
}
