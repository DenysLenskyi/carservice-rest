package ua.foxminded.javaspring.lenskyi.carservice.controller;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarModelDto;
import ua.foxminded.javaspring.lenskyi.carservice.controller.specification.CarModelWithBrand;
import ua.foxminded.javaspring.lenskyi.carservice.controller.specification.CarModelWithYear;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarModel;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarBrandService;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarModelService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/model")
public class CarModelController {

    private CarModelService carModelService;
    private CarBrandService carBrandService;

    public CarModelController(CarModelService carModelService, CarBrandService carBrandService) {
        this.carModelService = carModelService;
        this.carBrandService = carBrandService;
    }

    @GetMapping("/all")
    public List<CarModelDto> findAll(@RequestParam(defaultValue = "0") int pageNumber,
                                     @RequestParam(defaultValue = "0") int pageSize,
                                     @RequestParam(defaultValue = "id") String sort,
                                     @RequestParam(required = false) Integer year,
                                     @RequestParam(required = false) String brandName) {
        Specification<CarModel> spec = Specification.where(new CarModelWithYear(year))
                .and(new CarModelWithBrand(carBrandService.findCarBrandByName(brandName)));
        return carModelService.findAll(pageNumber, pageSize, sort, spec).getContent();
    }
}