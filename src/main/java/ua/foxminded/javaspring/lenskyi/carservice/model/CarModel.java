package ua.foxminded.javaspring.lenskyi.carservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

@Entity
@Table(name = "model",
        schema = "carservice",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "year", "brand_id"}))
public class CarModel {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @Column(name = "year", nullable = false)
    private Integer year;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private CarBrand carBrand;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.PERSIST)
    @JoinTable(name = "model_type", schema = "carservice",
            joinColumns = {@JoinColumn(name = "model_id")},
            inverseJoinColumns = {@JoinColumn(name = "type_id")})
    private Set<CarType> carTypes;

    public CarModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public CarBrand getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(CarBrand carBrand) {
        this.carBrand = carBrand;
    }

    public Set<CarType> getCarTypes() {
        return carTypes;
    }

    public void setCarTypes(Set<CarType> types) {
        this.carTypes = types;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarModel carModel)) return false;

        if (getYear() != carModel.getYear()) return false;
        if (!getId().equals(carModel.getId())) return false;
        return getName().equals(carModel.getName());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getYear();
        return result;
    }
}
