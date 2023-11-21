package ua.foxminded.javaspring.lenskyi.carservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.javaspring.lenskyi.carservice.model.dto.CarBrandDto;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarBrandService;
import ua.foxminded.javaspring.lenskyi.carservice.util.swagger.CarBrandOpenApi;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brand")
public class CarBrandController implements CarBrandOpenApi {

    private CarBrandService carBrandService;

    public CarBrandController(CarBrandService carBrandService) {
        this.carBrandService = carBrandService;
    }

    @GetMapping("/all")
    public List<CarBrandDto> findAll(@RequestParam(defaultValue = "0") int pageNumber,
                                     @RequestParam(defaultValue = "0") int pageSize,
                                     @RequestParam(defaultValue = "id") String sort) {
        return carBrandService.findAllPaginated(pageNumber, pageSize, sort);
    }

    @GetMapping("/{id}")
    public CarBrandDto findById(@PathVariable("id")Long id) {
        return carBrandService.findById(id);
    }

    @GetMapping("/by-name/{name}")
    public CarBrandDto findByName(@PathVariable("name")String name) {
        return carBrandService.findByName(name);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CarBrandDto> createCarBrand(@RequestBody @Valid CarBrandDto carBrandDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(carBrandService.createCarBrand(carBrandDto));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CarBrandDto> updateCarBrand(@RequestBody @Valid CarBrandDto carBrandDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(carBrandService.updateCarBrand(carBrandDto));
    }

    @DeleteMapping("/{id}")
    public void deleteCarBrand(@PathVariable("id") Long id) {
        carBrandService.deleteCarBrand(id);
    }
}