package ua.foxminded.javaspring.lenskyi.carservice.repository;

import org.springframework.stereotype.Repository;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarType;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CarTypeRepository extends BaseRepository<CarType, Long> {

    Optional<CarType> findCarTypeByName(String name);

    Set<CarType> findCarTypeByNameIn(List<String> names);
}