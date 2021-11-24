package kitchenpos.application;

import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderDto;
import kitchenpos.ui.dto.OrderLineItemDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderDto create(final OrderDto orderDto) {
        final List<OrderLineItem> orderLineItems = orderDto.getOrderLineItems().stream()
                .map(OrderLineItemDto::toEntity)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItems.stream()
                .map(orderLineItem1 -> orderLineItem1.getMenu().getId())
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        Order order = orderDto.toEntity();

        order.setId(null);

        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTable().getId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        order.setOrderTable(orderTable);
        order.setOrderStatus(OrderStatus.COOKING);
        order.setOrderedTime(LocalDateTime.now());

        final Order savedOrder = orderRepository.save(order);

        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            //TODO
            orderLineItem.setOrder(savedOrder);
            savedOrderLineItems.add(orderLineItemRepository.save(orderLineItem));
        }
        savedOrder.setOrderLineItems(savedOrderLineItems);

        return OrderDto.of(savedOrder);
    }

    public List<OrderDto> list() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDto changeOrderStatus(final Long orderId, final OrderDto orderDto) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION, savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        savedOrder.setOrderStatus(orderDto.getOrderStatus());

        return OrderDto.of(orderRepository.save(savedOrder));
    }
}
