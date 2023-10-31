package ua.foxminded.javaspring.lenskyi.carservice.controller.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarModel;

public class CarModelWithName implements Specification<CarModel> {

    private String name;

    public CarModelWithName(String name) {
        this.name = name;
    }

    @Override
    public Predicate toPredicate(Root<CarModel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (name == null) {
            return cb.isTrue(cb.literal(true));
        }
        return cb.equal(root.get("name"), this.name);
    }
}