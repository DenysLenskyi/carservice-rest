package ua.foxminded.javaspring.lenskyi.carservice.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarBrandDto;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.mapper.CarBrandDtoMapper;
import ua.foxminded.javaspring.lenskyi.carservice.exception.IdDoesNotExistException;
import ua.foxminded.javaspring.lenskyi.carservice.exception.TheNameIsNotUniqueException;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarBrand;
import ua.foxminded.javaspring.lenskyi.carservice.repository.CarBrandRepository;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarBrandService;

import java.util.List;

@Service
public class CarBrandServiceImpl implements CarBrandService {

    private static final String ID_DOES_NOT_EXIST_ERROR_MESSAGE = "There is no CarBrand with id=";
    private static final String NOT_UNIQUE_BRAND_NAME_ERROR_MESSAGE = "Error. Not unique CarBrand name=";
    private final CarBrandRepository carBrandRepository;
    private CarBrandDtoMapper mapper;

    public CarBrandServiceImpl(CarBrandRepository carBrandRepository, CarBrandDtoMapper mapper) {
        this.carBrandRepository = carBrandRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CarBrandDto> findAll() {
        List<CarBrand> carBrands = carBrandRepository.findAll();
        return carBrands.stream()
                .map(mapper::brandEntityToBrandDto)
                .toList();
    }

    @Override
    public CarBrandDto findById(Long id) {
        if (!carBrandRepository.existsById(id)) {
            throw new IdDoesNotExistException(ID_DOES_NOT_EXIST_ERROR_MESSAGE + id);
        }
        return mapper.brandEntityToBrandDto(carBrandRepository.findById(id).orElseThrow(IllegalArgumentException::new));
    }

    @Override
    @Transactional
    public CarBrandDto createBrand(CarBrandDto carBrandDto) {
        if (carBrandRepository.existsByName(carBrandDto.getName())) {
            throw new TheNameIsNotUniqueException(NOT_UNIQUE_BRAND_NAME_ERROR_MESSAGE + carBrandDto.getName());
        }
        CarBrand carBrand = new CarBrand();
        carBrand.setName(carBrandDto.getName());
        carBrandRepository.saveAndFlush(carBrand);
        return mapper.brandEntityToBrandDto(carBrand);
    }

    @Override
    @Transactional
    public CarBrandDto updateBrand(Long id, CarBrandDto carBrandDto) {
        if (!carBrandRepository.existsById(id)) {
            throw new IdDoesNotExistException(ID_DOES_NOT_EXIST_ERROR_MESSAGE + id);
        }
        CarBrand carBrand = carBrandRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        if (carBrandRepository.existsByName(carBrandDto.getName()) && (!carBrand.getName().equals(carBrandDto.getName()))) {
            throw new TheNameIsNotUniqueException(NOT_UNIQUE_BRAND_NAME_ERROR_MESSAGE + carBrandDto.getName());
        }
        carBrand.setName(carBrandDto.getName());
        carBrandRepository.saveAndFlush(carBrand);
        return mapper.brandEntityToBrandDto(carBrand);
    }

    @Override
    @Transactional
    public void deleteBrand(Long id) {
        if (!carBrandRepository.existsById(id)) {
            throw new IdDoesNotExistException(ID_DOES_NOT_EXIST_ERROR_MESSAGE + id);
        }
        carBrandRepository.deleteById(id);
    }
}