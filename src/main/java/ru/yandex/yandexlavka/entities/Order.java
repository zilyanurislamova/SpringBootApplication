package ru.yandex.yandexlavka.entities;

import jakarta.persistence.*;
import lombok.Data;
import ru.yandex.yandexlavka.embeddable.Hours;
import ru.yandex.yandexlavka.enums.CourierType;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_generator")
    @SequenceGenerator(name = "orders_generator", sequenceName = "orders_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private Integer regions;

    @ElementCollection
    private List<Hours> deliveryHours;

    @Column(nullable = false)
    private Integer cost;

    @Column
    private Instant completedTime;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Courier courier;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private GroupOrder groupOrder;

    @Column
    private LocalTime assignedStartTime;

    @Column
    private LocalTime assignedEndTime;

    public boolean canBeAssigned(Courier courier, LocalDate date, LocalTime startTime, LocalTime endTime) {
        CourierType courierType = courier.getCourierType();
        List<GroupOrder> groupOrders = courier.getGroupOrders();
        List<GroupOrder> groupOrdersByDate = groupOrders.stream()
                .filter(groupOrder -> groupOrder.getDate().equals(date))
                .toList();
        GroupOrder groupOrder = groupOrdersByDate.isEmpty() ? new GroupOrder() : groupOrdersByDate.get(groupOrdersByDate.size() - 1);
        List<Order> orders = groupOrder.getOrders() == null ? new ArrayList<>() : groupOrder.getOrders();
        boolean assignable = false;
        if (orders.isEmpty()) {
            if (canBeFirstInNewGroupOrder(courierType, startTime, endTime)) {
                assignedStartTime = startTime;
                assignedEndTime = assignedStartTime.plusMinutes(courierType.FIRST_ORDER_MINUTES);
                assignable = true;
            }
        } else {
            if (canBeNextInExistingGroupOrder(courierType, groupOrder, startTime, endTime)) {
                assignedStartTime = groupOrder.getEndTime();
                assignedEndTime = assignedStartTime.plusMinutes(courierType.NEXT_ORDERS_MINUTES);
                assignable = true;
            } else if (canBeFirstInNewGroupOrderConsideringExisting(courierType, groupOrder, startTime, endTime)) {
                assignedStartTime = groupOrder.getEndTime();
                assignedEndTime = assignedStartTime.plusMinutes(courierType.FIRST_ORDER_MINUTES);
                groupOrder = new GroupOrder();
                orders = new ArrayList<>();
                assignable = true;
            } else if (canBeFirstInNewGroupOrder(courierType, startTime, endTime)) {
                assignedStartTime = startTime;
                assignedEndTime = assignedStartTime.plusMinutes(courierType.FIRST_ORDER_MINUTES);
                groupOrder = new GroupOrder();
                orders = new ArrayList<>();
                assignable = true;
            }
        }
        if (assignable) {
            this.courier = courier;
            this.groupOrder = groupOrder;
            orders.add(this);
            groupOrder.setEndTime(assignedEndTime);
            if (!groupOrders.contains(groupOrder)) {
                groupOrder.setDate(date);
                groupOrder.setOrders(orders);
                groupOrder.setCourier(courier);
                groupOrder.setStartTime(assignedStartTime);
                groupOrders.add(groupOrder);
            }
            return true;
        }
        return false;
    }

    private boolean canBeFirstInNewGroupOrder(CourierType courierType, LocalTime startTime, LocalTime endTime) {
        return !startTime.plusMinutes(courierType.FIRST_ORDER_MINUTES).isAfter(endTime) &&
                weight <= courierType.MAX_WEIGHT;
    }

    private boolean canBeNextInExistingGroupOrder(CourierType courierType, GroupOrder groupOrder, LocalTime startTime, LocalTime endTime) {
        return groupOrder.getOrders().size() + 1 <= courierType.MAX_ORDERS &&
                !groupOrder.getEndTime().isBefore(startTime) &&
                !groupOrder.getEndTime().plusMinutes(courierType.NEXT_ORDERS_MINUTES).isAfter(endTime) &&
                groupOrder.getOrders().stream()
                        .mapToDouble(Order::getWeight)
                        .sum() + weight <= courierType.MAX_WEIGHT &&
                Stream.concat(groupOrder.getOrders().stream().map(Order::getRegions), Stream.of(regions))
                        .distinct()
                        .count() <= courierType.MAX_REGIONS;
    }

    private boolean canBeFirstInNewGroupOrderConsideringExisting(CourierType courierType, GroupOrder groupOrder, LocalTime startTime, LocalTime endTime) {
        return !groupOrder.getEndTime().isBefore(startTime) &&
                !groupOrder.getEndTime().plusMinutes(courierType.FIRST_ORDER_MINUTES).isAfter(endTime) &&
                weight <= courierType.MAX_WEIGHT;
    }
}
