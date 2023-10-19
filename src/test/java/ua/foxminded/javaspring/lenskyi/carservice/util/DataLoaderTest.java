package ua.foxminded.javaspring.lenskyi.carservice.util;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.foxminded.javaspring.lenskyi.carservice.repository.BrandRepository;
import ua.foxminded.javaspring.lenskyi.carservice.repository.CarModelRepository;
import ua.foxminded.javaspring.lenskyi.carservice.repository.CarTypeRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Transactional
class DataLoaderTest {

    private final static int EXPECTED_NUM_BRANDS = 64;
    private final static int EXPECTED_NUM_MODELS = 9836;
    private final static int EXPECTED_NUM_CAR_TYPES = 10;
    @Autowired
    private DataLoader dataLoader;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CarModelRepository carModelRepository;
    @Autowired
    private CarTypeRepository carTypeRepository;

    @Test
    void loadDataTest() {
        dataLoader.loadData();
        assertEquals(EXPECTED_NUM_BRANDS, brandRepository.findAll().size());
        assertEquals(EXPECTED_NUM_MODELS, carModelRepository.findAll().size());
        assertEquals(EXPECTED_NUM_CAR_TYPES, carTypeRepository.findAll().size());
    }
}