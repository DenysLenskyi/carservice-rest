package ua.foxminded.javaspring.lenskyi.carservice.service;

import jakarta.transaction.Transactional;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.BrandDto;

import java.util.List;

public interface BrandService {
    List<BrandDto> findAll();

    BrandDto findById(Long id);

    BrandDto createBrand(BrandDto brandDto);

    @Transactional
    BrandDto updateBrand(Long id, BrandDto brandDto);

    @Transactional
    void deleteBrand(Long id);
}