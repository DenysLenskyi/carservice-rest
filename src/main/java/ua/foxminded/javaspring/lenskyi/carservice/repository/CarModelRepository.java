package ua.foxminded.javaspring.lenskyi.carservice.repository;

import org.springframework.stereotype.Repository;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarBrand;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarModel;

import java.util.Optional;

@Repository
public interface CarModelRepository extends BaseRepository<CarModel, String> {

    boolean existsById(String id);

    boolean existsByNameAndYearAndCarBrand(String name, Integer year, CarBrand carBrand);

    Optional<CarModel> findByNameAndYearAndCarBrand(String name, Integer year, CarBrand carBrand);
}