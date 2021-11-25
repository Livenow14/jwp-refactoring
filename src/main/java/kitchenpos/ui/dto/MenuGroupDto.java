package kitchenpos.ui.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupDto {
    private Long id;
    private String name;

    protected MenuGroupDto() {
    }

    public MenuGroupDto(String name) {
        this(null, name);
    }

    public MenuGroupDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupDto of(MenuGroup menuGroup) {
        return new MenuGroupDto(menuGroup.getId(), menuGroup.getName());
    }

    public MenuGroup toEntity() {
        return new MenuGroup(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
