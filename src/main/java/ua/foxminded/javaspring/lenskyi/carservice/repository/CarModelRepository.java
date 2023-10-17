package ua.foxminded.javaspring.lenskyi.carservice.repository;

import org.springframework.stereotype.Repository;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarModel;

@Repository
public interface CarModelRepository extends BaseRepository<CarModel, Long> {

    boolean existsById(String id);
}