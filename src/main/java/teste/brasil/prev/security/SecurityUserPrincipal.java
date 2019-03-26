package teste.brasil.prev.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import teste.brasil.prev.entities.Cliente;

public class SecurityUserPrincipal implements UserDetails {
    
	private static final long serialVersionUID = 6331691230877506611L;
	
	private Cliente cliente;
 
	private List<SimpleGrantedAuthority> roles = new ArrayList<>();
	
    public SecurityUserPrincipal(Cliente cliente, boolean admin) {
        this.cliente = cliente;
        
        roles.add(new SimpleGrantedAuthority("USER"));
        
        if(admin) {
        	roles.add(new SimpleGrantedAuthority("ADMIN"));
        }
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	public String getPassword() {
		return cliente.getSenha();
	}

	@Override
	public String getUsername() {
		return cliente.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
    
}