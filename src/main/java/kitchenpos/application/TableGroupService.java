package kitchenpos.application;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.OrderTableDto;
import kitchenpos.ui.dto.TableGroupDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private static final Logger log = LoggerFactory.getLogger(TableGroupService.class);

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupDto create(final TableGroupDto tableGroupDto) {
        final List<OrderTable> orderTables = tableGroupDto.getOrderTables().stream()
                .map(OrderTableDto::toEntity)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            log.info("입력한 OrderTable이 없거나 사이즈가 2미만입니다.");
            throw new IllegalArgumentException();
        }

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllById(orderTableIds);

        if (orderTables.size() != savedOrderTables.size()) {
            log.info("저장된 주물테이블 수와 요청한 주문테이블의 수가 다릅니다.");
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                log.info("주문테이블이 비어있지 않거나 이미 단체지정이 완료되어있습니다.");
                throw new IllegalArgumentException();
            }
        }

        TableGroup tableGroup = tableGroupDto.toEntity();
        tableGroup.setCreatedDate(LocalDateTime.now());

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        for (final OrderTable savedOrderTable : savedOrderTables) {
            //TODO
            savedOrderTable.setTableGroup(savedTableGroup);
            savedOrderTable.setEmpty(false);
            orderTableRepository.save(savedOrderTable);
        }
        savedTableGroup.setOrderTables(savedOrderTables);

        return TableGroupDto.of(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroup(null);
            orderTable.setEmpty(false);
            orderTableRepository.save(orderTable);
        }
    }
}
