package ua.foxminded.javaspring.lenskyi.carservice.controller.dto.mapper;

import org.mapstruct.Mapper;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.BrandDto;
import ua.foxminded.javaspring.lenskyi.carservice.model.Brand;

@Mapper(componentModel = "spring")
public interface BrandDtoMapper {

    BrandDto brandEntityToBrandDto(Brand brand);

    Brand brandDtoToBrandEntity(BrandDto brandDto);
}
