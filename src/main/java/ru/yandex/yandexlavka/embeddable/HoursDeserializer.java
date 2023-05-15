package ru.yandex.yandexlavka.embeddable;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ru.yandex.yandexlavka.exceptions.BadRequestException;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HoursDeserializer extends JsonDeserializer<Hours> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public Hours deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String hoursString = jsonParser.readValueAs(String.class);
        String[] hours = hoursString.split("-");
        LocalTime startTime = LocalTime.parse(hours[0], formatter);
        LocalTime endTime = LocalTime.parse(hours[1], formatter);
        if (!startTime.isBefore(endTime)) {
            throw new BadRequestException("Start time must be before end time!");
        }
        return new Hours(startTime, endTime);
    }
}
