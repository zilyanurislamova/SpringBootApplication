package ru.yandex.yandexlavka.dto.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderAssignResponseDTO {
    @NotNull
    private LocalDate date;

    @NotNull
    private List<@Valid CourierGroupOrdersDTO> couriers;
}
