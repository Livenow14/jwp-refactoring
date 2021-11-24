package kitchenpos.ui;

import kitchenpos.ui.dto.OrderTableDto;
import kitchenpos.ui.dto.TableGroupDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("테이블 그룹 문서화 테스트")
class TableGroupRestControllerTest extends ApiDocument {
    @DisplayName("테이블 그룹 생성 - 성공")
    @Test
    void table_group_create() throws Exception {
        //given
        TableGroupDto requestTableGroupDto = new TableGroupDto(Arrays.asList(new OrderTableDto(1L), new OrderTableDto(2L)));
        TableGroupDto expected = new TableGroupDto(1L, LocalDateTime.now(),
                Arrays.asList(
                        new OrderTableDto(1L, null, 0, false), new OrderTableDto(2L, null, 0, false)
                )
        );
        //when
        willReturn(expected).given(tableGroupService).create(any(TableGroupDto.class));
        final ResultActions result = 테이블_그룹_생성_요청(requestTableGroupDto);
        //then
        테이블_그룹_생성_성공함(result, expected);
    }

    @DisplayName("테이블 그룹 해제 - 성공")
    @Test
    void table_ungroup() throws Exception {
        //given
        //when
        willDoNothing().given(tableGroupService).ungroup(anyLong());
        final ResultActions result = 테이블_그룹_해제_요청(1L);
        //then
        테이블_그룹_해제_성공함(result);
    }

    private ResultActions 테이블_그룹_생성_요청(TableGroupDto tableGroupDto) throws Exception {
        return mockMvc.perform(post("/api/table-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(tableGroupDto))
        );
    }

    private ResultActions 테이블_그룹_해제_요청(Long id) throws Exception {
        return mockMvc.perform(delete("/api/table-groups/{tableGroupId}", id));
    }

    private void 테이블_그룹_생성_성공함(ResultActions result, TableGroupDto tableGroupDto) throws Exception {
        result.andExpect(status().isCreated())
                .andExpect(content().json(toJson(tableGroupDto)))
                .andDo(toDocument("table-group-create"));
    }

    private void 테이블_그룹_해제_성공함(ResultActions result) throws Exception {
        result.andExpect(status().isNoContent())
                .andDo(toDocument("table-group-ungroup"));
    }
}