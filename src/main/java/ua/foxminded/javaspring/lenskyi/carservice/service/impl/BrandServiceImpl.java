package ua.foxminded.javaspring.lenskyi.carservice.service.impl;

import org.springframework.stereotype.Service;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.BrandDto;
import ua.foxminded.javaspring.lenskyi.carservice.controller.dto.mapper.BrandDtoMapper;
import ua.foxminded.javaspring.lenskyi.carservice.exception.IdDoesNotExistException;
import ua.foxminded.javaspring.lenskyi.carservice.exception.TheNameIsNotUniqueException;
import ua.foxminded.javaspring.lenskyi.carservice.model.Brand;
import ua.foxminded.javaspring.lenskyi.carservice.repository.BrandRepository;
import ua.foxminded.javaspring.lenskyi.carservice.service.BrandService;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    private static final String ID_DOES_NOT_EXIST_ERROR_MESSAGE = "There is no Brand with id=";
    private static final String NOT_UNIQUE_BRAND_NAME_ERROR_MESSAGE = "Error. Not unique Brand name=";
    private final BrandRepository brandRepository;
    private BrandDtoMapper mapper;

    public BrandServiceImpl(BrandRepository brandRepository, BrandDtoMapper mapper) {
        this.brandRepository = brandRepository;
        this.mapper = mapper;
    }

    @Override
    public List<BrandDto> findAll() {
        List<Brand> brands = brandRepository.findAll();
        return brands.stream()
                .map(mapper::brandEntityToBrandDto)
                .toList();
    }

    @Override
    public BrandDto findById(Long id) {
        if (!brandRepository.existsById(id)) {
            throw new IdDoesNotExistException(ID_DOES_NOT_EXIST_ERROR_MESSAGE + id);
        }
        return mapper.brandEntityToBrandDto(brandRepository.findById(id).orElseThrow());
    }

    @Override
    public BrandDto createBrand(BrandDto brandDto) {
        if (brandRepository.existsByName(brandDto.getName())) {
            throw new TheNameIsNotUniqueException(NOT_UNIQUE_BRAND_NAME_ERROR_MESSAGE + brandDto.getName());
        }
        Brand brand = new Brand();
        brand.setName(brandDto.getName());
        brandRepository.saveAndFlush(brand);
        return mapper.brandEntityToBrandDto(brand);
    }
}