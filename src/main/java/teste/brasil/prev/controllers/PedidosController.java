package teste.brasil.prev.controllers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import teste.brasil.prev.entities.Cliente;
import teste.brasil.prev.entities.Pedido;
import teste.brasil.prev.entities.PedidoItem;
import teste.brasil.prev.entities.Produto;
import teste.brasil.prev.repositories.ClientesRepository;
import teste.brasil.prev.repositories.PedidoItensRepository;
import teste.brasil.prev.repositories.PedidosRepository;
import teste.brasil.prev.repositories.ProdutosRepository;
import teste.brasil.prev.to.ItemTO;

@RestController()
@RequestMapping("api/pedidos")
public class PedidosController {

	@Autowired
	private PedidosRepository pedidos;
	
	@Autowired
	private PedidoItensRepository itens;
	
	@Autowired
	private ProdutosRepository produtos;
	
	@Autowired
	private ClientesRepository clientes;
	
	@GetMapping
	public Page<Pedido> index(@RequestParam(defaultValue = "0") int pagina, 
							   @RequestParam(defaultValue = "10") int quant,
							   @RequestParam(required = false) String ordem) {
		
		Sort sort = Sort.by(ordem != null ? ordem : "idPedido");
		
		Cliente cliente = obterCliente();
		
		Page<Pedido> lista = pedidos.findAllByCliente(cliente, PageRequest.of(pagina, quant, sort));
		
		return lista;
	}
	
	@GetMapping("/{id}")
	public Pedido obterPedido(@PathVariable("id") Integer id) {
		Cliente cliente = obterCliente();
		
		Pedido pedido = pedidos.findByClienteAndIdPedido(cliente, id);
		
		if(pedido == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado");
		}
		
		return pedido;
	}
	
	@PostMapping
	public Pedido criar(HttpServletResponse response, @RequestBody Pedido pedido) {
		Cliente cliente = obterCliente();
		
		pedido.setIdPedido(null);
		pedido.setCliente(cliente);
		pedido.setData(new Date(System.currentTimeMillis()));
		
		pedido = pedidos.save(pedido);
		
		response.setStatus(HttpServletResponse.SC_CREATED);
		
		return pedido;
	}
	
	@GetMapping("/{idPedido}/itens")
	public List<PedidoItem> obterPedidoItens(@PathVariable("idPedido") Integer idPedido) {
		Pedido pedido = obterPedido(idPedido);
		
		return itens.findAllByPedido(pedido);
	}
	
	@GetMapping("/{idPedido}/itens/{idItem}")
	public PedidoItem obterPedidoItem(@PathVariable("idPedido") Integer idPedido, @PathVariable("idItem") Integer idItem) {
		Pedido pedido = obterPedido(idPedido);
		
		PedidoItem item = itens.findByPedidoAndIdItem(pedido, idItem);
		
		if(item == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item do Pedido não encontrado");
		}
		
		return item;
	}
	
	@PostMapping("/{idPedido}/itens")
	public PedidoItem criarItem(HttpServletResponse response, @PathVariable("idPedido") Integer idPedido, @RequestBody ItemTO item) {
		Pedido pedido = obterPedido(idPedido);
		Optional<Produto> optProduto = produtos.findById(item.getIdProduto());
		
		if(!optProduto.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
		}
		
		Produto produto = optProduto.get();
		
		PedidoItem novo = new PedidoItem();
		
		novo.setPedido(pedido);
		novo.setProduto(produto);
		novo.setProdutoNome(produto.getProduto());
		novo.setQuantidade(item.getQuantidade());
		novo.setValor(produto.getPreco());
		novo.setSubtotal(novo.getValor().multiply(new BigDecimal(novo.getQuantidade())));
		
		novo = itens.save(novo);
		
		return novo;
	}
	
	@DeleteMapping("/{id}")
	public void remover(@PathVariable("id") Integer id) {
		Pedido pedido = obterPedido(id);
		
		itens.deleteAllByPedido(pedido);
		pedidos.deleteById(pedido.getIdPedido());
	}

	@DeleteMapping("/{idPedido}/itens/{idItem}")
	public void removerItem(@PathVariable("idPedido") Integer idPedido, @PathVariable("idItem") Integer idItem) {
		PedidoItem item = obterPedidoItem(idPedido, idItem);
		
		itens.delete(item);
	}
	
	private Cliente obterCliente() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		
		return clientes.findByEmail(email);
	}
}
