package ua.foxminded.javaspring.lenskyi.carservice.controller.dto.mapper;

import org.mapstruct.Mapper;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarTypeDto;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarType;

@Mapper(componentModel = "spring")
public interface CarTypeDtoMapper {

    CarTypeDto carTypeEntityToCarTypeDto(CarType carType);

    CarType carTypeDtoToCarTypeEntity(CarTypeDto carTypeDto);
}
