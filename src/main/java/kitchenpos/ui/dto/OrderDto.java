package kitchenpos.ui.dto;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDto {
    private Long id;
    private Long orderTableId;
    private OrderStatus orderStatus;
    private LocalDateTime orderedTime;
    private List<OrderLineItemDto> orderLineItems;

    protected OrderDto() {
    }

    public OrderDto(Long id, Long orderTableId, OrderStatus orderStatus, LocalDateTime orderedTime, List<OrderLineItemDto> orderLineItems) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderedTime = orderedTime;
        this.orderLineItems = orderLineItems;
    }

    public static OrderDto of(Order order) {
        return new OrderDto(order.getId(), order.getOrderTable().getId(),
                order.getOrderStatus(), order.getOrderedTime(), order.getOrderLineItems().stream()
                .map(OrderLineItemDto::of)
                .collect(Collectors.toList()));
    }

    public Order toEntity() {
        Order order = new Order();
        OrderTable orderTable = new OrderTable();
        orderTable.setId(orderTableId);
        order.setOrderTable(orderTable);
        order.setOrderLineItems(orderLineItems.stream()
                .map(OrderLineItemDto::toEntity)
                .collect(Collectors.toList()));
        return order;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public List<OrderLineItemDto> getOrderLineItems() {
        return orderLineItems;
    }
}
