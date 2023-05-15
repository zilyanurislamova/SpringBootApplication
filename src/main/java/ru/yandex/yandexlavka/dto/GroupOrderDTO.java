package ru.yandex.yandexlavka.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class GroupOrderDTO {
    @NotNull
    private Long groupOrderId;

    @NotNull
    private List<OrderDTO> orders;
}
