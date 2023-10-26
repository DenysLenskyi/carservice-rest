package ua.foxminded.javaspring.lenskyi.carservice.service;

import org.springframework.data.domain.Page;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarTypeDto;

public interface CarTypeService {
    Page<CarTypeDto> findAllPaginated(Integer pageNumber, Integer pageSize, String sort);
}
