package ru.yandex.yandexlavka.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.yandexlavka.entities.Courier;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {
    @Query(value = "SELECT * FROM couriers ORDER BY id LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Courier> findAllWithLimitOffset(@Param("limit") Integer limit,
                                         @Param("offset") Integer offset);

    @Query(value = "SELECT SUM(cost) AS cost, SUM(1) AS count " +
            "FROM couriers JOIN orders ON couriers.id = orders.courier_id " +
            "WHERE courier_id = :id AND completed_time BETWEEN :start AND :end", nativeQuery = true)
    List<List<Integer>> findCompletedOrdersCostAndCount(@Param("id") Long id,
                                                        @Param("start") LocalDate startDate,
                                                        @Param("end") LocalDate endDate);
}
