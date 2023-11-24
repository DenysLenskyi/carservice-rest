package ua.foxminded.javaspring.lenskyi.carservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.javaspring.lenskyi.carservice.model.dto.CarTypeDto;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarTypeService;
import ua.foxminded.javaspring.lenskyi.carservice.util.swagger.CarTypeOpenApi;

import java.util.List;

@RestController
@RequestMapping("/api/v1/type")
public class CarTypeController implements CarTypeOpenApi {

    private CarTypeService carTypeService;

    public CarTypeController(CarTypeService carTypeService) {
        this.carTypeService = carTypeService;
    }

    @GetMapping("/all")
    public List<CarTypeDto> findAll(@RequestParam(defaultValue = "0") int pageNumber,
                                    @RequestParam(defaultValue = "0") int pageSize,
                                    @RequestParam(defaultValue = "id") String sort) {
        return carTypeService.findAllPaginated(pageNumber, pageSize, sort);
    }

    @GetMapping("/{id}")
    public CarTypeDto findById(@PathVariable("id") Long id) {
        return carTypeService.findById(id);
    }

    @GetMapping("/by-name/{name}")
    public CarTypeDto findByName(@PathVariable("name") String name) {
        return carTypeService.findByName(name);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CarTypeDto> createCarType(@RequestBody @Valid CarTypeDto carTypeDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(carTypeService.createCarType(carTypeDto));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CarTypeDto> updateCarType(@RequestBody @Valid CarTypeDto carTypeDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(carTypeService.updateCarType(carTypeDto));
    }

    @DeleteMapping("/{id}")
    public void deleteCarType(@PathVariable("id") Long id) {
        carTypeService.deleteCarType(id);
    }
}