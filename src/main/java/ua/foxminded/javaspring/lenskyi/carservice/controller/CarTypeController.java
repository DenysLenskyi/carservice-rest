package ua.foxminded.javaspring.lenskyi.carservice.controller;

import org.springframework.web.bind.annotation.*;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.CarTypeDto;
import ua.foxminded.javaspring.lenskyi.carservice.service.CarTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/type")
public class CarTypeController {

    private CarTypeService carTypeService;

    public CarTypeController(CarTypeService carTypeService) {
        this.carTypeService = carTypeService;
    }

    @GetMapping("/all")
    public List<CarTypeDto> findAll(@RequestParam(defaultValue = "0") int pageNumber,
                                    @RequestParam(defaultValue = "0") int pageSize,
                                    @RequestParam(defaultValue = "id") String sort) {
        return carTypeService.findAllPaginated(pageNumber, pageSize, sort).getContent();
    }

//    @GetMapping("/{id}")
//    public CarBrandDto findById(@PathVariable("id")Long id) {
//        return carTypeService.findById(id);
//    }
//
//    @GetMapping("/by-name/{name}")
//    public CarBrandDto findByName(@PathVariable("name")String name) {
//        return carTypeService.findByName(name);
//    }
//
//    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<CarBrandDto> createBrand(@RequestBody @Valid CarBrandDto carBrandDto) {
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(carTypeService.createBrand(carBrandDto));
//    }
//
//    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<CarBrandDto> updateBrand(@RequestBody @Valid CarBrandDto carBrandDto) {
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(carTypeService.updateBrand(carBrandDto));
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteBrand(@PathVariable("id") Long id) {
//        carTypeService.deleteBrand(id);
//    }
}