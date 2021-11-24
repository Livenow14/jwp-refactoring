package kitchenpos.dao;

import kitchenpos.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END" +
            " FROM Order o WHERE o.orderTable.id IN (:orderTableIds) AND o.orderStatus IN (:orderStatuses)"
    )
    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN TRUE ELSE FALSE END" +
            " FROM Order o WHERE o.orderTable.id IN (:orderTableId) AND o.orderStatus IN (:orderStatuses)")
    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses);
}