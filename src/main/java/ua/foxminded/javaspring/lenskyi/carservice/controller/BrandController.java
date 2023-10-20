package ua.foxminded.javaspring.lenskyi.carservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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


}