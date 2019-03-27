package teste.brasil.prev.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import teste.brasil.prev.entities.Produto;

public interface ProdutosRepository extends JpaRepository<Produto, Integer> {
	
}
