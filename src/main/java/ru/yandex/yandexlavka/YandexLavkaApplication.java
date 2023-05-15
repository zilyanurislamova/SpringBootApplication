package ru.yandex.yandexlavka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class YandexLavkaApplication {
    public static void main(String[] args) {
        SpringApplication.run(YandexLavkaApplication.class, args);
    }
}
