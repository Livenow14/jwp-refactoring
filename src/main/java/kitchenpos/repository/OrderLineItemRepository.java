package kitchenpos.repository;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
    List<OrderLineItem> findAllByOrder(Order order);
}