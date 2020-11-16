package kitchenpos.dto.request;

import kitchenpos.domain.OrderTable;

public class OrderTableRequest {
    private final int numberOfGuests;
    private final boolean empty;

    public OrderTableRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toEntity() {
        return OrderTable.of(this.numberOfGuests, this.empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}