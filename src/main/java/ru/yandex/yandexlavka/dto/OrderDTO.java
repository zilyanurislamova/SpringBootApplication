package ru.yandex.yandexlavka.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.yandex.yandexlavka.embeddable.Hours;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
public class OrderDTO implements Serializable {
    @NotNull
    private Long orderId;

    @NotNull
    private Double weight;

    @NotNull
    private Integer regions;

    @NotNull
    private List<Hours> deliveryHours;

    @NotNull
    private Integer cost;

    private Instant completedTime;
}
