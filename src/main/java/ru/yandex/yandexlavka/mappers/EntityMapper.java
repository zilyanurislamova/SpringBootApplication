package ru.yandex.yandexlavka.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.yandex.yandexlavka.dto.CourierDTO;
import ru.yandex.yandexlavka.dto.GroupOrderDTO;
import ru.yandex.yandexlavka.dto.OrderDTO;
import ru.yandex.yandexlavka.dto.request.CreateCourierDTO;
import ru.yandex.yandexlavka.dto.request.CreateOrderDTO;
import ru.yandex.yandexlavka.dto.response.CourierGroupOrdersDTO;
import ru.yandex.yandexlavka.dto.response.CourierMetaInfoResponseDTO;
import ru.yandex.yandexlavka.entities.Courier;
import ru.yandex.yandexlavka.entities.GroupOrder;
import ru.yandex.yandexlavka.entities.Order;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EntityMapper {
    Order toEntity(CreateOrderDTO dto);

    Courier toEntity(CreateCourierDTO dto);

    @Mapping(target = "orderId", source = "entity.id")
    OrderDTO toDTO(Order entity);

    @Mapping(target = "courierId", source = "entity.id")
    CourierDTO toDTO(Courier entity);

    @Mapping(target = "groupOrderId", source = "entity.id")
    GroupOrderDTO toDTO(GroupOrder entity);

    @Mapping(target = "courierId", source = "entity.id")
    CourierMetaInfoResponseDTO toCourierMetaInfoResponseDTO(Courier entity);

    default CourierGroupOrdersDTO toCourierGroupOrdersDTO(Courier entity, LocalDate date) {
        return new CourierGroupOrdersDTO(entity.getId(), entity.getGroupOrders().stream()
                .filter(groupOrder -> groupOrder.getDate().equals(date))
                .map(this::toDTO)
                .collect(Collectors.toList()));
    }
}
