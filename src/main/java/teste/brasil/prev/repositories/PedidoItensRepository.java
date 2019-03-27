package teste.brasil.prev.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import teste.brasil.prev.entities.Pedido;
import teste.brasil.prev.entities.PedidoItem;

public interface PedidoItensRepository extends JpaRepository<PedidoItem, Integer> {
	
	List<PedidoItem> findAllByPedido(Pedido pedido);
	
	PedidoItem findByPedidoAndIdItem(Pedido pedido, Integer idItem);
	
	void deleteAllByPedido(Pedido pedido);
	
}
