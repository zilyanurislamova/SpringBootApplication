package ru.yandex.yandexlavka.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import ru.yandex.yandexlavka.embeddable.Hours;
import ru.yandex.yandexlavka.enums.CourierType;

import java.util.List;

@Data
@Entity
@Table(name = "couriers")
public class Courier {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "couriers_generator")
    @SequenceGenerator(name = "couriers_generator", sequenceName = "couriers_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourierType courierType;

    @ElementCollection
    private List<Integer> regions;

    @ElementCollection
    private List<Hours> workingHours;

    @ToString.Exclude
    @OneToMany(mappedBy = "courier")
    private List<Order> orders;

    @ToString.Exclude
    @OneToMany(mappedBy = "courier")
    private List<GroupOrder> groupOrders;
}
