package ua.foxminded.javaspring.lenskyi.carservice.service;

import org.springframework.data.domain.Page;
import ua.foxminded.javaspring.lenskyi.carservice.model.dto.CarTypeDto;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarType;

import java.util.List;

public interface CarTypeService {
    Page<CarTypeDto> findAllPaginated(Integer pageNumber, Integer pageSize, String sort);

    List<CarTypeDto> findAll();

    CarTypeDto findById(Long id);

    CarTypeDto findByName(String name);

    CarType findCarTypeByName(String name);

    CarTypeDto createCarType(CarTypeDto carTypeDto);

    CarTypeDto updateCarType(CarTypeDto carTypeDto);

    void deleteCarType(Long id);
}
