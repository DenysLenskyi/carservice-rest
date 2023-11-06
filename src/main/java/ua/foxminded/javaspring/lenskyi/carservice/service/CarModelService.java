package ua.foxminded.javaspring.lenskyi.carservice.service;

import org.springframework.data.domain.Page;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarModelDto;

public interface CarModelService {

    Page<CarModelDto> findAll(Integer pageNumber, Integer pageSize, String sort,
                              String modelName, Integer year, String brandName, String typeName);

    CarModelDto createCarModel(CarModelDto carModelDto);

    void deleteCarModel(String id);

    CarModelDto updateCarModel(CarModelDto carModelDto);
}
