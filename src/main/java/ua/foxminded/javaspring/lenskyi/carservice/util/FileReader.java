package ua.foxminded.javaspring.lenskyi.carservice.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileReader {

    private static final Logger log = LoggerFactory.getLogger(FileReader.class);

    public List<String> readFile(String fileName) {
        List<String> list = new ArrayList<>();
        try (InputStream inputStream = getClass().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            list = reader.lines().toList();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Reading file error");
        }
        return list;
    }
}