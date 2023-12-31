package ua.foxminded.javaspring.lenskyi.carservice.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ua.foxminded.javaspring.lenskyi.carservice.model.dto.CarTypeDto;
import ua.foxminded.javaspring.lenskyi.carservice.model.mapper.CarTypeDtoMapper;
import ua.foxminded.javaspring.lenskyi.carservice.exception.IdDoesNotExistException;
import ua.foxminded.javaspring.lenskyi.carservice.exception.NameDoesNotExistException;
import ua.foxminded.javaspring.lenskyi.carservice.exception.TheNameIsNotUniqueException;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarType;
import ua.foxminded.javaspring.lenskyi.carservice.repository.CarTypeRepository;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarTypeService;

import java.util.List;

@Service
public class CarTypeServiceImpl implements CarTypeService {

    private static final String ID_DOES_NOT_EXIST_ERROR_MESSAGE = "There is no CarType with id %d";
    private static final String NAME_DOES_NOT_EXIST_ERROR_MESSAGE = "There is no CarType with name %s";
    private static final String ERROR_NOT_UNIQUE_CAR_TYPE_NAME = "Error. Not unique CarType name %s";
    private final CarTypeRepository carTypeRepository;
    private final CarTypeDtoMapper mapper;

    public CarTypeServiceImpl(CarTypeRepository carTypeRepository, CarTypeDtoMapper mapper) {
        this.carTypeRepository = carTypeRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CarTypeDto> findAllPaginated(Integer pageNumber, Integer pageSize, String sort) {
        if (pageSize == 0) pageSize = carTypeRepository.findAll().size();
        Page<CarType> carTypePage = carTypeRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sort)));
        return carTypePage.getContent().stream()
                .map(mapper::carTypeEntityToCarTypeDto)
                .toList();
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
                .orElseThrow(() -> new IdDoesNotExistException(String.format(ID_DOES_NOT_EXIST_ERROR_MESSAGE, id)));
    }

    @Override
    public CarTypeDto findByName(String name) {
        return carTypeRepository.findCarTypeByName(name)
                .map(mapper::carTypeEntityToCarTypeDto)
                .orElseThrow(() -> new NameDoesNotExistException(String.format(NAME_DOES_NOT_EXIST_ERROR_MESSAGE, name)));
    }

    @Override
    public CarType findCarTypeByName(String name) {
        return carTypeRepository.findCarTypeByName(name)
                .orElse(null);
    }

    @Override
    @Transactional
    public CarTypeDto createCarType(CarTypeDto carTypeDto) {
        if (carTypeRepository.existsByName(carTypeDto.getName())) {
            throw new TheNameIsNotUniqueException(String.format(ERROR_NOT_UNIQUE_CAR_TYPE_NAME, carTypeDto.getName()));
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
                .orElseThrow(() -> new IdDoesNotExistException(String.format(ID_DOES_NOT_EXIST_ERROR_MESSAGE, carTypeDto.getId())));
        if (carTypeRepository.existsByName(carTypeDto.getName()) && (!carType.getName().equals(carTypeDto.getName()))) {
            throw new TheNameIsNotUniqueException(String.format(ERROR_NOT_UNIQUE_CAR_TYPE_NAME, carTypeDto.getName()));
        }
        carType.setName(carTypeDto.getName());
        carTypeRepository.saveAndFlush(carType);
        return mapper.carTypeEntityToCarTypeDto(carType);
    }

    @Override
    @Transactional
    public void deleteCarType(Long id) {
        if (!carTypeRepository.existsById(id)) {
            throw new IdDoesNotExistException(String.format(ID_DOES_NOT_EXIST_ERROR_MESSAGE, id));
        }
        carTypeRepository.deleteById(id);
    }
}