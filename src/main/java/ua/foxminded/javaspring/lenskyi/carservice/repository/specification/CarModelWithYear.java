package ua.foxminded.javaspring.lenskyi.carservice.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarModel;

public class CarModelWithYear implements Specification<CarModel> {

    private Integer year;

    public CarModelWithYear(Integer year) {
        this.year = year;
    }

    @Override
    public Predicate toPredicate(Root<CarModel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (year == null) {
            return cb.isTrue(cb.literal(true));
        }
        return cb.equal(root.get("year"), this.year);
    }
}