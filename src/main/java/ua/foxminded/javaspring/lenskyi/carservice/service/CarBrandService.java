package ua.foxminded.javaspring.lenskyi.carservice.service;

import ua.foxminded.javaspring.lenskyi.carservice.model.dto.CarBrandDto;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarBrand;

import java.util.List;

public interface CarBrandService {
    List<CarBrandDto> findAll();

    CarBrandDto findById(Long id);

    CarBrandDto findByName(String name);

    CarBrand findCarBrandByName(String name);

    CarBrandDto createCarBrand(CarBrandDto carBrandDto);

    CarBrandDto updateCarBrand(CarBrandDto carBrandDto);

    void deleteCarBrand(Long id);

    List<CarBrandDto> findAllPaginated(Integer pageNumber, Integer pageSize, String sort);
}