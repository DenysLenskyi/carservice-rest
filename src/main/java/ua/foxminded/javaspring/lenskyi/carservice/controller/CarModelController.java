package ua.foxminded.javaspring.lenskyi.carservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.javaspring.lenskyi.carservice.model.dto.CarModelDto;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarModelService;
import ua.foxminded.javaspring.lenskyi.carservice.util.swagger.CarModelOpenApi;

import java.util.List;

@RestController
@RequestMapping("/api/v1/model")
public class CarModelController implements CarModelOpenApi {

        private CarModelService carModelService;

        public CarModelController(CarModelService carModelService) {
                this.carModelService = carModelService;
        }

        @GetMapping("/search")
        public List<CarModelDto> findAll(@RequestParam(defaultValue = "0") int pageNumber,
                                         @RequestParam(defaultValue = "0") int pageSize,
                                         @RequestParam(defaultValue = "id") String sort,
                                         @RequestParam(required = false) String modelName,
                                         @RequestParam(required = false) Integer year,
                                         @RequestParam(required = false) String brandName,
                                         @RequestParam(required = false) String typeName) {
                return carModelService.findAll(pageNumber, pageSize, sort, modelName, year, brandName, typeName);
        }

        @PostMapping
        public ResponseEntity<CarModelDto> createCarModel(@RequestBody @Valid CarModelDto carModelDto) {
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(carModelService.createCarModel(carModelDto));
        }

        @DeleteMapping("/{id}")
        public void deleteCarModel(@PathVariable("id") String id) {
                carModelService.deleteCarModel(id);
        }

        @PutMapping
        public ResponseEntity<CarModelDto> updateCarModel(@RequestBody CarModelDto carModelDto) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(carModelService.updateCarModel(carModelDto));
        }
}