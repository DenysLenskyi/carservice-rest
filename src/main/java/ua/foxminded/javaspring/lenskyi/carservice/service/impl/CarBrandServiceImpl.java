package ua.foxminded.javaspring.lenskyi.carservice.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarBrandDto;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.mapper.CarBrandDtoMapper;
import ua.foxminded.javaspring.lenskyi.carservice.exception.IdDoesNotExistException;
import ua.foxminded.javaspring.lenskyi.carservice.exception.NameDoesNotExistException;
import ua.foxminded.javaspring.lenskyi.carservice.exception.SortingFieldDoesNotExistException;
import ua.foxminded.javaspring.lenskyi.carservice.exception.TheNameIsNotUniqueException;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarBrand;
import ua.foxminded.javaspring.lenskyi.carservice.repository.CarBrandRepository;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarBrandService;

import java.util.List;

@Service
public class CarBrandServiceImpl implements CarBrandService {

    private static final String ID_DOES_NOT_EXIST_ERROR_MESSAGE = "There is no CarBrand with id=";
    private static final String NAME_DOES_NOT_EXIST_ERROR_MESSAGE = "There is no CarBrand with name=";
    private static final String NOT_UNIQUE_BRAND_NAME_ERROR_MESSAGE = "Error. Not unique CarBrand name=";
    private static final String FIELD_DOES_NOT_EXIST_ERROR_MESSAGE = "Error. A Brand has no field=";
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
                .orElseThrow(() -> new IdDoesNotExistException(ID_DOES_NOT_EXIST_ERROR_MESSAGE + id));
    }

    @Override
    public CarBrandDto findByName(String name) {
        return carBrandRepository.findCarBrandByName(name)
                .map(mapper::carBrandEntityToCarBrandDto)
                .orElseThrow(() -> new NameDoesNotExistException(NAME_DOES_NOT_EXIST_ERROR_MESSAGE + name));
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
        return mapper.carBrandEntityToCarBrandDto(carBrand);
    }

    @Override
    @Transactional
    public CarBrandDto updateBrand(CarBrandDto carBrandDto) {
        CarBrand carBrand = carBrandRepository.findById(carBrandDto.getId())
                .orElseThrow(() -> new IdDoesNotExistException(ID_DOES_NOT_EXIST_ERROR_MESSAGE + carBrandDto.getId()));
        if (carBrandRepository.existsByName(carBrandDto.getName()) && (!carBrand.getName().equals(carBrandDto.getName()))) {
            throw new TheNameIsNotUniqueException(NOT_UNIQUE_BRAND_NAME_ERROR_MESSAGE + carBrandDto.getName());
        }
        carBrand.setName(carBrandDto.getName());
        carBrandRepository.saveAndFlush(carBrand);
        return mapper.carBrandEntityToCarBrandDto(carBrand);
    }

    @Override
    @Transactional
    public void deleteBrand(Long id) {
        if (!carBrandRepository.existsById(id)) {
            throw new IdDoesNotExistException(ID_DOES_NOT_EXIST_ERROR_MESSAGE + id);
        }
        carBrandRepository.deleteById(id);
    }

    @Override
    public Page<CarBrandDto> findAllPaginated(Integer pageNumber, Integer pageSize, String sort) {
        Pageable pageable;
        if (pageNumber == null) pageNumber = 0;
        if (pageSize == null) pageSize = carBrandRepository.findAll().size();
        if (sort != null) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, sort);
        } else {
            pageable = PageRequest.of(pageNumber, pageSize);
        }
        Page<CarBrand> pageCarBrand;
        try {
            pageCarBrand = carBrandRepository.findAll(pageable);
        } catch (PropertyReferenceException e) {
            throw new SortingFieldDoesNotExistException(FIELD_DOES_NOT_EXIST_ERROR_MESSAGE + sort);
        }
        List<CarBrandDto> carBrandDtoList = pageCarBrand.getContent().stream()
                .map(mapper::carBrandEntityToCarBrandDto)
                .toList();
        return new PageImpl<>(carBrandDtoList, PageRequest.of(
                pageCarBrand.getNumber(), pageCarBrand.getSize()), pageCarBrand.getTotalElements());
    }
}