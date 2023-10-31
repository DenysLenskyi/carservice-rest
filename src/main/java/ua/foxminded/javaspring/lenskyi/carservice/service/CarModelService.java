package ua.foxminded.javaspring.lenskyi.carservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarModelDto;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarModel;

public interface CarModelService {

    Page<CarModelDto> findAll(Integer pageNumber, Integer pageSize, String sort, Specification<CarModel> spec);

    CarModelDto createCarModel(CarModelDto carModelDto);

    void deleteCarModel(String id);
}
