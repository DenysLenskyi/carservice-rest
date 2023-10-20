package ua.foxminded.javaspring.lenskyi.carservice.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.BrandDto;
import ua.foxminded.javaspring.lenskyi.carservice.service.BrandService;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {

    private BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public List<BrandDto> findAll() {
        return brandService.findAll();
    }

    @GetMapping("/{id}")
    public BrandDto findById(@PathVariable("id")Long id) {
        return brandService.findById(id);
    }

    @PostMapping(path = "/new", consumes = {"application/json"})
    public ResponseEntity<BrandDto> createBrand(@RequestBody @Valid BrandDto brandDto) {
        BrandDto newBrand = brandService.createBrand(brandDto);
        return new ResponseEntity<>(newBrand, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}", consumes = {"application/json"})
    public ResponseEntity<BrandDto> updateBrand(@PathVariable("id") Long id,
                                                @RequestBody @Valid BrandDto brandDto) {
        BrandDto updatedBrandDto = brandService.updateBrand(id, brandDto);
        return new ResponseEntity<>(updatedBrandDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteBrand(@PathVariable("id") Long id) {
        brandService.deleteBrand(id);
    }
}