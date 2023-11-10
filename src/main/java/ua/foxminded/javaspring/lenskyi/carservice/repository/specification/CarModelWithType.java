package ua.foxminded.javaspring.lenskyi.carservice.repository.specification;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarModel;
import ua.foxminded.javaspring.lenskyi.carservice.model.CarType;

import java.util.Collection;

public class CarModelWithType implements Specification<CarModel> {

    private CarType carType;

    public CarModelWithType(CarType carType) {
        this.carType = carType;
    }

    @Override
    public Predicate toPredicate(Root<CarModel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (carType == null) {
            return cb.isTrue(cb.literal(true));
        }
        query.distinct(true);
        Root<CarType> carTypeRoot = query.from(CarType.class);
        Expression<Collection<CarType>> modelTypes = root.get("carTypes");
        return cb.and(cb.equal(carTypeRoot.get("name"), this.carType.getName()), cb.isMember(carTypeRoot, modelTypes));
    }
}