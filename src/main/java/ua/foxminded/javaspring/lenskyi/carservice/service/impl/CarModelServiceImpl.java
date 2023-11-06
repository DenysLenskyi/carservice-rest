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
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarTypeDto;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.mapper.CarModelDtoMapper;
import ua.foxminded.javaspring.lenskyi.carservice.exception.CarModelNameYearBrandConstraintViolationException;
import ua.foxminded.javaspring.lenskyi.carservice.exception.IdDoesNotExistException;
import ua.foxminded.javaspring.lenskyi.carservice.exception.NameDoesNotExistException;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarBrand;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarModel;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarType;
import ua.foxminded.javaspring.lenskyi.carservice.repository.CarBrandRepository;
import ua.foxminded.javaspring.lenskyi.carservice.repository.CarModelRepository;
import ua.foxminded.javaspring.lenskyi.carservice.repository.CarTypeRepository;
import ua.foxminded.javaspring.lenskyi.carservice.repository.specification.CarModelWithBrand;
import ua.foxminded.javaspring.lenskyi.carservice.repository.specification.CarModelWithName;
import ua.foxminded.javaspring.lenskyi.carservice.repository.specification.CarModelWithType;
import ua.foxminded.javaspring.lenskyi.carservice.repository.specification.CarModelWithYear;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarBrandService;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarModelService;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarTypeService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CarModelServiceImpl implements CarModelService {

    private static final String BRAND_NAME_DOES_NOT_EXIST = "There is no CarBrand with name %s";
    private static final String MODEL_ID_DOES_NOT_EXIST = "There is no CarModel with id %s";
    private static final String CAR_TYPES_NOT_FOUND_BY_NAME = "No CarType found by provided names";
    private static final String MODEL_NAME_YEAR_BRAND_CONSTRAINT_VIOLATION_MESSAGE =
            "This CarModel name, year, Brand already exist";
    private static final Logger LOGGER = LoggerFactory.getLogger(CarModelServiceImpl.class);
    private final CarModelRepository carModelRepository;
    private final CarBrandRepository carBrandRepository;
    private final CarTypeRepository carTypeRepository;
    private CarBrandService carBrandService;
    private CarTypeService carTypeService;
    private CarModelDtoMapper mapper;

    public CarModelServiceImpl(CarModelRepository carModelRepository, CarBrandRepository carBrandRepository,
                               CarTypeRepository carTypeRepository, CarBrandService carBrandService,
                               CarTypeService carTypeService, CarModelDtoMapper mapper) {
        this.carModelRepository = carModelRepository;
        this.carBrandRepository = carBrandRepository;
        this.carTypeRepository = carTypeRepository;
        this.carBrandService = carBrandService;
        this.carTypeService = carTypeService;
        this.mapper = mapper;
    }

    @Override
    public Page<CarModelDto> findAll(Integer pageNumber, Integer pageSize, String sort,
                                     String modelName, Integer year, String brandName, String typeName) {
        if (pageSize == 0) pageSize = carModelRepository.findAll().size();
        Specification<CarModel> spec = Specification.where(new CarModelWithYear(year))
                .and(new CarModelWithName(modelName))
                .and(new CarModelWithBrand(carBrandService.findCarBrandByName(brandName)))
                .and(new CarModelWithType(carTypeService.findCarTypeByName(typeName)));
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
        carModel.setName(carModelDto.getName());
        carModel.setYear(carModelDto.getYear());
        CarBrand carBrand = carBrandRepository.findCarBrandByName(carModelDto.getCarBrandDto().getName())
                .orElseThrow(() -> new NameDoesNotExistException(
                        String.format(BRAND_NAME_DOES_NOT_EXIST, carModelDto.getCarBrandDto().getName())
                ));
        if (carModelRepository.existsByNameAndYearAndCarBrand(carModel.getName(), carModel.getYear(),
                carBrand)) {
            throw new CarModelNameYearBrandConstraintViolationException(
                    MODEL_NAME_YEAR_BRAND_CONSTRAINT_VIOLATION_MESSAGE);
        }
        carModel.setCarBrand(carBrand);
        List<String> carTypeNames = carModelDto.getCarTypeDtos().stream()
                .map(CarTypeDto::getName)
                .toList();
        Set<CarType> carTypes = carTypeRepository.findCarTypeByNameIn(carTypeNames);
        if (carTypes.isEmpty()) throw new NameDoesNotExistException(CAR_TYPES_NOT_FOUND_BY_NAME);
        carModel.setCarTypes(carTypes);
        String carModelId = generateCarModelId();
        while (carModelRepository.existsById(carModelId)) {
            carModelId = generateCarModelId();
        }
        carModel.setId(carModelId);
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
        Optional.ofNullable(carModelDto.getName())
                .ifPresent(carModel::setName);
        Optional.ofNullable(carModelDto.getYear())
                .ifPresent(carModel::setYear);
        if (carModelDto.getCarBrandDto() != null && carModelDto.getCarBrandDto().getName() != null) {
            CarBrand carBrand = carBrandRepository.findCarBrandByName(carModelDto.getCarBrandDto().getName())
                    .orElseThrow(() -> new NameDoesNotExistException(
                            String.format(BRAND_NAME_DOES_NOT_EXIST, carModelDto.getCarBrandDto().getName())
                    ));
            carModel.setCarBrand(carBrand);
        }

        // TODO | if a model exist by updated name, year, brand -> find that model, if id not equal carModel.id then throw constraint violation  exception

        if (carModelDto.getCarTypeDtos() != null && !carModelDto.getCarTypeDtos().isEmpty() &&
                !carModelDto.getCarTypeDtos().contains(null)) {
            List<String> carTypeNames = carModelDto.getCarTypeDtos().stream()
                    .map(CarTypeDto::getName)
                    .toList();
            Set<CarType> carTypes = carTypeRepository.findCarTypeByNameIn(carTypeNames);
            if (carTypes.isEmpty()) throw new NameDoesNotExistException(CAR_TYPES_NOT_FOUND_BY_NAME);
            carModel.setCarTypes(carTypes);
        }
        carModelRepository.saveAndFlush(carModel);
        LOGGER.info("CarModel has been updated");
        return mapper.carModelEntityToCarModelDto(carModel);
    }
}