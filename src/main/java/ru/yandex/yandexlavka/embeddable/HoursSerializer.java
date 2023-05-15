package ru.yandex.yandexlavka.embeddable;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class HoursSerializer extends JsonSerializer<Hours> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void serialize(Hours hours, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String value = hours.getStartTime().format(formatter) + "-" + hours.getEndTime().format(formatter);
        jsonGenerator.writeString(value);
    }
}

