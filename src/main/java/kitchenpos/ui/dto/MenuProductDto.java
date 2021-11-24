package kitchenpos.ui.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuProductDto {
    private Long seq;
    private Long menuId;
    private Long productId;
    private Long quantity;

    protected MenuProductDto() {
    }

    public MenuProductDto(Long productId, Long quantity) {
        this(null, null, productId, quantity);
    }

    public MenuProductDto(Long seq, Long menuId, Long productId, Long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductDto of(MenuProduct menuProduct) {
        return new MenuProductDto(menuProduct.getSeq(), menuProduct.getMenu().getId(),
                menuProduct.getProduct().getId(), menuProduct.getQuantity());
    }

    public MenuProduct toEntity() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        Menu menu = new Menu();
        menu.setId(menuId);
        menuProduct.setMenu(menu);
        Product product = new Product();
        product.setId(productId);
        menuProduct.setProduct(product);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
