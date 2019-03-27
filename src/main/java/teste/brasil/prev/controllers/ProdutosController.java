package teste.brasil.prev.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import teste.brasil.prev.entities.Produto;
import teste.brasil.prev.repositories.ProdutosRepository;

@RestController()
@RequestMapping("api/produtos")
public class ProdutosController {

	@Autowired
	private ProdutosRepository produtos;
	
	@GetMapping
	public Page<Produto> index(@RequestParam(defaultValue = "0") int pagina, 
							   @RequestParam(defaultValue = "10") int quant,
							   @RequestParam(required = false) String ordem) {
		
		Sort sort = Sort.by(ordem != null ? ordem : "idProduto");
		
		Page<Produto> lista = produtos.findAll(PageRequest.of(pagina, quant, sort));
		
		return lista;
	}
	
	@GetMapping("/{id}")
	public Produto obter(@PathVariable("id") Integer id) {
		Optional<Produto> cat = produtos.findById(id);
		
		if(!cat.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
		}
		
		return cat.get();
	}
	
	@PostMapping
	public Produto criar(HttpServletResponse response, @RequestBody Produto cat) {
		cat.setIdProduto(null);
		
		cat = produtos.save(cat);
		
		response.setStatus(HttpServletResponse.SC_CREATED);
		
		return cat;
	}
	
	@PutMapping
	public void alterar(HttpServletResponse response, @RequestBody Produto cat) {
		obter(cat.getIdProduto());
		
		produtos.save(cat);
	}
	
	@DeleteMapping("/{id}")
	public void remover(@PathVariable("id") Integer id) {
		try {
			produtos.deleteById(id);
		} catch(EmptyResultDataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
		}
	}
	
}
