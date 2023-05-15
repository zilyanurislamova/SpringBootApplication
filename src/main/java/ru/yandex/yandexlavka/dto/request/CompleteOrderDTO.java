package ru.yandex.yandexlavka.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

@Data
public class CompleteOrderDTO {
    @NotNull
    @Positive
    private Long courierId;

    @NotNull
    @Positive
    private Long orderId;

    @NotNull
    @PastOrPresent
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant completeTime;
}
