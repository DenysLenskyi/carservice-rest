package ua.foxminded.javaspring.lenskyi.carservice.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FileReaderTest {

    private static final String FILENAME = "/file.csv";
    private static final String EXPECTED_PART_OF_CONTENT = "TY61Um9ZDd,Dodge,2021,Durango,SUV";

    @Autowired
    private FileReader fileReader;

    @Test
    void readFileTest() {
        List<String> fileStrings = fileReader.readFile(FILENAME);
        assertEquals(9838, fileStrings.size());
        assertTrue(fileStrings.contains(EXPECTED_PART_OF_CONTENT));
    }
}