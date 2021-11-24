package kitchenpos.ui.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemDto {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    protected OrderLineItemDto() {
    }

    public OrderLineItemDto(Long seq, Long orderId, Long menuId, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemDto of(OrderLineItem orderLineItem) {
        return new OrderLineItemDto(orderLineItem.getSeq(), orderLineItem.getOrder().getId(),
                orderLineItem.getMenu().getId(), orderLineItem.getQuantity());
    }

    public OrderLineItem toEntity() {
        OrderLineItem orderLineItem = new OrderLineItem();
        Menu menu = new Menu();
        menu.setId(menuId);
        orderLineItem.setMenu(menu);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
