package ua.foxminded.javaspring.lenskyi.carservice.service;

import org.springframework.data.domain.Page;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarTypeDto;

import java.util.List;

public interface CarTypeService {
    Page<CarTypeDto> findAllPaginated(Integer pageNumber, Integer pageSize, String sort);

    List<CarTypeDto> findAll();

    CarTypeDto findById(Long id);

    CarTypeDto findByName(String name);

    CarTypeDto createCarType(CarTypeDto carTypeDto);

    CarTypeDto updateCarType(CarTypeDto carTypeDto);

    void deleteCarType(Long id);
}
