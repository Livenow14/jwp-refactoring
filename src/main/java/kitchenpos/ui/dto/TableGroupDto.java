package kitchenpos.ui.dto;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupDto {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableDto> orderTables;

    protected TableGroupDto() {
    }

    public TableGroupDto(Long id, LocalDateTime createdDate, List<OrderTableDto> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupDto of(TableGroup tableGroup) {
        return new TableGroupDto(tableGroup.getId(), tableGroup.getCreatedDate(),
                tableGroup.getOrderTables().stream()
                        .map(OrderTableDto::of)
                        .collect(Collectors.toList()));
    }

    public TableGroup toEntity() {
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTableGroup = orderTables.stream()
                .map(OrderTableDto::toEntity)
                .collect(Collectors.toList());
        tableGroup.setOrderTables(orderTableGroup);
        return tableGroup;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }
}
