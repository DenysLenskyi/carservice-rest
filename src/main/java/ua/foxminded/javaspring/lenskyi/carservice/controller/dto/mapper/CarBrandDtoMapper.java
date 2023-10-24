package ua.foxminded.javaspring.lenskyi.carservice.controller.dto.mapper;

import org.mapstruct.Mapper;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarBrandDto;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarBrand;

@Mapper(componentModel = "spring")
public interface CarBrandDtoMapper {

    CarBrandDto carBrandEntityToCarBrandDto(CarBrand carBrand);

    CarBrand carBrandDtoToCarBrandEntity(CarBrandDto carBrandDto);
}
