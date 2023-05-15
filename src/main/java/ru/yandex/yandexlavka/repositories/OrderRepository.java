package ru.yandex.yandexlavka.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.yandexlavka.entities.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "SELECT * FROM orders ORDER BY id LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Order> findAllWithLimitOffset(@Param("limit") Integer limit,
                                       @Param("offset") Integer offset);

    @Query(value = "SELECT orders.id AS order_id, " +
            "couriers.id AS courier_id, " +
            "GREATEST(order_delivery_hours.start_time, courier_working_hours.start_time) AS start_time, " +
            "LEAST(order_delivery_hours.end_time, courier_working_hours.end_time) AS end_time " +
            "FROM orders " +
            "JOIN courier_regions ON orders.regions = courier_regions.regions " +
            "JOIN couriers ON couriers.id = courier_regions.courier_id " +
            "JOIN order_delivery_hours ON orders.id = order_delivery_hours.order_id " +
            "JOIN courier_working_hours ON courier_regions.courier_id = courier_working_hours.courier_id " +
            "WHERE orders.courier_id IS NULL " +
            "AND NOT (order_delivery_hours.end_time < courier_working_hours.start_time " +
            "OR courier_working_hours.end_time < order_delivery_hours.start_time) " +
            "ORDER BY start_time, cost, weight DESC, courier_type", nativeQuery = true)
    List<Object[]> findAllUnassignedOrdersAndMatchingCouriers();
}
