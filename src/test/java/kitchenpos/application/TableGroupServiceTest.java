package kitchenpos.application;

import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderDto;
import kitchenpos.ui.dto.OrderLineItemDto;
import kitchenpos.ui.dto.OrderTableDto;
import kitchenpos.ui.dto.TableGroupDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TableGroup 비즈니스 흐름 테스트")
@Transactional
@SpringBootTest
class TableGroupServiceTest {
    private static final LocalDateTime NOW = LocalDateTime.now();

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    private OrderTableDto firstOrderTableDto;
    private OrderTableDto secondOrderTableDto;

    @BeforeEach
    void setUp() {
        firstOrderTableDto = tableService.create(new OrderTableDto(true));
        secondOrderTableDto = tableService.create(new OrderTableDto(true));
    }

    @DisplayName("단체지정 저장 - 성공")
    @Test
    void create() {
        //given
        TableGroupDto expected = new TableGroupDto(Arrays.asList(new OrderTableDto(firstOrderTableDto.getId()), new OrderTableDto(secondOrderTableDto.getId())));
        //when
        TableGroupDto actual = tableGroupService.create(expected);
        //then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getCreatedDate()).isAfter(NOW);
        assertThat(actual.getOrderTables())
                .extracting(OrderTableDto::isEmpty)
                .containsExactly(false, false);
        assertThat(actual.getOrderTables())
                .extracting(OrderTableDto::getTableGroupId)
                .contains(actual.getId());
    }

    @DisplayName("단체지정 저장 - 실패 - 단체지정의 OrderTable이 null")
    @Test
    void createFailureWhenTableGroupNull() {
        //given
        TableGroupDto expected = new TableGroupDto(Collections.emptyList());
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정 저장 - 실패 - 단체지정의 OrderTable이 2 이상이 아닐 때")
    @Test
    void createFailureWhenOrderTableLowerThanTwo() {
        //given
        TableGroupDto expected = new TableGroupDto(Collections.singletonList(new OrderTableDto(firstOrderTableDto.getId())));
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정 저장 - 실패 - 저장된 주물테이블 수와 요청했던 주문테이블의 수가 다름")
    @Test
    void createFailureWhenOrderTableDoesNotMatches() {
        //given
        TableGroupDto expected = new TableGroupDto(Arrays.asList(new OrderTableDto(firstOrderTableDto.getId()), new OrderTableDto(0L)));
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정 저장 - 실패 - 주문테이블의 empty 값이 false")
    @Test
    void createFailureWhenOrderTableEmptyFalse() {
        //given
        tableService.changeEmpty(firstOrderTableDto.getId(), new OrderTableDto(false));
        tableService.changeEmpty(secondOrderTableDto.getId(), new OrderTableDto(false));
        TableGroupDto expected = new TableGroupDto(Arrays.asList(new OrderTableDto(firstOrderTableDto.getId()), new OrderTableDto(secondOrderTableDto.getId())));
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정 저장 - 실패 -  주문테이블에 이미 단체지정이 되어있음")
    @Test
    void createFailureWhenOrderTableAssignTableGroup() {
        //given
        TableGroupDto expected = new TableGroupDto(Arrays.asList(new OrderTableDto(firstOrderTableDto.getId()), new OrderTableDto(secondOrderTableDto.getId())));
        //when
        tableGroupService.create(expected);
        //then
        assertThatThrownBy(() -> tableGroupService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정 해제 - 성공 - 단체지정 해제")
    @Test
    void unGroupTableGroup() {
        //given
        TableGroupDto requestDto = new TableGroupDto(Arrays.asList(new OrderTableDto(firstOrderTableDto.getId()), new OrderTableDto(secondOrderTableDto.getId())));
        //when
        TableGroupDto expect = tableGroupService.create(requestDto);
        tableGroupService.ungroup(expect.getId());
        List<OrderTable> actual = orderTableRepository.findAllByTableGroupId(expect.getId());
        //then
        assertThat(actual).isEmpty();
    }

    @DisplayName("단체지정 해제 - 성공 - 주문 이후 단체지정 해제")
    @Test
    void unGroupTableGroupAfterOrder() {
        //given
        TableGroupDto requestDto = new TableGroupDto(Arrays.asList(new OrderTableDto(firstOrderTableDto.getId()), new OrderTableDto(secondOrderTableDto.getId())));
        //when
        TableGroupDto expect = tableGroupService.create(requestDto);
        OrderDto orderDto = orderService.create(new OrderDto(firstOrderTableDto.getId(), Collections.singletonList(new OrderLineItemDto(1L, 1L))));
        orderService.changeOrderStatus(orderDto.getId(), new OrderDto(OrderStatus.COMPLETION));
        tableGroupService.ungroup(expect.getId());
        List<OrderTable> actual = orderTableRepository.findAllByTableGroupId(expect.getId());
        //then
        assertThat(actual).isEmpty();
    }

    @DisplayName("단체지정 해제 - 실패 - 테이블의 주문 상태가 COMPLETION이 아님")
    @Test
    void unGroupTableGroupFailureWhenOrderStatusNotCompletion() {
        //given
        TableGroupDto requestDto = new TableGroupDto(Arrays.asList(new OrderTableDto(firstOrderTableDto.getId()), new OrderTableDto(secondOrderTableDto.getId())));
        //when
        TableGroupDto expect = tableGroupService.create(requestDto);
        OrderDto expected = orderService.create(new OrderDto(firstOrderTableDto.getId(), Collections.singletonList(new OrderLineItemDto(1L, 1L))));
        orderService.create(expected);
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(expect.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
