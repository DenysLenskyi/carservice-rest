package ua.foxminded.javaspring.lenskyi.carservice.controller;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarModelDto;
import ua.foxminded.javaspring.lenskyi.carservice.controller.specification.CarModelWithYear;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarModel;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarModelService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/model")
public class CarModelController {

    private CarModelService carModelService;

    public CarModelController(CarModelService carModelService) {
        this.carModelService = carModelService;
    }

    @GetMapping("/all")
    public List<CarModelDto> findAll(@RequestParam(defaultValue = "0") int pageNumber,
                                     @RequestParam(defaultValue = "0") int pageSize,
                                     @RequestParam(defaultValue = "id") String sort,
                                     @RequestParam(required = false) Integer year) {
        Specification<CarModel> spec = Specification.where(new CarModelWithYear(year));
        return carModelService.findAll(pageNumber, pageSize, sort, spec).getContent();
    }
}