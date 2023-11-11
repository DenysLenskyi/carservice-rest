package ua.foxminded.javaspring.lenskyi.carservice.service;

import ua.foxminded.javaspring.lenskyi.carservice.model.dto.CarModelDto;

import java.util.List;

public interface CarModelService {

    List<CarModelDto> findAll(Integer pageNumber, Integer pageSize, String sort,
                              String modelName, Integer year, String brandName, String typeName);

    CarModelDto createCarModel(CarModelDto carModelDto);

    void deleteCarModel(String id);

    CarModelDto updateCarModel(CarModelDto carModelDto);
}
