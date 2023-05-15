package ru.yandex.yandexlavka.dto.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.yandexlavka.dto.CourierDTO;

import java.util.List;

@Data
@AllArgsConstructor
public class CouriersResponseDTO {
    @NotNull
    private List<@Valid CourierDTO> couriers;

    @NotNull
    private Integer limit;

    @NotNull
    private Integer offset;
}
