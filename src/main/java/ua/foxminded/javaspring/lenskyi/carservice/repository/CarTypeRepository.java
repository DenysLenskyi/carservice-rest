package ua.foxminded.javaspring.lenskyi.carservice.repository;

import org.springframework.stereotype.Repository;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarType;

@Repository
public interface CarTypeRepository extends BaseRepository<CarType, Long> {
}