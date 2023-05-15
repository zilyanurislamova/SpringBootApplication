package ru.yandex.yandexlavka.embeddable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalTime;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(using = HoursSerializer.class)
@JsonDeserialize(using = HoursDeserializer.class)
public class Hours implements Serializable {
    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;
}
