package ua.foxminded.javaspring.lenskyi.carservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class CarModelDto {

    private String id;
    @NotBlank
    private String name;
    @NotNull
    private Integer year;
    @NotNull
    private CarBrandDto carBrandDto;
    @NotNull
    @NotEmpty
    private Set<CarTypeDto> carTypeDtos;

    public CarModelDto() {

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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public CarBrandDto getCarBrandDto() {
        return carBrandDto;
    }

    public void setCarBrandDto(CarBrandDto carBrandDto) {
        this.carBrandDto = carBrandDto;
    }

    public Set<CarTypeDto> getCarTypeDtos() {
        return carTypeDtos;
    }

    public void setCarTypeDtos(Set<CarTypeDto> carTypeDtos) {
        this.carTypeDtos = carTypeDtos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarModelDto that)) return false;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        return getYear() != null ? getYear().equals(that.getYear()) : that.getYear() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getYear() != null ? getYear().hashCode() : 0);
        return result;
    }
}
