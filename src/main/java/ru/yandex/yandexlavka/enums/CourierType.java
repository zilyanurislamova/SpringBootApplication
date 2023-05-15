package ru.yandex.yandexlavka.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CourierType {
    FOOT(2, 3, 10.0, 2, 1, 25, 10),
    BIKE(3, 2, 20.0, 4, 2, 12, 8),
    AUTO(4, 1, 40.0, 7, 3, 8, 4);

    public final Integer EARNINGS_CONST;
    public final Integer RATING_CONST;
    public final Double MAX_WEIGHT;
    public final Integer MAX_ORDERS;
    public final Integer MAX_REGIONS;
    public final Integer FIRST_ORDER_MINUTES;
    public final Integer NEXT_ORDERS_MINUTES;
}
