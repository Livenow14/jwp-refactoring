package kitchenpos.ui.dto;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class OrderTableDto {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    protected OrderTableDto() {
    }

    public OrderTableDto(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTableDto of(OrderTable orderTable) {
        return new OrderTableDto(orderTable.getId(), getTableGroupId(orderTable.getTableGroup()),
                orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    private static Long getTableGroupId(TableGroup tableGroup) {
        if (tableGroup == null) {
            return null;
        }
        return tableGroup.getId();
    }

    public OrderTable toEntity() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
