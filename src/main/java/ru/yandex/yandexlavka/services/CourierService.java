package ru.yandex.yandexlavka.services;

import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.dto.CourierDTO;
import ru.yandex.yandexlavka.dto.request.CreateCourierDTO;
import ru.yandex.yandexlavka.dto.response.CourierMetaInfoResponseDTO;
import ru.yandex.yandexlavka.dto.response.OrderAssignResponseDTO;

import java.time.LocalDate;
import java.util.List;

@Service
public interface CourierService {
    List<CourierDTO> createCouriers(List<CreateCourierDTO> couriers);

    List<CourierDTO> couriersList(Integer limit, Integer offset);

    CourierDTO courierInfo(Long id);

    CourierMetaInfoResponseDTO courierMetaInfo(Long id, LocalDate startDate, LocalDate endDate);

    OrderAssignResponseDTO assignmentsInfo(LocalDate date, Long id);
}
