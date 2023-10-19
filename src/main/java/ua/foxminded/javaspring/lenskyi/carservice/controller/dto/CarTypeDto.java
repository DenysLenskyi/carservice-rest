package ua.foxminded.javaspring.lenskyi.carservice.controller.dto;

import jakarta.validation.constraints.NotBlank;

public class CarTypeDto {

    private Long id;
    @NotBlank
    private String name;

    public CarTypeDto() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarTypeDto carType)) return false;

        return getName() != null ? getName().equals(carType.getName()) : carType.getName() == null;
    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }
}