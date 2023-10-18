package ua.foxminded.javaspring.lenskyi.carservice.util;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Transactional
public class DataLoaderTest {

    @Autowired
    private DataLoader dataLoader;

    @Test
    void loadDataTest() {
        dataLoader.loadData();
    }
}