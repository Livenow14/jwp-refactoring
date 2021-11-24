package kitchenpos.ui;

import kitchenpos.ui.dto.MenuDto;
import kitchenpos.ui.dto.MenuProductDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("메뉴 문서화 테스트")
class MenuRestControllerTest extends ApiDocument {
    @DisplayName("메뉴 저장 - 성공")
    @Test
    void menu_create() throws Exception {
        //given
        MenuDto requestMenuDto = new MenuDto("후라이드+후라이드", new BigDecimal(19000), 1L, Collections.singletonList(new MenuProductDto(1L, 2L)));
        MenuDto expected = new MenuDto(7L, "후라이드+후라이드", new BigDecimal(19000), 1L, Collections.singletonList(new MenuProductDto(1L, 2L)));
        //when
        willReturn(expected).given(menuService).create(any(MenuDto.class));
        final ResultActions actual = 메뉴_저장_요청(requestMenuDto);
        //then
        메뉴_저장_성공함(actual, expected);
    }

    @DisplayName("메뉴 조회 - 성공")
    @Test
    void menu_findAll() throws Exception {
        //given
        List<MenuDto> expected = Arrays.asList(
                new MenuDto(1L, "후라이드치킨", new BigDecimal(16000), 2L, Collections.singletonList(new MenuProductDto(1L, 1L, 1L, 1L))),
                new MenuDto(2L, "양념치킨", new BigDecimal(16000), 2L, Collections.singletonList(new MenuProductDto(2L, 2L, 2L, 1L))),
                new MenuDto(3L, "반반치킨", new BigDecimal(16000), 2L, Collections.singletonList(new MenuProductDto(3L, 3L, 3L, 1L))),
                new MenuDto(4L, "통구이", new BigDecimal(17000), 2L, Collections.singletonList(new MenuProductDto(4L, 4L, 4L, 1L))),
                new MenuDto(5L, "간장치킨", new BigDecimal(17000), 2L, Collections.singletonList(new MenuProductDto(5L, 5L, 5L, 1L))),
                new MenuDto(6L, "순살치킨", new BigDecimal(17000), 2L, Collections.singletonList(new MenuProductDto(6L, 6L, 6L, 1L))),
                new MenuDto(7L, "후라이드+후라이드", new BigDecimal(19000), 1L, Collections.singletonList(new MenuProductDto(7L, 7L, 1L, 2L)))
        );
        //when
        willReturn(expected).given(menuService).list();
        final ResultActions result = 메뉴_조회_요청();
        //then
        메뉴_조회_성공함(result, expected);
    }

    private ResultActions 메뉴_저장_요청(MenuDto menuDto) throws Exception {
        return mockMvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(menuDto))
        );
    }

    private ResultActions 메뉴_조회_요청() throws Exception {
        return mockMvc.perform(get("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
        );
    }

    private void 메뉴_저장_성공함(ResultActions actual, MenuDto menuDto) throws Exception {
        actual.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/menus/" + menuDto.getId()))
                .andDo(toDocument("menu-create"));
    }

    private void 메뉴_조회_성공함(ResultActions actual, List<MenuDto> menuDtos) throws Exception {
        actual.andExpect(status().isOk())
                .andExpect(content().json(toJson(menuDtos)))
                .andDo(toDocument("menu-findAll"));
    }
}