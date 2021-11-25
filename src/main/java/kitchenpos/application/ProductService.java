package kitchenpos.application;

import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.ProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductDto create(final ProductDto productDto) {
        final BigDecimal price = productDto.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            log.info("price가 null이거나 음수입니다.");
            throw new IllegalArgumentException();
        }

        return ProductDto.of(productRepository.save(productDto.toEntity()));
    }

    public List<ProductDto> list() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductDto::of)
                .collect(Collectors.toList());
    }
}
