package kitchenpos.ui.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductDto> menuProducts;

    protected MenuDto() {
    }

    public MenuDto(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProductDto> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuDto of(Menu menu) {
        return new MenuDto(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroup().getId(),
                menu.getMenuProducts().stream()
                        .map(MenuProductDto::of)
                        .collect(Collectors.toList())
                );
    }

    public Menu toEntity() {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(menuGroupId);
        menu.setMenuGroup(menuGroup);

        List<MenuProduct> collect = menuProducts.stream()
                .map(MenuProductDto::toEntity)
                .collect(Collectors.toList());
        menu.setMenuProducts(collect);
        return menu;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductDto> getMenuProducts() {
        return menuProducts;
    }
}
