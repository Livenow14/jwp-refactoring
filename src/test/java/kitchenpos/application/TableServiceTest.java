package kitchenpos.application;

import kitchenpos.CustomParameterizedTest;
import kitchenpos.ui.dto.OrderDto;
import kitchenpos.ui.dto.OrderLineItemDto;
import kitchenpos.ui.dto.OrderTableDto;
import kitchenpos.ui.dto.TableGroupDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Table 비즈니스 흐름 테스트")
@Transactional
@SpringBootTest
class TableServiceTest {
    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderService orderService;

    private OrderTableDto orderTableDto;
    private int numberOfGuests;

    @BeforeEach
    void setUp() {
        orderTableDto = new OrderTableDto(true);
    }

    @DisplayName("주문테이블 저장 - 성공")
    @Test
    void create() {
        //given
        //when
        OrderTableDto actual = tableService.create(orderTableDto);
        //then
        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("주문테이블 조회 - 성공 - 전체 주문테이블 조회")
    @Test
    void findAll() {
        //given
        //when
        final List<OrderTableDto> actual = tableService.list();
        //then
        assertThat(actual).isNotEmpty();
    }

    @DisplayName("주문테이블 empty 값 수정 - 성공")
    @Test
    void changeEmpty() {
        //given
        //when
        OrderTableDto expected = tableService.create(orderTableDto);
        final OrderTableDto actual = tableService.changeEmpty(expected.getId(), new OrderTableDto(false));
        //then
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.isEmpty()).isFalse();
    }

    @DisplayName("주문테이블 empty 값 수정 - 실패 - 주문테이블이 존재하지 않음")
    @Test
    void changeEmptyWhenNotFoundOrderTable() {
        //given
        //when
        //then
        assertThatThrownBy(() -> tableService.changeEmpty(0L, new OrderTableDto(false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 empty 값 수정 - 실패 - 주문테이블이 단체에 지정되어 있음")
    @Test
    void changeEmptyWhenOrderTableAssignTableGroup() {
        //given
        //when
        OrderTableDto firstTable = tableService.create(orderTableDto);
        OrderTableDto secondTable = tableService.create(orderTableDto);
        TableGroupDto tableGroupDto = new TableGroupDto(Arrays.asList(firstTable, secondTable));
        tableGroupService.create(tableGroupDto);
        //then
        assertThatThrownBy(() -> tableService.changeEmpty(firstTable.getId(), new OrderTableDto(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 empty 값 수정 - 실패 - 주문테이블에 할당된 주문의 상태가 COMPLETITON이 아님")
    @Test
    void changeEmptyWhenOrderTableOrderIsNotCOMPLETION() {
        //given
        //when
        OrderTableDto expected = tableService.create(orderTableDto);
        tableService.changeEmpty(expected.getId(), new OrderTableDto(false));
        orderService.create(new OrderDto(expected.getId(), Collections.singletonList(new OrderLineItemDto(1L, 1L))));
        //then
        assertThatThrownBy(() -> tableService.changeEmpty(expected.getId(), new OrderTableDto(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 numberOfGuests 변경 - 성공")
    @Test
    void changeNumberOfGuests() {
        //given
        int numberOfGuests = 4;
        //when
        OrderTableDto expected = tableService.create(orderTableDto);
        tableService.changeEmpty(expected.getId(), new OrderTableDto(false));
        final OrderTableDto actual = tableService.changeNumberOfGuests(expected.getId(), new OrderTableDto(numberOfGuests));
        //then
        assertThat(actual.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @DisplayName("주문테이블 numberOfGuests 변경 - 실패 - 주문테이블이 존재하지 않음")
    @Test
    void changeNumberOfGuestsFailureWhenNotFoundOrderTable() {
        //given
        //when
        OrderTableDto expected = tableService.create(orderTableDto);
        tableService.changeEmpty(expected.getId(), new OrderTableDto(true));
        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, new OrderTableDto(4L)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 numberOfGuests 변경 - 실패 - 손님의 수가 0 미만")
    @CustomParameterizedTest
    @ValueSource(ints = {-1, -9999})
    void changeNumberOfGuestsFailureWhenNumberOfGuestLowerThanZero(int numberOfGuests) {
        //given
        //when
        OrderTableDto expected = tableService.create(orderTableDto);
        tableService.changeEmpty(expected.getId(), new OrderTableDto(true));
        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(expected.getId(), new OrderTableDto(numberOfGuests)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 numberOfGuests 변경 - 실패 - 테이블의 empty값이 true")
    @Test
    void changeNumberOfGuestsFailureWhenTableEmptyTrue() {
        //given
        //when
        OrderTableDto expected = tableService.create(orderTableDto);
        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(expected.getId(), new OrderTableDto(4)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
