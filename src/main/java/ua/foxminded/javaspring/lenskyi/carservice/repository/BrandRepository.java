package ua.foxminded.javaspring.lenskyi.carservice.repository;

import org.springframework.stereotype.Repository;
import ua.foxminded.javaspring.lenskyi.carservice.model.Brand;

@Repository
public interface BrandRepository extends BaseRepository<Brand, Long> {
}