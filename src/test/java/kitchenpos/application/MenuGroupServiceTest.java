package kitchenpos.application;

import kitchenpos.CustomParameterizedTest;
import kitchenpos.ui.dto.MenuGroupDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MenuGroup 비즈니스 흐름 테스트")
@Transactional
@SpringBootTest
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴그룸 저장 - 성공")
    @CustomParameterizedTest
    @ValueSource(strings = {"두마리메뉴", "두마리메뉴", "한마리메뉴", "순살파닭두마리메뉴"})
    void create(String name) {
        //given
        MenuGroupDto expected = new MenuGroupDto(name);
        //when
        final MenuGroupDto actual = menuGroupService.create(expected);
        //then
        assertThat(actual).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
    }

    @DisplayName("메뉴그룹 조회 - 전체 메뉴그룹 조회")
    @Test
    void findAll() {
        //given
        //when
        final List<MenuGroupDto> actual = menuGroupService.list();
        //then
        assertThat(actual).isNotEmpty();
    }
}