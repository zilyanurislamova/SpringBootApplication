package ru.yandex.yandexlavka.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.yandexlavka.dto.CourierDTO;
import ru.yandex.yandexlavka.dto.request.CreateCourierRequestDTO;
import ru.yandex.yandexlavka.dto.response.CourierMetaInfoResponseDTO;
import ru.yandex.yandexlavka.dto.response.CouriersResponseDTO;
import ru.yandex.yandexlavka.dto.response.CreateCourierResponseDTO;
import ru.yandex.yandexlavka.dto.response.OrderAssignResponseDTO;
import ru.yandex.yandexlavka.exceptions.BadRequestException;
import ru.yandex.yandexlavka.services.CourierService;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("couriers")
public class CourierController {
    private final CourierService courierService;

    public CourierController(CourierService courierService) {
        this.courierService = courierService;
    }

    @PostMapping
    public CreateCourierResponseDTO createCouriers(@RequestBody @Valid CreateCourierRequestDTO request) {
        List<CourierDTO> couriers = courierService.createCouriers(request.getCouriers());
        return new CreateCourierResponseDTO(couriers);
    }

    @GetMapping
    public CouriersResponseDTO couriersList(@RequestParam(required = false, defaultValue = "1") @Positive Integer limit,
                                            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer offset) {
        List<CourierDTO> couriers = courierService.couriersList(limit, offset);
        return new CouriersResponseDTO(couriers, limit, offset);
    }

    @GetMapping(path = "{courier_id}")
    public CourierDTO courierInfo(@PathVariable(name = "courier_id") @Positive Long id) {
        return courierService.courierInfo(id);
    }

    @GetMapping(path = "meta-info/{courier_id}")
    public CourierMetaInfoResponseDTO courierMetaInfo(@PathVariable(name = "courier_id") @Positive Long id,
                                                      @RequestParam(name = "start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PastOrPresent LocalDate startDate,
                                                      @RequestParam(name = "end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (!startDate.isBefore(endDate)) {
            throw new BadRequestException("Start date must be before end date!");
        }
        if (endDate.isAfter(LocalDate.now().plusDays(1))) {
            throw new BadRequestException("End date must not be later than tomorrow!");
        }
        return courierService.courierMetaInfo(id, startDate, endDate);
    }

    @GetMapping(path = "assignments")
    public OrderAssignResponseDTO assignmentsInfo(@RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PastOrPresent LocalDate date,
                                                  @RequestParam(required = false, name = "courier_id") @Positive Long id) {
        return courierService.assignmentsInfo(date, id);
    }
}
