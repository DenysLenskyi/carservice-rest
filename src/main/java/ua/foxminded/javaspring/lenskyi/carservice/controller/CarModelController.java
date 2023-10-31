package ua.foxminded.javaspring.lenskyi.carservice.controller;

import jakarta.validation.Valid;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarModelDto;
import ua.foxminded.javaspring.lenskyi.carservice.controller.specification.CarModelWithBrand;
import ua.foxminded.javaspring.lenskyi.carservice.controller.specification.CarModelWithName;
import ua.foxminded.javaspring.lenskyi.carservice.controller.specification.CarModelWithType;
import ua.foxminded.javaspring.lenskyi.carservice.controller.specification.CarModelWithYear;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarModel;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarBrandService;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarModelService;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/model")
public class CarModelController {

    private CarModelService carModelService;
    private CarBrandService carBrandService;
    private CarTypeService carTypeService;

    public CarModelController(CarModelService carModelService, CarBrandService carBrandService,
                              CarTypeService carTypeService) {
        this.carModelService = carModelService;
        this.carBrandService = carBrandService;
        this.carTypeService = carTypeService;
    }

    @GetMapping("/all")
    public List<CarModelDto> findAll(@RequestParam(defaultValue = "0") int pageNumber,
                                     @RequestParam(defaultValue = "0") int pageSize,
                                     @RequestParam(defaultValue = "id") String sort,
                                     @RequestParam(required = false) String modelName,
                                     @RequestParam(required = false) Integer year,
                                     @RequestParam(required = false) String brandName,
                                     @RequestParam(required = false) String typeName) {
        Specification<CarModel> spec = Specification.where(new CarModelWithYear(year))
                .and(new CarModelWithName(modelName))
                .and(new CarModelWithBrand(carBrandService.findCarBrandByName(brandName)))
                .and(new CarModelWithType(carTypeService.findCarTypeByName(typeName)));
        return carModelService.findAll(pageNumber, pageSize, sort, spec).getContent();
    }

    @PostMapping
    public ResponseEntity<CarModelDto> createCarModel(@RequestBody @Valid CarModelDto carModelDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(carModelService.createCarModel(carModelDto));
    }
}