package ru.yandex.yandexlavka.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@Table(name = "group_orders")
public class GroupOrder {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_orders_generator")
    @SequenceGenerator(name = "group_orders_generator", sequenceName = "group_orders_seq", allocationSize = 1)
    private Long id;

    @ToString.Exclude
    @OneToMany(mappedBy = "groupOrder")
    private List<Order> orders;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private Courier courier;

    @Column
    private LocalDate date;

    @Column
    private LocalTime startTime;

    @Column
    private LocalTime endTime;
}
