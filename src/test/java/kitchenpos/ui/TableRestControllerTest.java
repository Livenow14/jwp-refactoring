package kitchenpos.ui;

import kitchenpos.ui.dto.OrderTableDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("테이블 문서화 테스트")
class TableRestControllerTest extends ApiDocument {
    @DisplayName("테이블 생성 - 성공")
    @Test
    void table_create() throws Exception {
        //given
        OrderTableDto requestOrderTableDto = new OrderTableDto(true);
        OrderTableDto expected = new OrderTableDto(1L, null, 0, true);
        //when
        willReturn(expected).given(tableService).create(any(OrderTableDto.class));
        final ResultActions result = 테이블_생성_요청(requestOrderTableDto);
        //then
        테이블_생성_성공함(result, expected);
    }

    @DisplayName("테이블 조회 - 성공")
    @Test
    void table_findAll() throws Exception {
        //given
        List<OrderTableDto> expected = Arrays.asList(
                new OrderTableDto(1L, null, 0, true),
                new OrderTableDto(2L, null, 0, true),
                new OrderTableDto(3L, null, 0, true),
                new OrderTableDto(4L, null, 0, true),
                new OrderTableDto(5L, null, 0, true),
                new OrderTableDto(6L, null, 0, true),
                new OrderTableDto(7L, null, 0, true),
                new OrderTableDto(8L, null, 0, true),
                new OrderTableDto(9L, null, 0, true)
        );
        //when
        willReturn(expected).given(tableService).list();
        final ResultActions result = 테이블_조회_요청();
        //then
        테이블_조회_성공함(result, expected);
    }

    @DisplayName("테이블 상태 변경 - 성공")
    @Test
    void table_change_status() throws Exception {
        //given
        OrderTableDto requestOrderTableDto = new OrderTableDto(false);
        OrderTableDto expected = new OrderTableDto(1L, null, 0, false);
        //when
        willReturn(expected).given(tableService).changeEmpty(anyLong(), any(OrderTableDto.class));
        final ResultActions result = 테이블_상태_변경_요청(1L, requestOrderTableDto);
        //then
        테이블_상태_변경_성공함(result, expected);
    }

    @DisplayName("테이블 손님 수 변경 - 성공")
    @Test
    void table_change_guest() throws Exception {
        //given
        OrderTableDto requestOrderTableDto = new OrderTableDto(4);
        OrderTableDto expected = new OrderTableDto(1L, null, 4, false);
        //when
        willReturn(expected).given(tableService).changeNumberOfGuests(anyLong(), any(OrderTableDto.class));
        final ResultActions result = 테이블_손님_수_변경_요청(1L, requestOrderTableDto);
        //then
        테이블_손님_수_변경_성공함(result, expected);
    }

    private ResultActions 테이블_생성_요청(OrderTableDto orderTableDto) throws Exception {
        return mockMvc.perform(post("/api/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(orderTableDto))
        );
    }

    private ResultActions 테이블_조회_요청() throws Exception {
        return mockMvc.perform(get("/api/tables")
                .contentType(MediaType.APPLICATION_JSON)
        );
    }

    private ResultActions 테이블_상태_변경_요청(Long id, OrderTableDto orderTableDto) throws Exception {
        return mockMvc.perform(put("/api/tables/{orderTableId}/empty", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(orderTableDto))
        );
    }

    private ResultActions 테이블_손님_수_변경_요청(Long id, OrderTableDto orderTableDto) throws Exception {
        return mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(orderTableDto))
        );
    }

    private void 테이블_생성_성공함(ResultActions result, OrderTableDto orderTableDto) throws Exception {
        result.andExpect(status().isCreated())
                .andExpect(content().json(toJson(orderTableDto)))
                .andExpect(header().string("Location", "/api/tables/" + orderTableDto.getId()))
                .andDo(toDocument("table-create"));
    }

    private void 테이블_조회_성공함(ResultActions result, List<OrderTableDto> orderTableDtos) throws Exception {
        result.andExpect(status().isOk())
                .andExpect(content().json(toJson(orderTableDtos)))
                .andDo(toDocument("table-findAll"));
    }

    private void 테이블_상태_변경_성공함(ResultActions result, OrderTableDto orderTableDto) throws Exception {
        result.andExpect(status().isOk())
                .andExpect(content().json(toJson(orderTableDto)))
                .andDo(toDocument("table-change-status"));
    }

    private void 테이블_손님_수_변경_성공함(ResultActions result, OrderTableDto orderTableDto) throws Exception {
        result.andExpect(status().isOk())
                .andExpect(content().json(toJson(orderTableDto)))
                .andDo(toDocument("table-change-quest-number"));
    }
}