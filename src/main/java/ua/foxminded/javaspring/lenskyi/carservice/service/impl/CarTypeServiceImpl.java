package ua.foxminded.javaspring.lenskyi.carservice.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarTypeDto;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.mapper.CarTypeDtoMapper;
import ua.foxminded.javaspring.lenskyi.carservice.exception.IdDoesNotExistException;
import ua.foxminded.javaspring.lenskyi.carservice.exception.NameDoesNotExistException;
import ua.foxminded.javaspring.lenskyi.carservice.exception.SortingFieldDoesNotExistException;
import ua.foxminded.javaspring.lenskyi.carservice.exception.TheNameIsNotUniqueException;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarType;
import ua.foxminded.javaspring.lenskyi.carservice.repository.CarTypeRepository;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarTypeService;

import java.util.List;

@Service
public class CarTypeServiceImpl implements CarTypeService {

    private static final String ID_DOES_NOT_EXIST_ERROR_MESSAGE = "There is no CarType with id=";
    private static final String NAME_DOES_NOT_EXIST_ERROR_MESSAGE = "There is no CarType with name=";
    private static final String ERROR_NOT_UNIQUE_CAR_TYPE_NAME = "Error. Not unique CarType name=";
    private static final String FIELD_DOES_NOT_EXIST_ERROR_MESSAGE = "Error. A CarType has no field=";
    private final CarTypeRepository carTypeRepository;
    private final CarTypeDtoMapper mapper;

    public CarTypeServiceImpl(CarTypeRepository carTypeRepository, CarTypeDtoMapper mapper) {
        this.carTypeRepository = carTypeRepository;
        this.mapper = mapper;
    }

    @Override
    public Page<CarTypeDto> findAllPaginated(Integer pageNumber, Integer pageSize, String sort) {
        List<CarType> carTypeList = carTypeRepository.findAll();
        if (pageSize == 0) pageSize = carTypeList.size();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, sort);
        Page<CarType> carTypePage;
        try {
            carTypePage = convertListToPage(carTypeList, pageable);
        } catch (PropertyReferenceException e) {
            throw new SortingFieldDoesNotExistException(FIELD_DOES_NOT_EXIST_ERROR_MESSAGE + sort);
        }
        List<CarTypeDto> carTypeDtoList = carTypePage.getContent().stream()
                .map(mapper::carTypeEntityToCarTypeDto)
                .toList();
        return new PageImpl<>(carTypeDtoList, PageRequest.of(
                carTypePage.getNumber(), carTypePage.getSize()), carTypePage.getTotalElements());
    }

    private Page<CarType> convertListToPage(List<CarType> carTypeList, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), carTypeList.size());
        return new PageImpl<>(carTypeList.subList(start, end), pageable, carTypeList.size());
    }

    @Override
    public List<CarTypeDto> findAll() {
        List<CarType> carBrands = carTypeRepository.findAll();
        return carBrands.stream()
                .map(mapper::carTypeEntityToCarTypeDto)
                .toList();
    }

    @Override
    public CarTypeDto findById(Long id) {
        return carTypeRepository.findById(id)
                .map(mapper::carTypeEntityToCarTypeDto)
                .orElseThrow(() -> new IdDoesNotExistException(ID_DOES_NOT_EXIST_ERROR_MESSAGE + id));
    }

    @Override
    public CarTypeDto findByName(String name) {
        return carTypeRepository.findCarTypeByName(name)
                .map(mapper::carTypeEntityToCarTypeDto)
                .orElseThrow(() -> new NameDoesNotExistException(NAME_DOES_NOT_EXIST_ERROR_MESSAGE + name));
    }

    @Override
    @Transactional
    public CarTypeDto createCarType(CarTypeDto carTypeDto) {
        if (carTypeRepository.existsByName(carTypeDto.getName())) {
            throw new TheNameIsNotUniqueException(ERROR_NOT_UNIQUE_CAR_TYPE_NAME + carTypeDto.getName());
        }
        CarType carType = new CarType();
        carType.setName(carTypeDto.getName());
        carTypeRepository.saveAndFlush(carType);
        return mapper.carTypeEntityToCarTypeDto(carType);
    }

    @Override
    @Transactional
    public CarTypeDto updateCarType(CarTypeDto carTypeDto) {
        CarType carType = carTypeRepository.findById(carTypeDto.getId())
                .orElseThrow(() -> new IdDoesNotExistException(ID_DOES_NOT_EXIST_ERROR_MESSAGE + carTypeDto.getId()));
        if (carTypeRepository.existsByName(carTypeDto.getName()) && (!carType.getName().equals(carTypeDto.getName()))) {
            throw new TheNameIsNotUniqueException(ERROR_NOT_UNIQUE_CAR_TYPE_NAME + carTypeDto.getName());
        }
        carType.setName(carTypeDto.getName());
        carTypeRepository.saveAndFlush(carType);
        return mapper.carTypeEntityToCarTypeDto(carType);
    }

    @Override
    @Transactional
    public void deleteCarType(Long id) {
        if (!carTypeRepository.existsById(id)) {
            throw new IdDoesNotExistException(ID_DOES_NOT_EXIST_ERROR_MESSAGE + id);
        }
        carTypeRepository.deleteById(id);
    }
}