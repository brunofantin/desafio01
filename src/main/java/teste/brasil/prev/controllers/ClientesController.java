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
import org.springframework.util.StringUtils;
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

import teste.brasil.prev.entities.Cliente;
import teste.brasil.prev.repositories.ClientesRepository;
import teste.brasil.prev.to.ClienteTO;

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
	public Cliente criar(HttpServletResponse response, @RequestBody ClienteTO cliente) {
		cliente.setIdCliente(null);
		cliente.setSenha(encoder.encode(cliente.getSenha()));
		
		Cliente novoCliente = clientes.save(cliente.toCliente());
		
		response.setStatus(HttpServletResponse.SC_CREATED);
		
		return novoCliente;
	}
	
	@PutMapping
	public void alterar(HttpServletResponse response, @RequestBody ClienteTO cliente) {
		Cliente antigo = obter(cliente.getIdCliente());
		
		if(StringUtils.isEmpty(cliente.getSenha())) {
			//Sem Senha. Vamos utilizar a antiga 
			cliente.setSenha(antigo.getSenha());
		} else if (!cliente.getSenha().equals(antigo.getSenha())) {
			//Nova Senha. Vamos encodar
			cliente.setSenha(encoder.encode(cliente.getSenha()));
		}
		
		clientes.save(cliente.toCliente());
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
