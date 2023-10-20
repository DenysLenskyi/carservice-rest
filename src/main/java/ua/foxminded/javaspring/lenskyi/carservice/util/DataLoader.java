package ua.foxminded.javaspring.lenskyi.carservice.util;

import jakarta.transaction.Transactional;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.carservice.model.Brand;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarModel;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarType;
import ua.foxminded.javaspring.lenskyi.carservice.repository.BrandRepository;
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
    private FileReader fileReader;
    private BrandRepository brandRepository;
    private CarModelRepository carModelRepository;
    private CarTypeRepository carTypeRepository;
    private Set<Brand> brands = new HashSet<>();
    private Set<CarType> carTypes = new HashSet<>();
    private Set<CarModel> carModels = new HashSet<>();
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DataSource dataSource;

    public DataLoader(FileReader fileReader, BrandRepository brandRepository, CarModelRepository carModelRepository, CarTypeRepository carTypeRepository) {
        this.fileReader = fileReader;
        this.brandRepository = brandRepository;
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
        fileStrings.forEach(string -> {
            String s = string.replaceAll("\"", "");
            String[] data = s.split(COMA);
            CarModel model = new CarModel();
            String modelId = data[0];
            model.setId(modelId);
            String brandName = data[1];
            Brand brand = getOrCreateBrand(brandName);
            model.setBrand(brand);
            String year = data[2];
            model.setYear(Integer.parseInt(year));
            String modelName = data[3];
            model.setName(modelName);
            List<String> dataList = new ArrayList<>(Arrays.asList(data));
            dataList.remove(modelId);
            dataList.remove(brandName);
            dataList.remove(year);
            dataList.remove(modelName);
            List<String> carTypeNames = new ArrayList<>(dataList);
            Set<CarType> carTypes = getOrCreateCarTypes(carTypeNames);
            model.setCarTypes(carTypes);
            carModels.add(model);
        });
        brandRepository.saveAll(brands);
        carTypeRepository.saveAll(carTypes);
        carModelRepository.saveAll(carModels);
        log.info("Initial data loaded successfully");
    }

    private Brand getOrCreateBrand(String brandName) {
        Set<String> brandNames = brands.stream()
                .map(Brand::getName)
                .collect(Collectors.toSet());
        if (brandNames.contains(brandName)) {
            return brands.stream()
                    .filter(b -> b.getName().equals(brandName))
                    .findFirst()
                    .orElse(null);
        } else {
            Brand brand = new Brand();
            brand.setName(brandName);
            brands.add(brand);
            return brand;
        }
    }

    private Set<CarType> getOrCreateCarTypes(List<String> carTypeNames) {
        Set<String> existedCarTypeNames = carTypes.stream()
                .map(CarType::getName)
                .collect(Collectors.toSet());
        return carTypeNames.stream()
                .map(e -> e.replaceAll("\\s", ""))
                .map(name -> {
                    if (existedCarTypeNames.contains(name)) {
                        return carTypes.stream()
                                .filter(carType -> carType.getName().equals(name))
                                .findFirst()
                                .orElse(null);
                    } else {
                        CarType carType = new CarType();
                        carType.setName(name);
                        carTypes.add(carType);
                        return carType;
                    }
                })
                .collect(Collectors.toSet());
    }
}