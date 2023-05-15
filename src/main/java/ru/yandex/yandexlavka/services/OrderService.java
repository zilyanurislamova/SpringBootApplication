package ru.yandex.yandexlavka.services;

import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.dto.request.CompleteOrderDTO;
import ru.yandex.yandexlavka.dto.request.CreateOrderDTO;
import ru.yandex.yandexlavka.dto.OrderDTO;
import ru.yandex.yandexlavka.dto.response.OrderAssignResponseDTO;

import java.time.LocalDate;
import java.util.List;

@Service
public interface OrderService {
    List<OrderDTO> createOrders(List<CreateOrderDTO> orders);

    List<OrderDTO> ordersList(Integer limit, Integer offset);

    OrderDTO orderInfo(Long id);

    List<OrderDTO> completeOrders(List<CompleteOrderDTO> completedOrders);

    OrderAssignResponseDTO assignOrders(LocalDate date);
}
