package teste.brasil.prev.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import teste.brasil.prev.repositories.ClientesRepository;

@RestController()
@RequestMapping("api/clientes")
public class ClientesController {

	@Autowired
	private ClientesRepository clientes;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@GetMapping
	public Page<Cliente> index(@RequestParam(defaultValue = "0") int pagina, 
							   @RequestParam(defaultValue = "10") int quant,
							   @RequestParam(required = false) String ordem) {
		
		Sort sort = Sort.by(ordem != null ? ordem : "idCliente");
		
		Page<Cliente> lista = clientes.findAll(PageRequest.of(pagina, quant, sort));
		
		return lista;
	}
	
	@GetMapping("/{id}")
	public Cliente obter(@PathVariable("id") Integer id) {
		Optional<Cliente> cliente = clientes.findById(id);
		
		if(!cliente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado");
		}
		
		return cliente.get();
	}
	
	@PostMapping
	public Cliente salvar(HttpServletResponse response, @RequestBody Cliente cliente) {
		boolean novoCliente = cliente.getIdCliente() == null;
		
		if(novoCliente) {
			cliente.setSenha(encoder.encode(cliente.getSenha()));
		}
		
		cliente = clientes.save(cliente);
		
		if(novoCliente) {
			response.setStatus(HttpServletResponse.SC_CREATED);
		}
		
		return cliente;
	}
	
	@DeleteMapping("/{id}")
	public void remover(@PathVariable("id") Integer id) {
		try {
			clientes.deleteById(id);
		} catch(EmptyResultDataAccessException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado");
		}
	}
	
}
