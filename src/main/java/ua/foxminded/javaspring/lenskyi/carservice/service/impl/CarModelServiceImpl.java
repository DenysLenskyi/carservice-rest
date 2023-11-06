package ua.foxminded.javaspring.lenskyi.carservice.service.impl;

import jakarta.transaction.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
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
import ua.foxminded.javaspring.lenskyi.carservice.exception.CarModelNameYearBrandConstraintViolationException;
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

    private static final String BRAND_ID_DOES_NOT_EXIST = "There is no CarBrand with id %d";
    private static final String TYPE_ID_DOES_NOT_EXIST = "There is no CarType with id %d";
    private static final String MODEL_ID_DOES_NOT_EXIST = "There is no CarModel with id %s";
    private static final String MODEL_NAME_YEAR_BRAND_CONSTRAINT_VIOLATION_MESSAGE =
            "This CarModel name, year, Brand already exist";
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
                        String.format(BRAND_ID_DOES_NOT_EXIST, carModelDto.getCarBrandDto().getId())
                ));
        if (carModelRepository.existsByNameAndYearAndCarBrand(carModel.getName(), carModel.getYear(),
                carBrand)) {
            throw new CarModelNameYearBrandConstraintViolationException(
                    MODEL_NAME_YEAR_BRAND_CONSTRAINT_VIOLATION_MESSAGE);
        }
        carModel.setCarBrand(carBrand);
        Set<CarType> carTypes = carModelDto.getCarTypeDtos().stream()
                .map(carTypeDto -> carTypeRepository.findById(carTypeDto.getId())
                        .orElseThrow(() -> new IdDoesNotExistException(
                                String.format(TYPE_ID_DOES_NOT_EXIST, carTypeDto.getId())
                        )))
                .collect(Collectors.toSet());
        carModel.setCarTypes(carTypes);
        carModelRepository.saveAndFlush(carModel);
        LOGGER.info("CarModel has been created");
        return mapper.carModelEntityToCarModelDto(carModel);
    }

    private String generateCarModelId() {
        LOGGER.info("Generating CarModel id");
        String id = RandomStringUtils.randomAlphanumeric(10);
        LOGGER.info("CarModel id has been generated");
        return id;
    }

    @Override
    @Transactional
    public void deleteCarModel(String id) {
        carModelRepository.deleteById(id);
        LOGGER.info("CarModel deleted; id = {}", id);
    }

    @Override
    @Transactional
    public CarModelDto updateCarModel(CarModelDto carModelDto) {
        LOGGER.info("Updating CarModel");
        CarModel carModel = carModelRepository.findById(carModelDto.getId())
                .orElseThrow(() -> new IdDoesNotExistException(
                        String.format(MODEL_ID_DOES_NOT_EXIST, carModelDto.getId())
                ));
        if (carModelDto.getName() != null) {
            carModel.setName(carModelDto.getName());
        }
        if (carModelDto.getYear() != null) {
            carModel.setYear(carModelDto.getYear());
        }
        if (carModelDto.getCarBrandDto() != null && carModelDto.getCarBrandDto().getId() != null) {
            CarBrand carBrand = carBrandRepository.findById(carModelDto.getCarBrandDto().getId())
                    .orElseThrow(() -> new IdDoesNotExistException(
                            String.format(BRAND_ID_DOES_NOT_EXIST, carModelDto.getCarBrandDto().getId())
                    ));
            carModel.setCarBrand(carBrand);
        }

        // TODO | if a model exist by updated name, year, brand -> find that model, if id not equal carModel.id then throw constraint violation  exception

        if (carModelDto.getCarTypeDtos() != null && !carModelDto.getCarTypeDtos().isEmpty() &&
                !carModelDto.getCarTypeDtos().contains(null)) {
            Set<CarType> carTypes = carModelDto.getCarTypeDtos().stream()
                    .map(carTypeDto -> carTypeRepository.findById(carTypeDto.getId())
                            .orElseThrow(() -> new IdDoesNotExistException(
                                    String.format(TYPE_ID_DOES_NOT_EXIST, carTypeDto.getId())
                            )))
                    .collect(Collectors.toSet());
            carModel.setCarTypes(carTypes);
        }
        carModelRepository.saveAndFlush(carModel);
        LOGGER.info("CarModel has been updated");
        return mapper.carModelEntityToCarModelDto(carModel);
    }
}