package teste.brasil.prev.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import teste.brasil.prev.entities.Cliente;

public interface ClientesRepository extends JpaRepository<Cliente, Integer> {

	Cliente findByEmail(String email);
	
}
