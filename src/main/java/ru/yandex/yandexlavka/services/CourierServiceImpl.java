package ru.yandex.yandexlavka.services;

import org.springframework.stereotype.Service;
import ru.yandex.yandexlavka.dto.CourierDTO;
import ru.yandex.yandexlavka.dto.request.CreateCourierDTO;
import ru.yandex.yandexlavka.dto.response.CourierMetaInfoResponseDTO;
import ru.yandex.yandexlavka.dto.response.OrderAssignResponseDTO;
import ru.yandex.yandexlavka.entities.Courier;
import ru.yandex.yandexlavka.entities.GroupOrder;
import ru.yandex.yandexlavka.enums.CourierType;
import ru.yandex.yandexlavka.exceptions.NotFoundException;
import ru.yandex.yandexlavka.mappers.EntityMapper;
import ru.yandex.yandexlavka.repositories.CourierRepository;
import ru.yandex.yandexlavka.repositories.GroupOrderRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class CourierServiceImpl implements CourierService {
    private final CourierRepository courierRepository;
    private final GroupOrderRepository groupOrderRepository;
    private final EntityMapper entityMapper;

    public CourierServiceImpl(CourierRepository courierRepository, GroupOrderRepository groupOrderRepository, EntityMapper entityMapper) {
        this.courierRepository = courierRepository;
        this.groupOrderRepository = groupOrderRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public List<CourierDTO> createCouriers(List<CreateCourierDTO> couriers) {
        List<Courier> entities = couriers.stream()
                .map(entityMapper::toEntity)
                .collect(Collectors.toList());
        return courierRepository.saveAll(entities).stream()
                .map(entityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourierDTO> couriersList(Integer limit, Integer offset) {
        return courierRepository.findAllWithLimitOffset(limit, offset).stream()
                .map(entityMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CourierDTO courierInfo(Long id) {
        return entityMapper.toDTO(courierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Courier not found!")));
    }

    @Override
    public CourierMetaInfoResponseDTO courierMetaInfo(Long id, LocalDate startDate, LocalDate endDate) {
        Courier courier = courierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Courier not found!"));
        CourierMetaInfoResponseDTO responseDTO = entityMapper.toCourierMetaInfoResponseDTO(courier);
        List<Integer> costAndCount = courierRepository.findCompletedOrdersCostAndCount(id, startDate, endDate).get(0);
        Integer cost = costAndCount.get(0);
        Integer count = costAndCount.get(1);
        CourierType courierType = courier.getCourierType();
        if (cost != null) {
            responseDTO.setEarnings(cost * courierType.EARNINGS_CONST);
        }
        if (count != null) {
            Integer hours = Math.toIntExact(24 * DAYS.between(startDate, endDate));
            responseDTO.setRating((count / hours) * courierType.RATING_CONST);
        }
        return responseDTO;
    }

    @Override
    public OrderAssignResponseDTO assignmentsInfo(LocalDate date, Long id) {
        List<GroupOrder> groupOrders = id == null ? groupOrderRepository.findAllByDate(date) : groupOrderRepository.findAllByDateAndCourier(date, id);
        List<Courier> couriers = groupOrders.stream().map(GroupOrder::getCourier).distinct().toList();
        return new OrderAssignResponseDTO(date, couriers.stream()
                .map(courier -> entityMapper.toCourierGroupOrdersDTO(courier, date))
                .collect(Collectors.toList()));
    }
}
