package ua.foxminded.javaspring.lenskyi.carservice.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.javaspring.lenskyi.carservice.model.Brand;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarModel;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarType;
import ua.foxminded.javaspring.lenskyi.carservice.repository.BrandRepository;
import ua.foxminded.javaspring.lenskyi.carservice.repository.CarModelRepository;
import ua.foxminded.javaspring.lenskyi.carservice.repository.CarTypeRepository;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class DataLoader {

    private static final String FILENAME = "/file.csv";
    private static final String FILE_HEADER = "objectId,Make,Year,Model,Category";
    private static final String COMA = ",";
    private FileReader fileReader;
    private BrandRepository brandRepository;
    private CarModelRepository carModelRepository;
    private CarTypeRepository carTypeRepository;

    @Autowired
    public DataLoader(FileReader fileReader, BrandRepository brandRepository, CarModelRepository carModelRepository, CarTypeRepository carTypeRepository) {
        this.fileReader = fileReader;
        this.brandRepository = brandRepository;
        this.carModelRepository = carModelRepository;
        this.carTypeRepository = carTypeRepository;
    }

    public void loadData() {
        List<String> fileStrings = new LinkedList<>(fileReader.readFile(FILENAME));
        fileStrings.remove(FILE_HEADER);
        fileStrings.forEach(string -> {
            String s = string.replaceAll("\"", "");
            String[] data = s.split(COMA);
            if (data.length > 0) {
                String modelId = data[0];
                String brandName = data[1];
                if (!brandRepository.existsByName(brandName)) {
                    Brand brand = new Brand();
                    brand.setName(brandName);
                    brandRepository.saveAndFlush(brand);
                }
                String year = data[2];
                String modelName = data[3];
                List<String> dataList = new ArrayList<>(Arrays.asList(data));
                dataList.remove(modelId);
                dataList.remove(brandName);
                dataList.remove(year);
                dataList.remove(modelName);
                List<String> carTypeNames = new ArrayList<>(dataList);
                Set<CarType> carTypes = getCarTypes(carTypeNames);
                if (!carModelRepository.existsById(modelId)) {
                    CarModel model = new CarModel();
                    model.setId(modelId);
                    model.setBrand(brandRepository.findByName(brandName));
                    model.setYear(Integer.parseInt(year));
                    model.setName(modelName);
                    model.setTypes(carTypes);
                    carModelRepository.saveAndFlush(model);
                }
            }

        });
    }

    private Set<CarType> getCarTypes(List<String> carTypeNames) {
        return carTypeNames.stream()
                .map(name -> {
                    if (!carTypeRepository.existsByName(name)) {
                        CarType carType = new CarType();
                        carType.setName(name);
                        carTypeRepository.saveAndFlush(carType);
                        return carType;
                    }
                    return carTypeRepository.findByName(name);
                })
                .collect(Collectors.toSet());
    }
}