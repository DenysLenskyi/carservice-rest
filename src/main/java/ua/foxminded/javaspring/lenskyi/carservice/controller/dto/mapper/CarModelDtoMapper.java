package ua.foxminded.javaspring.lenskyi.carservice.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarModelDto;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarModel;

@Mapper(componentModel = "spring", uses = {BrandDtoMapper.class, CarTypeDtoMapper.class})
public interface CarModelDtoMapper {

    @Mapping(target = "brandDto", source = "brand")
    @Mapping(target = "carTypeDtos", source = "carTypes")
    CarModelDto carModelEntityToCarModelDto(CarModel carModel);

    CarModel carModelDtoToCarModelEntity(CarModelDto carModelDto);
}