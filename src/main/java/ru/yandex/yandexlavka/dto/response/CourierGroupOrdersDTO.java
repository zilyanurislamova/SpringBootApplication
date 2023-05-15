package ru.yandex.yandexlavka.dto.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.yandexlavka.dto.GroupOrderDTO;

import java.util.List;

@Data
@AllArgsConstructor
public class CourierGroupOrdersDTO {
    @NotNull
    private Long courierId;

    @NotNull
    private List<@Valid GroupOrderDTO> orders;
}
