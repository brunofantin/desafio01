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

import teste.brasil.prev.entities.Categoria;
import teste.brasil.prev.repositories.CategoriasRepository;

@RestController()
@RequestMapping("api/categorias")
public class CategoriasController {

	@Autowired
	private CategoriasRepository cats;
	
	@GetMapping
	public Page<Categoria> index(@RequestParam(defaultValue = "0") int pagina, 
							   @RequestParam(defaultValue = "10") int quant,
							   @RequestParam(required = false) String ordem) {
		
		Sort sort = Sort.by(ordem != null ? ordem : "idCategoria");
		
		Page<Categoria> lista = cats.findAll(PageRequest.of(pagina, quant, sort));
		
		return lista;
	}
	
	@GetMapping("/{id}")
	public Categoria obter(@PathVariable("id") Integer id) {
		Optional<Categoria> cat = cats.findById(id);
		
		if(!cat.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada");
		}
		
		return cat.get();
	}
	
	@PostMapping
	public Categoria criar(HttpServletResponse response, @RequestBody Categoria cat) {
		cat.setIdCategoria(null);
		
		cat = cats.save(cat);
		
		response.setStatus(HttpServletResponse.SC_CREATED);
		
		return cat;
	}
	
	@PutMapping
	public void alterar(HttpServletResponse response, @RequestBody Categoria cat) {
		obter(cat.getIdCategoria());
		
		cats.save(cat);
	}
	
	@DeleteMapping("/{id}")
	public void remover(@PathVariable("id") Integer id) {
		try {
			cats.deleteById(id);
		} catch(EmptyResultDataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada");
		}
	}
	
}
