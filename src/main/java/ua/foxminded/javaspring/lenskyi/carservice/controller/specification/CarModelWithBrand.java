package ua.foxminded.javaspring.lenskyi.carservice.controller.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarBrand;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarModel;

public class CarModelWithBrand implements Specification<CarModel> {

    private CarBrand carBrand;

    public CarModelWithBrand(CarBrand carBrand) {
        this.carBrand = carBrand;
    }

    @Override
    public Predicate toPredicate(Root<CarModel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (carBrand == null) {
            return cb.isTrue(cb.literal(true));
        }
        return cb.equal(root.get("carBrand"), this.carBrand);
    }
}