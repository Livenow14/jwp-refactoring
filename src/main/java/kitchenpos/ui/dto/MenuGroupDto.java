package kitchenpos.ui.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;

public class MenuGroupDto {
    private Long id;
    private String name;

    protected MenuGroupDto() {
    }

    public MenuGroupDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupDto of(MenuGroup menuGroup) {
        return new MenuGroupDto(menuGroup.getId(), menuGroup.getName());
    }

    public MenuGroup toEntity() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
