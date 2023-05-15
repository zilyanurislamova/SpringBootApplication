package ru.yandex.yandexlavka.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.yandex.yandexlavka.enums.CourierType;
import ru.yandex.yandexlavka.embeddable.Hours;

import java.util.List;

@Data
public class CourierDTO {
    @NotNull
    private Long courierId;

    @NotNull
    private CourierType courierType;

    @NotNull
    private List<Integer> regions;

    @NotNull
    private List<Hours> workingHours;
}
