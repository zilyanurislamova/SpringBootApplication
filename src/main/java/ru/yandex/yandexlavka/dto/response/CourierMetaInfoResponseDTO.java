package ru.yandex.yandexlavka.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.yandex.yandexlavka.enums.CourierType;
import ru.yandex.yandexlavka.embeddable.Hours;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourierMetaInfoResponseDTO {
    @NotNull
    private Long courierId;

    @NotNull
    private CourierType courierType;

    @NotNull
    private List<Integer> regions;

    @NotNull
    private List<Hours> workingHours;

    private Integer rating;

    private Integer earnings;
}
