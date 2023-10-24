package ua.foxminded.javaspring.lenskyi.carservice.controller;

import jakarta.validation.Valid;
import org.springframework.boot.context.properties.bind.DefaultValue;
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
        return carBrandService.findAllPaginated(null, null, null).getContent();
    }

    @GetMapping("/all/{pageNumber}/{pageSize}")
    public List<CarBrandDto> findAll(@PathVariable Integer pageNumber,
                                     @PathVariable Integer pageSize) {
        return carBrandService.findAllPaginated(pageNumber, pageSize, null).getContent();
    }

    @GetMapping("/all/{pageNumber}/{pageSize}/{sort}")
    public List<CarBrandDto> findAll(@PathVariable Integer pageNumber,
                                     @PathVariable Integer pageSize,
                                     @PathVariable String sort) {
        return carBrandService.findAllPaginated(pageNumber, pageSize, sort).getContent();
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