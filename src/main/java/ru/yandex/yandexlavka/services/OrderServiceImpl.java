package ru.yandex.yandexlavka.services;

import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.dto.OrderDTO;
import ru.yandex.yandexlavka.dto.request.CompleteOrderDTO;
import ru.yandex.yandexlavka.dto.request.CreateOrderDTO;
import ru.yandex.yandexlavka.dto.response.OrderAssignResponseDTO;
import ru.yandex.yandexlavka.entities.Courier;
import ru.yandex.yandexlavka.entities.Order;
import ru.yandex.yandexlavka.exceptions.BadRequestException;
import ru.yandex.yandexlavka.exceptions.NotFoundException;
import ru.yandex.yandexlavka.mappers.EntityMapper;
import ru.yandex.yandexlavka.repositories.CourierRepository;
import ru.yandex.yandexlavka.repositories.OrderRepository;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;
    private final EntityMapper entityMapper;

    public OrderServiceImpl(OrderRepository orderRepository, CourierRepository courierRepository, EntityMapper entityMapper) {
        this.orderRepository = orderRepository;
        this.courierRepository = courierRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public List<OrderDTO> createOrders(List<CreateOrderDTO> orders) {
        List<Order> entities = orders.stream()
                .map(entityMapper::toEntity)
                .collect(Collectors.toList());
        return orderRepository.saveAll(entities).stream()
                .map(entityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> ordersList(Integer limit, Integer offset) {
        return orderRepository.findAllWithLimitOffset(limit, offset).stream()
                .map(entityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO orderInfo(Long id) {
        return entityMapper.toDTO(orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found!")));
    }

    @Override
    public List<OrderDTO> completeOrders(List<CompleteOrderDTO> completeInfo) {
        List<Order> entities = new ArrayList<>();
        for (CompleteOrderDTO completeOrderDTO : completeInfo) {
            Order order = orderRepository.findById(completeOrderDTO.getOrderId())
                    .orElseThrow(() -> new BadRequestException("Order not found!"));
            Courier courier = Optional.ofNullable(order.getCourier())
                    .orElseThrow(() -> new BadRequestException("Order not assigned!"));
            if (!courier.getId().equals(completeOrderDTO.getCourierId())) {
                throw new BadRequestException("Courier mismatch!");
            }
            if (order.getCompletedTime() == null) {
                order.setCompletedTime(completeOrderDTO.getCompleteTime());
            }
            entities.add(order);
        }
        return orderRepository.saveAll(entities).stream()
                .map(entityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderAssignResponseDTO assignOrders(LocalDate date) {
        List<Object[]> rows = orderRepository.findAllUnassignedOrdersAndMatchingCouriers();
        List<Order> assignedOrders = new ArrayList<>();
        Set<Long> courierIds = new HashSet<>();
        for (Object[] columns : rows) {
            Long orderId = (Long) columns[0];
            Long courierId = (Long) columns[1];
            LocalTime startTime = ((Time) columns[2]).toLocalTime();
            LocalTime endTime = ((Time) columns[3]).toLocalTime();
            Order order = orderRepository.getReferenceById(orderId);
            Courier courier = courierRepository.getReferenceById(courierId);
            if (assignedOrders.contains(order)) {
                continue;
            }
            if (order.canBeAssigned(courier, date, startTime, endTime)) {
                assignedOrders.add(order);
                courierIds.add(courierId);
            }
        }
        orderRepository.saveAll(assignedOrders);
        return new OrderAssignResponseDTO(date, courierRepository.findAllById(courierIds).stream()
                .map(courier -> entityMapper.toCourierGroupOrdersDTO(courier, date))
                .collect(Collectors.toList()));
    }
}