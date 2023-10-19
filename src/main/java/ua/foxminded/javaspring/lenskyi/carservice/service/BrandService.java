package ua.foxminded.javaspring.lenskyi.carservice.service;

import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.BrandDto;

import java.util.List;

public interface BrandService {
    List<BrandDto> findAll();

    BrandDto findById(Long id);
}
