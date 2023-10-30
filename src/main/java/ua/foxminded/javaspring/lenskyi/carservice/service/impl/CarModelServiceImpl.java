package ua.foxminded.javaspring.lenskyi.carservice.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarModelDto;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.mapper.CarModelDtoMapper;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarModel;
import ua.foxminded.javaspring.lenskyi.carservice.repository.CarModelRepository;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarModelService;

import java.util.List;

@Service
public class CarModelServiceImpl implements CarModelService {

    private final CarModelRepository carModelRepository;
    private CarModelDtoMapper mapper;

    public CarModelServiceImpl(CarModelRepository carModelRepository, CarModelDtoMapper mapper) {
        this.carModelRepository = carModelRepository;
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
}
