package ua.foxminded.javaspring.lenskyi.carservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarBrandDto;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarBrandService;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class CarBrandController {

    private CarBrandService carBrandService;

    public CarBrandController(CarBrandService carBrandService) {
        this.carBrandService = carBrandService;
    }

    @GetMapping
    public List<CarBrandDto> findAll() {
        return carBrandService.findAll();
    }

    @GetMapping("/{id}")
    public CarBrandDto findById(@PathVariable("id")Long id) {
        return carBrandService.findById(id);
    }

    @PostMapping(path = "/new", consumes = {"application/json"})
    public ResponseEntity<CarBrandDto> createBrand(@RequestBody @Valid CarBrandDto carBrandDto) {
        CarBrandDto newBrand = carBrandService.createBrand(carBrandDto);
        return new ResponseEntity<>(newBrand, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}", consumes = {"application/json"})
    public ResponseEntity<CarBrandDto> updateBrand(@PathVariable("id") Long id,
                                                   @RequestBody @Valid CarBrandDto carBrandDto) {
        CarBrandDto updatedCarBrandDto = carBrandService.updateBrand(id, carBrandDto);
        return new ResponseEntity<>(updatedCarBrandDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteBrand(@PathVariable("id") Long id) {
        carBrandService.deleteBrand(id);
    }
}