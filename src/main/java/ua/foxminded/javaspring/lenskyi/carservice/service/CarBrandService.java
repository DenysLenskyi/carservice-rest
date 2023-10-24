package ua.foxminded.javaspring.lenskyi.carservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarBrandDto;

import java.util.List;

public interface CarBrandService {
    List<CarBrandDto> findAll();

    CarBrandDto findById(Long id);

    CarBrandDto findByName(String name);

    CarBrandDto createBrand(CarBrandDto carBrandDto);

    CarBrandDto updateBrand(CarBrandDto carBrandDto);

    void deleteBrand(Long id);

    Page<CarBrandDto> findAllPaginated(Integer pageNumber, Integer pageSize, String sort);
}