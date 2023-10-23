package ua.foxminded.javaspring.lenskyi.carservice.service;

import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarBrandDto;

import java.util.List;

public interface CarBrandService {
    List<CarBrandDto> findAll();

    CarBrandDto findById(Long id);

    CarBrandDto createBrand(CarBrandDto carBrandDto);

    CarBrandDto updateBrand(Long id, CarBrandDto carBrandDto);

    void deleteBrand(Long id);
}