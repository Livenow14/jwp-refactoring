package kitchenpos.application;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderTableDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableService {
    private static final Logger log = LoggerFactory.getLogger(TableService.class);

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableDto create(final OrderTableDto orderTableDto) {
        return OrderTableDto.of(orderTableRepository.save(orderTableDto.toEntity()));
    }

    public List<OrderTableDto> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableDto changeEmpty(final Long orderTableId, final OrderTableDto orderTableDto) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            log.info("테이블 그룹으로 설정되어있습니다.");
            throw new IllegalArgumentException();
        }

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            log.info("주문테이블에 할당된 주문의 상태가 COMPLETITON이 아닙니다.");
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(orderTableDto.isEmpty());

        return OrderTableDto.of(orderTableRepository.save(savedOrderTable));
    }

    @Transactional
    public OrderTableDto changeNumberOfGuests(final Long orderTableId, final OrderTableDto orderTableDto) {
        final int numberOfGuests = orderTableDto.getNumberOfGuests();

        if (numberOfGuests < 0) {
            log.info("손님의 수는 음수일 수 없습니다.");
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            log.info("주문테이블이 비어있습니다.");
            throw new IllegalArgumentException();
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return OrderTableDto.of(orderTableRepository.save(savedOrderTable));
    }
}
