package ua.foxminded.javaspring.lenskyi.carservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarBrandDto;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarBrandService;

import java.util.List;

@RestController
@RequestMapping("api/v1/brand")
public class CarBrandController {

    private CarBrandService carBrandService;

    public CarBrandController(CarBrandService carBrandService) {
        this.carBrandService = carBrandService;
    }

    @GetMapping("/all")
    public List<CarBrandDto> findAll() {
        return carBrandService.findAll();
    }

    @GetMapping("/{id}")
    public CarBrandDto findById(@PathVariable("id")Long id) {
        return carBrandService.findById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CarBrandDto> createBrand(@RequestBody @Valid CarBrandDto carBrandDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(carBrandService.createBrand(carBrandDto));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CarBrandDto> updateBrand(@RequestBody @Valid CarBrandDto carBrandDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(carBrandService.updateBrand(carBrandDto));
    }

    @DeleteMapping("/{id}")
    public void deleteBrand(@PathVariable("id") Long id) {
        carBrandService.deleteBrand(id);
    }
}