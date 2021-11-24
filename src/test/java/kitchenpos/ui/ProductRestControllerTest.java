package kitchenpos.ui;

import kitchenpos.ui.dto.ProductDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("상품 문서화 테스트")
class ProductRestControllerTest extends ApiDocument {
    @DisplayName("상품 저장 - 성공")
    @Test
    void product_create() throws Exception {
        //given
        ProductDto requestProductDto = new ProductDto("강정치킨", new BigDecimal(17000));
        ProductDto expected = new ProductDto(1L, "강정치킨", new BigDecimal(17000));
        //when
        willReturn(expected).given(productService).create(any(ProductDto.class));
        final ResultActions result = 상품_저장_요청(requestProductDto);
        //then
        상품_저장_성공함(result, expected);
    }

    @DisplayName("상품 조회 - 성공")
    @Test
    void product_findAll() throws Exception {
        //given
        List<ProductDto> expected = Arrays.asList(
                new ProductDto(1L, "후라이드", new BigDecimal(16000)),
                new ProductDto(2L, "양념치킨", new BigDecimal(16000)),
                new ProductDto(3L, "반반치킨", new BigDecimal(16000)),
                new ProductDto(4L, "통구이", new BigDecimal(17000)),
                new ProductDto(5L, "간장치킨", new BigDecimal(17000)),
                new ProductDto(6L, "순살치킨", new BigDecimal(17000)),
                new ProductDto(7L, "강정치킨", new BigDecimal(17000))
        );
        //when
        willReturn(expected).given(productService).list();
        final ResultActions result = 상품_조회_요청();
        //then
        상품_조회_성공함(result, expected);
    }

    private ResultActions 상품_저장_요청(ProductDto productDto) throws Exception {
        return mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(productDto))
        );
    }

    private ResultActions 상품_조회_요청() throws Exception {
        return mockMvc.perform(get("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
        );
    }

    private void 상품_저장_성공함(ResultActions result, ProductDto productDto) throws Exception {
        result.andExpect(status().isCreated())
                .andExpect(content().json(toJson(productDto)))
                .andExpect(header().string("Location", "/api/products/" + productDto.getId()))
                .andDo(toDocument("product-create"));
    }

    private void 상품_조회_성공함(ResultActions result, List<ProductDto> productDtos) throws Exception {
        result.andExpect(status().isOk())
                .andExpect(content().json(toJson(productDtos)))
                .andDo(toDocument("product-findAll"));
    }
}