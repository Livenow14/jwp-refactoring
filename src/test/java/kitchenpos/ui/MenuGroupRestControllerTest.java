package kitchenpos.ui;

import kitchenpos.ui.dto.MenuGroupDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("메뉴 그룹 문서화 테스트")
class MenuGroupRestControllerTest extends ApiDocument {
    @DisplayName("메뉴 그룹 저장 - 성공")
    @Test
    void menu_group_create() throws Exception {
        //given
        MenuGroupDto requestMenuGroupDto = new MenuGroupDto("신메뉴");
        MenuGroupDto expected = new MenuGroupDto(1L, "신메뉴");
        //when
        willReturn(expected).given(menuGroupService).create(any(MenuGroupDto.class));
        final ResultActions actual = 메뉴_그룹_저장_요청(requestMenuGroupDto);
        //then
        메뉴_그룹_저장_성공함(actual, expected);
    }

    @DisplayName("메뉴 그룹 조회 - 성공")
    @Test
    void menu_group_findAll() throws Exception {
        //given
        List<MenuGroupDto> expected = Arrays.asList(
                new MenuGroupDto(1L, "두마리메뉴"),
                new MenuGroupDto(2L, "한마리메뉴"),
                new MenuGroupDto(3L, "순살파닭두마리메뉴"),
                new MenuGroupDto(4L, "신메뉴")
        );
        //when
        willReturn(expected).given(menuGroupService).list();
        final ResultActions result = 메뉴_그룹_조회_요청();
        //then
        메뉴_그룹_조회_성공함(result, expected);
    }

    private ResultActions 메뉴_그룹_저장_요청(MenuGroupDto menuGroupDto) throws Exception {
        return mockMvc.perform(post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(menuGroupDto))
        );
    }

    private ResultActions 메뉴_그룹_조회_요청() throws Exception {
        return mockMvc.perform(get("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
        );
    }

    private ResultActions 메뉴_그룹_저장_성공함(ResultActions result, MenuGroupDto menuGroupDto) throws Exception {
        return result.andExpect(status().isCreated())
                .andExpect(content().json(toJson(menuGroupDto)))
                .andExpect(header().string("Location", "/api/menu-groups/" + menuGroupDto.getId()))
                .andDo(toDocument("menu-group-create"));
    }

    private void 메뉴_그룹_조회_성공함(ResultActions result, List<MenuGroupDto> menuGroupDtos) throws Exception {
        result.andExpect(status().isOk())
                .andExpect(content().json(toJson(menuGroupDtos)))
                .andDo(toDocument("menu-group-findAll"));
    }
}