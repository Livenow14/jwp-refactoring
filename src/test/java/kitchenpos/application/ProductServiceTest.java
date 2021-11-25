package kitchenpos.application;

import kitchenpos.CustomParameterizedTest;
import kitchenpos.ui.dto.ProductDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Proudct 비즈니스 흐름 테스트")
@Transactional
@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @DisplayName("상품 저장 - 성공")
    @CustomParameterizedTest
    @CsvSource(value = {"후라이드치킨, 16000.00", "후라이드치킨, 16000.00", "간장치킨, 17000.00"}, delimiter = ',')
    void create(String name, BigDecimal price) {
        //given
        ProductDto expected = new ProductDto(name, price);
        //when
        ProductDto actual = productService.create(expected);
        //then
        assertThat(actual).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
    }

    @DisplayName("상품 저장 - 실패 - 상품이 가격이 null")
    @CustomParameterizedTest
    @ValueSource(strings = {"후라이드치킨", "후라이드치킨", "간장치킨"})
    void createFailureWhenNullPrice(String name) {
        //given
        ProductDto expected = new ProductDto(name, null);
        //when
        //then
        assertThatThrownBy(() -> productService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 저장 - 실패 - 상품이 가격이 음수")
    @CustomParameterizedTest
    @CsvSource(value = {"후라이드치킨, -1", "후라이드치킨, -0.1", "간장치킨, -0.999"}, delimiter = ',')
    void createFailure(String name, BigDecimal price) {
        //given
        ProductDto expected = new ProductDto(name, price);
        //when
        //then
        assertThatThrownBy(() -> productService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 조회 - 성공 - 전체 상품 조회")
    @Test
    void findAll() {
        //given
        //when
        final List<ProductDto> actual = productService.list();
        //then
        assertThat(actual).isNotEmpty();
    }
}
