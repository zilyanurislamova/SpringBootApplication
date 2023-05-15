package ru.yandex.yandexlavka.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.yandex.yandexlavka.embeddable.Hours;

import java.util.List;

@Data
public class CreateOrderDTO {
    @NotNull
    @Positive
    private Double weight;

    @NotNull
    @Positive
    private Integer regions;

    @NotNull
    private List<Hours> deliveryHours;

    @NotNull
    @Positive
    private Integer cost;
}
