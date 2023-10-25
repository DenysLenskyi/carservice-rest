package ua.foxminded.javaspring.lenskyi.carservice.util;

import jakarta.transaction.Transactional;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarBrand;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarModel;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarType;
import ua.foxminded.javaspring.lenskyi.carservice.repository.CarBrandRepository;
import ua.foxminded.javaspring.lenskyi.carservice.repository.CarModelRepository;
import ua.foxminded.javaspring.lenskyi.carservice.repository.CarTypeRepository;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DataLoader implements ApplicationRunner {

    private static final String FILENAME = "/file.csv";
    private static final String FILE_HEADER = "objectId,Make,Year,Model,Category";
    private static final String FILE_LAST_LINE = ",,,,";
    private static final String COMA = ",";
    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);
    private FileReader fileReader;
    private CarBrandRepository carBrandRepository;
    private CarModelRepository carModelRepository;
    private CarTypeRepository carTypeRepository;
    private Map<String, CarBrand> brandMap = new HashMap<>();
    private Map<String, CarType> carTypeMap = new HashMap<>();
    private Set<CarModel> carModels = new HashSet<>();

    @Autowired
    private DataSource dataSource;

    public DataLoader(FileReader fileReader, CarBrandRepository carBrandRepository, CarModelRepository carModelRepository, CarTypeRepository carTypeRepository) {
        this.fileReader = fileReader;
        this.carBrandRepository = carBrandRepository;
        this.carModelRepository = carModelRepository;
        this.carTypeRepository = carTypeRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        migrateWithSqlAndJavaCallbacks();
        loadData();
    }

    @Transactional
    void migrateWithSqlAndJavaCallbacks() {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("db/migration", "db/callback")
                .callbacks(new CustomFlywayCallback())
                .load();
        flyway.migrate();
    }

    @Transactional
    public void loadData() {
        List<String> fileStrings = new LinkedList<>(fileReader.readFile(FILENAME));
        fileStrings.remove(FILE_HEADER);
        fileStrings.remove(FILE_LAST_LINE);
        fileStrings.forEach(line -> {
            line = line.replaceAll("\"", "");
            String[] parsedLine = line.split(COMA);
            CarModel model = new CarModel();
            String modelId = parsedLine[0];
            model.setId(modelId);
            String brandName = parsedLine[1];
            CarBrand carBrand = getOrCreateBrand(brandName);
            model.setCarBrand(carBrand);
            String year = parsedLine[2];
            model.setYear(Integer.parseInt(year));
            String modelName = parsedLine[3];
            model.setName(modelName);
            List<String> dataList = new ArrayList<>(Arrays.asList(parsedLine));
            dataList.remove(modelId);
            dataList.remove(brandName);
            dataList.remove(year);
            dataList.remove(modelName);
            List<String> carTypeNames = new ArrayList<>(dataList);
            Set<CarType> carTypes = getCarTypeSet(carTypeNames);
            model.setCarTypes(carTypes);
            carModels.add(model);
        });
        carBrandRepository.saveAll(brandMap.values());
        carTypeRepository.saveAll(carTypeMap.values());
        carModelRepository.saveAll(carModels);
        log.info("Initial data loaded successfully");
    }

    private CarBrand getOrCreateBrand(String givenBrandName) {
        return Optional.ofNullable(brandMap.get(givenBrandName))
                .orElseGet(() -> {
                    CarBrand carBrand = new CarBrand();
                    carBrand.setName(givenBrandName);
                    brandMap.put(givenBrandName, carBrand);
                    return carBrand;
                });
    }

    private Set<CarType> getCarTypeSet(List<String> carTypeNames) {
        return carTypeNames.stream()
                .map(e -> e.replaceAll("\\s", ""))
                .map(this::getOrCreateCarType)
                .collect(Collectors.toSet());
    }

    private CarType getOrCreateCarType(String givenCarTypeName) {
        return Optional.ofNullable(carTypeMap.get(givenCarTypeName))
                .orElseGet(() -> {
                    CarType carType = new CarType();
                    carType.setName(givenCarTypeName);
                    carTypeMap.put(givenCarTypeName, carType);
                    return carType;
                });
    }
}