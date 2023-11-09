package ua.foxminded.javaspring.lenskyi.carservice.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ua.foxminded.javaspring.lenskyi.carservice.model.dto.CarBrandDto;
import ua.foxminded.javaspring.lenskyi.carservice.model.mapper.CarBrandDtoMapper;
import ua.foxminded.javaspring.lenskyi.carservice.exception.IdDoesNotExistException;
import ua.foxminded.javaspring.lenskyi.carservice.exception.NameDoesNotExistException;
import ua.foxminded.javaspring.lenskyi.carservice.exception.TheNameIsNotUniqueException;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarBrand;
import ua.foxminded.javaspring.lenskyi.carservice.repository.CarBrandRepository;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarBrandService;

import java.util.List;

@Service
public class CarBrandServiceImpl implements CarBrandService {

    private static final String ID_DOES_NOT_EXIST_ERROR_MESSAGE = "There is no CarBrand with id %d";
    private static final String NAME_DOES_NOT_EXIST_ERROR_MESSAGE = "There is no CarBrand with name %s";
    private static final String NOT_UNIQUE_BRAND_NAME_ERROR_MESSAGE = "Error. Not unique CarBrand name %s";
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
                .map(mapper::carBrandEntityToCarBrandDto)
                .toList();
    }

    @Override
    public CarBrandDto findById(Long id) {
        return carBrandRepository.findById(id)
                .map(mapper::carBrandEntityToCarBrandDto)
                .orElseThrow(() -> new IdDoesNotExistException(String.format(ID_DOES_NOT_EXIST_ERROR_MESSAGE, id)));
    }

    @Override
    public CarBrandDto findByName(String name) {
        return carBrandRepository.findCarBrandByName(name)
                .map(mapper::carBrandEntityToCarBrandDto)
                .orElseThrow(() -> new NameDoesNotExistException(String.format(NAME_DOES_NOT_EXIST_ERROR_MESSAGE, name)));
    }

    @Override
    public CarBrand findCarBrandByName(String name) {
        return carBrandRepository.findCarBrandByName(name)
                .orElse(null);
    }

    @Override
    @Transactional
    public CarBrandDto createCarBrand(CarBrandDto carBrandDto) {
        if (carBrandRepository.existsByName(carBrandDto.getName())) {
            throw new TheNameIsNotUniqueException(String.format(NOT_UNIQUE_BRAND_NAME_ERROR_MESSAGE, carBrandDto.getName()));
        }
        CarBrand carBrand = new CarBrand();
        carBrand.setName(carBrandDto.getName());
        carBrandRepository.saveAndFlush(carBrand);
        return mapper.carBrandEntityToCarBrandDto(carBrand);
    }

    @Override
    @Transactional
    public CarBrandDto updateCarBrand(CarBrandDto carBrandDto) {
        CarBrand carBrand = carBrandRepository.findById(carBrandDto.getId())
                .orElseThrow(() -> new IdDoesNotExistException(String.format(ID_DOES_NOT_EXIST_ERROR_MESSAGE, carBrandDto.getId())));
        if (carBrandRepository.existsByName(carBrandDto.getName()) && (!carBrand.getName().equals(carBrandDto.getName()))) {
            throw new TheNameIsNotUniqueException(String.format(NOT_UNIQUE_BRAND_NAME_ERROR_MESSAGE, carBrandDto.getName()));
        }
        carBrand.setName(carBrandDto.getName());
        carBrandRepository.saveAndFlush(carBrand);
        return mapper.carBrandEntityToCarBrandDto(carBrand);
    }

    @Override
    @Transactional
    public void deleteCarBrand(Long id) {
        if (!carBrandRepository.existsById(id)) {
            throw new IdDoesNotExistException(String.format(ID_DOES_NOT_EXIST_ERROR_MESSAGE, id));
        }
        carBrandRepository.deleteById(id);
    }

    @Override
    public List<CarBrandDto> findAllPaginated(Integer pageNumber, Integer pageSize, String sort) {
        if (pageSize == 0) pageSize = carBrandRepository.findAll().size();
        Page<CarBrand> pageCarBrand = carBrandRepository.findAll(PageRequest.of(
                pageNumber, pageSize, Sort.by(sort)
        ));
        return pageCarBrand.getContent().stream()
                .map(mapper::carBrandEntityToCarBrandDto)
                .toList();
    }
}