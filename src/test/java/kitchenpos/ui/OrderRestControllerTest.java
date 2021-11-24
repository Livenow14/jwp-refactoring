package kitchenpos.ui;

import kitchenpos.domain.OrderStatus;
import kitchenpos.ui.dto.OrderDto;
import kitchenpos.ui.dto.OrderLineItemDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("주문 문서화 테스트")
class OrderRestControllerTest extends ApiDocument {
    @DisplayName("주문 저장 - 성공")
    @Test
    void order_create() throws Exception {
        //given
        OrderDto requestOrderDto = new OrderDto(1L, Collections.singletonList(new OrderLineItemDto(1L, 1L)));
        OrderDto expected = new OrderDto(1L, 1L, OrderStatus.COOKING, LocalDateTime.now(), Collections.singletonList(new OrderLineItemDto(1L, 1L, 1L, 1L)));
        //when
        willReturn(expected).given(orderService).create(any(OrderDto.class));
        final ResultActions result = 주문_저장_요청(requestOrderDto);
        //then
        주문_저장_성공함(result, expected);
    }

    @DisplayName("주문 조회 - 성공")
    @Test
    void order_findAll() throws Exception {
        //given
        List<OrderDto> expected = Collections.singletonList(
                new OrderDto(1L, 1L, OrderStatus.COOKING, LocalDateTime.now(),
                        Collections.singletonList(new OrderLineItemDto(1L, 1L, 1L, 1L))
                )
        );
        //when
        willReturn(expected).given(orderService).list();
        final ResultActions result = 주문_조회_요청();
        //then
        주문_조회_성공함(result, expected);
    }

    @DisplayName("주문 상태 변경 - 성공")
    @Test
    void order_change_order_status() throws Exception {
        //given
        OrderDto requestOrderDto = new OrderDto(OrderStatus.COMPLETION);
        OrderDto expected = new OrderDto(1L, 1L, OrderStatus.COMPLETION, LocalDateTime.now(), Collections.singletonList(new OrderLineItemDto(1L, 1L, 1L, 1L)));
        //when
        willReturn(expected).given(orderService).changeOrderStatus(anyLong(), any(OrderDto.class));
        final ResultActions result = 주문_상태_변경_요청(1L, requestOrderDto);
        //then
        주문_상태_변경_성공함(result, expected);
    }

    private ResultActions 주문_저장_요청(OrderDto orderDto) throws Exception {
        return mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(orderDto))
        );
    }

    private ResultActions 주문_조회_요청() throws Exception {
        return mockMvc.perform(get("/api/orders"));
    }

    private ResultActions 주문_상태_변경_요청(Long id, OrderDto orderDto) throws Exception {
        return mockMvc.perform(put("/api/orders/{orderId}/order-status", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(orderDto))
        );
    }

    private void 주문_저장_성공함(ResultActions result, OrderDto orderDto) throws Exception {
        result.andExpect(status().isCreated())
                .andExpect(content().json(toJson(orderDto)))
                .andExpect(header().string("Location", "/api/orders/" + orderDto.getId()))
                .andDo(toDocument("order-create"));
    }

    private void 주문_조회_성공함(ResultActions result, List<OrderDto> orderDtos) throws Exception {
        result.andExpect(status().isOk())
                .andExpect(content().json(toJson(orderDtos)))
                .andDo(toDocument("order-findAll"));
    }

    private void 주문_상태_변경_성공함(ResultActions result, OrderDto orderDto) throws Exception {
        result.andExpect(status().isOk())
                .andExpect(content().json(toJson(orderDto)))
                .andDo(toDocument("order-change-status"));
    }
}
