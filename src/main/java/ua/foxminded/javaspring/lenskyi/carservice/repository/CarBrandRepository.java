package ua.foxminded.javaspring.lenskyi.carservice.repository;

import org.springframework.stereotype.Repository;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarBrand;

import java.util.Optional;

@Repository
public interface CarBrandRepository extends BaseRepository<CarBrand, Long> {

    Optional<CarBrand> findCarBrandByName(String name);
}