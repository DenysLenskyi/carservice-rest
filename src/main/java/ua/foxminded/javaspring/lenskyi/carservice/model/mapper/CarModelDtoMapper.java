package ua.foxminded.javaspring.lenskyi.carservice.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.foxminded.javaspring.lenskyi.carservice.model.dto.CarModelDto;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarModel;

@Mapper(componentModel = "spring", uses = {CarBrandDtoMapper.class, CarTypeDtoMapper.class})
public interface CarModelDtoMapper {

    @Mapping(target = "carBrandDto", source = "carBrand")
    @Mapping(target = "carTypeDtos", source = "carTypes")
    CarModelDto carModelEntityToCarModelDto(CarModel carModel);

    CarModel carModelDtoToCarModelEntity(CarModelDto carModelDto);
}