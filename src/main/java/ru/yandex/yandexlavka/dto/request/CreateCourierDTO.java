package ru.yandex.yandexlavka.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.yandex.yandexlavka.embeddable.Hours;
import ru.yandex.yandexlavka.enums.CourierType;

import java.util.List;

@Data
public class CreateCourierDTO {
    @NotNull
    private CourierType courierType;

    @NotNull
    private List<@Positive Integer> regions;

    @NotNull
    private List<Hours> workingHours;
}
