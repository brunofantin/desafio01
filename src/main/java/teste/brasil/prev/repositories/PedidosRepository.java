package teste.brasil.prev.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import teste.brasil.prev.entities.Cliente;
import teste.brasil.prev.entities.Pedido;

public interface PedidosRepository extends JpaRepository<Pedido, Integer> {
	
	Page<Pedido> findAllByCliente(Cliente cliente, Pageable pageable);
	
	Pedido findByClienteAndIdPedido(Cliente cliente, Integer idPedido);
	
}
