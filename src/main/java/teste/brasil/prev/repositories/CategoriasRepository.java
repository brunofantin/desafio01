package teste.brasil.prev.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import teste.brasil.prev.entities.Categoria;

public interface CategoriasRepository extends JpaRepository<Categoria, Integer> {
	
}
