package teste.brasil.prev.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import teste.brasil.prev.entities.Cliente;
import teste.brasil.prev.repositories.ClientesRepository;

@Service
public class SecurityUserDetailsService implements UserDetailsService {
 
    @Autowired
    private ClientesRepository clientes;
 
    @Override
    public UserDetails loadUserByUsername(String username) {
        Cliente cliente = clientes.findByEmail(username);
        
        if (cliente == null) {
            throw new UsernameNotFoundException(username);
        }
        
        return new SecurityUserPrincipal(cliente, isAdmin(cliente));
    }

	private boolean isAdmin(Cliente cliente) {
		return "admin@teste.com".equals(cliente.getEmail());
	}
}