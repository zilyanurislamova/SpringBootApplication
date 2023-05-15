package ru.yandex.yandexlavka.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.yandexlavka.entities.GroupOrder;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GroupOrderRepository extends JpaRepository<GroupOrder, Long> {
    @Query(value = "SELECT * FROM group_orders WHERE date = :date", nativeQuery = true)
    List<GroupOrder> findAllByDate(@Param("date") LocalDate date);

    @Query(value = "SELECT * FROM group_orders WHERE date = :date AND courier_id = :id", nativeQuery = true)
    List<GroupOrder> findAllByDateAndCourier(@Param("date") LocalDate date,
                                             @Param("id") Long id);
}
