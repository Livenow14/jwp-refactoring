package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.ui.dto.OrderDto;
import kitchenpos.ui.dto.OrderLineItemDto;
import kitchenpos.ui.dto.OrderTableDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Order 비즈니스 흐름 테스트")
@Transactional
@SpringBootTest
class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @DisplayName("주문 저장 - 실패 - 주문항목이 비어있음")
    @Test
    void createFailureWhenOrderLinesEmpty() {
        //given
        OrderDto expected = new OrderDto(1L, Collections.emptyList());
        //when
        //then
        assertThatThrownBy(() -> orderService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 저장 - 실패 - 저장된 메뉴 수와 요청했던 메뉴 수가 다름")
    @Test
    void createFailureWhenMenusNotMatch() {
        //given
        Long savedMenuId = 1L;
        Long unSavedMenuId = 0L;
        OrderDto expected = new OrderDto(1L,
                Arrays.asList(new OrderLineItemDto(savedMenuId, 2L), new OrderLineItemDto(unSavedMenuId, 2L))
        );
        //when
        //then
        assertThatThrownBy(() -> orderService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 저장 - 실패 - 주문테이블의 empty값이 true")
    @Test
    void createFailureWhenOrderTableEmptyTrue() {
        //given
        OrderDto expected = new OrderDto(1L, Collections.singletonList(new OrderLineItemDto(1L, 1L)));
        //when
        //then
        assertThatThrownBy(() -> orderService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 저장 - 실패 - 주문테이블의 존재하지 않음")
    @Test
    void createFailureWhenOrderTableNotFOUNd() {
        //given
        OrderDto expected = new OrderDto(0L, Collections.singletonList(new OrderLineItemDto(1L, 1L)));
        //when
        //then
        assertThatThrownBy(() -> orderService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 조회 - 성공 - 전체 주문 조회")
    @Test
    void findAll() {
        //given
        OrderTableDto expectedOrderTable = tableService.create(new OrderTableDto(false));
        OrderDto expected = orderService.create(new OrderDto(expectedOrderTable.getId(), Collections.singletonList(new OrderLineItemDto(1L, 1L))));
        //when
        final List<OrderDto> actual = orderService.list();
        //then
        assertThat(actual).hasSize(1);
        assertThat(actual).extracting(OrderDto::getId)
                .contains(expected.getId());
        assertThat(actual).flatExtracting(OrderDto::getOrderLineItems)
                .isNotEmpty();
    }

    @DisplayName("주문 상태 수정 - 성공")
    @Test
    void changeOrderStatus() {
        //given
        OrderTableDto expectedOrderTable = tableService.create(new OrderTableDto(false));
        OrderDto expectedOrder = orderService.create(new OrderDto(expectedOrderTable.getId(), Collections.singletonList(new OrderLineItemDto(1L, 1L))));
        //when
        OrderDto actual = orderService.changeOrderStatus(expectedOrder.getId(), new OrderDto(OrderStatus.MEAL));
        //then
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }


    @DisplayName("주문 상태 수정 - 실패 - 주문이 존재하지 않음")
    @Test
    void changeOrderStatusFailureWhenNotFoundOrder() {
        //given
        //when
        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(0L, new OrderDto(OrderStatus.MEAL)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 수정 - 실패 - 주문상태가 COMPLETION")
    @Test
    void changeOrderStatusFailureOrderStatusCompletion() {
        //given
        OrderTableDto expectedOrderTable = tableService.create(new OrderTableDto(false));
        OrderDto expectedOrder = orderService.create(new OrderDto(expectedOrderTable.getId(), Collections.singletonList(new OrderLineItemDto(1L, 1L))));
        orderService.changeOrderStatus(expectedOrder.getId(), new OrderDto(OrderStatus.COMPLETION));
        //when
        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(expectedOrder.getId(), new OrderDto(OrderStatus.COMPLETION)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
