package kitchenpos.application;

import kitchenpos.CustomParameterizedTest;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.MenuDto;
import kitchenpos.ui.dto.MenuProductDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Menu 비즈니스 흐름 테스트")
@Transactional
@SpringBootTest
class MenuServiceTest {
    private static final MenuDto 후라이드치킨;
    private static final MenuProductDto MENU_PRODUCT_DTO;

    static {
        MENU_PRODUCT_DTO = new MenuProductDto(1L, 2L);
        후라이드치킨 = new MenuDto("후라이드치킨", new BigDecimal(19000), 1L, Collections.singletonList(MENU_PRODUCT_DTO));
    }

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("메뉴 저장 - 성공")
    @Test
    void create() {
        //given
        //when
        final MenuDto actual = menuService.create(후라이드치킨);
        //then
        assertThat(actual).usingRecursiveComparison().ignoringFields("id", "menuProducts").isEqualTo(후라이드치킨);
    }

    @DisplayName("메뉴 저장 - 실패 - 메뉴의 가격이 null")
    @Test
    void createFailureWhenPriceNull() {
        //given
        MenuDto expected = new MenuDto(후라이드치킨.getName(), null, 후라이드치킨.getMenuGroupId(), 후라이드치킨.getMenuProducts());
        //when
        //then
        assertThatThrownBy(() -> menuService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 저장 - 실패 - 메뉴의 가격이 음수")
    @Test
    void createFailureWhenPriceMinus() {
        //given
        MenuDto expected = new MenuDto(후라이드치킨.getName(), new BigDecimal("-1"), 후라이드치킨.getMenuGroupId(), 후라이드치킨.getMenuProducts());
        //when
        //then
        assertThatThrownBy(() -> menuService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("메뉴 저장 - 실패 - 메뉴그룹이 존재하지 않음")
    @CustomParameterizedTest
    @NullSource
    @ValueSource(longs = {0L, -1L})
    void createFailureWhenNotFoundMenuGroup(Long menuGroupId) {
        //given
        MenuDto expected = new MenuDto(후라이드치킨.getName(), 후라이드치킨.getPrice(), menuGroupId, 후라이드치킨.getMenuProducts());
        //when
        //then
        assertThatThrownBy(() -> menuService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 저장 - 실패 - 메뉴상품의 상품이 존재하지 않음")
    @CustomParameterizedTest
    @NullSource
    @ValueSource(longs = {0L, -1L})
    void createFailureWhenNotFoundMenuGroupProduct(Long productId) {
        //given
        MenuDto expected = new MenuDto(후라이드치킨.getName(), 후라이드치킨.getPrice(),
                후라이드치킨.getMenuGroupId(), Collections.singletonList(new MenuProductDto(productId, 2L))
        );
        //when
        //then
        assertThatThrownBy(() -> menuService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 저장 - 실패 - 저장하려는 메뉴의 가격이 상품의 가격*수량 보다 높음")
    @Test
    void createFailureWhenMenuPriceOverThanProducePriceMultiQuantity() {
        //given
        Product foundProduct = productRepository.findById(MENU_PRODUCT_DTO.getProductId()).get();
        BigDecimal expectPrice = new BigDecimal(MENU_PRODUCT_DTO.getQuantity())
                .multiply(foundProduct.getPrice())
                .add(BigDecimal.ONE);
        MenuDto expected = new MenuDto(후라이드치킨.getName(), expectPrice, 후라이드치킨.getMenuGroupId(), 후라이드치킨.getMenuProducts());
        //when
        //then
        assertThatThrownBy(() -> menuService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 조회 - 성공 - 전체 메뉴 조회")
    @Test
    void findAll() {
        //given
        //when
        final List<MenuDto> actual = menuService.list();
        //then
        assertThat(actual).isNotEmpty();
    }
}