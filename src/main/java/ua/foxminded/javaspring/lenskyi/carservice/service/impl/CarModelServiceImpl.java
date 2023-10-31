package ua.foxminded.javaspring.lenskyi.carservice.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarModelDto;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.mapper.CarModelDtoMapper;
import ua.foxminded.javaspring.lenskyi.carservice.exception.IdDoesNotExistException;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarBrand;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarModel;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarType;
import ua.foxminded.javaspring.lenskyi.carservice.repository.CarBrandRepository;
import ua.foxminded.javaspring.lenskyi.carservice.repository.CarModelRepository;
import ua.foxminded.javaspring.lenskyi.carservice.repository.CarTypeRepository;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarModelService;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CarModelServiceImpl implements CarModelService {

    private static final String BRAND_ID_DOES_NOT_EXIST = "There is no CarBrand with id=";
    private static final String TYPE_ID_DOES_NOT_EXIST = "There is no CarType with id=";
    private static final Logger LOGGER = LoggerFactory.getLogger(CarModelServiceImpl.class);
    private final CarModelRepository carModelRepository;
    private final CarBrandRepository carBrandRepository;
    private final CarTypeRepository carTypeRepository;
    private CarModelDtoMapper mapper;

    public CarModelServiceImpl(CarModelRepository carModelRepository, CarBrandRepository carBrandRepository,
                               CarTypeRepository carTypeRepository, CarModelDtoMapper mapper) {
        this.carModelRepository = carModelRepository;
        this.carBrandRepository = carBrandRepository;
        this.carTypeRepository = carTypeRepository;
        this.mapper = mapper;
    }

    @Override
    public Page<CarModelDto> findAll(Integer pageNumber, Integer pageSize, String sort, Specification<CarModel> spec) {
        if (pageSize == 0) pageSize = carModelRepository.findAll().size();
        Page<CarModel> carModelPage = carModelRepository.findAll(spec, PageRequest.of(pageNumber, pageSize, Sort.by(sort)));
        List<CarModelDto> carModelDtoList = carModelPage.getContent().stream()
                .map(mapper::carModelEntityToCarModelDto)
                .toList();
        return new PageImpl<>(carModelDtoList, PageRequest.of(
                carModelPage.getNumber(), carModelPage.getSize(), carModelPage.getSort()), carModelPage.getTotalElements());
    }

    @Override
    @Transactional
    public CarModelDto createCarModel(CarModelDto carModelDto) {
        LOGGER.info("Creating new CarModel");
        CarModel carModel = new CarModel();
        String carModelId = generateCarModelId();
        while (carModelRepository.existsById(carModelId)) {
            carModelId = generateCarModelId();
        }
        carModel.setId(carModelId);
        carModel.setName(carModelDto.getName());
        carModel.setYear(carModelDto.getYear());
        CarBrand carBrand = carBrandRepository.findById(carModelDto.getCarBrandDto().getId())
                .orElseThrow(() -> new IdDoesNotExistException(
                        BRAND_ID_DOES_NOT_EXIST + carModelDto.getCarBrandDto().getId()
                ));
        carModel.setCarBrand(carBrand);
        Set<CarType> carTypes = carModelDto.getCarTypeDtos().stream()
                .map(carTypeDto -> carTypeRepository.findById(carTypeDto.getId())
                        .orElseThrow(() -> new IdDoesNotExistException(
                                TYPE_ID_DOES_NOT_EXIST + carTypeDto.getId()
                        )))
                .collect(Collectors.toSet());
        carModel.setCarTypes(carTypes);
        carModelRepository.saveAndFlush(carModel);
        return mapper.carModelEntityToCarModelDto(carModel);
    }

    private String generateCarModelId() {
        LOGGER.info("Generating CarModel id");
        final String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        final int length = 10;
        StringBuilder carModelId = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            carModelId.append(randomChar);
        }
        LOGGER.info("CarModel id has been generated");
        return carModelId.toString();
    }

    @Override
    @Transactional
    public void deleteCarModel(String id) {
        carModelRepository.deleteById(id);
        LOGGER.info("CarModel deleted; id = {}", id);
    }
}