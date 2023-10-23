package ua.foxminded.javaspring.lenskyi.carservice.repository;

import org.springframework.stereotype.Repository;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarBrand;

@Repository
public interface CarBrandRepository extends BaseRepository<CarBrand, Long> {
}