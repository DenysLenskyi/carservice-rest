package ua.foxminded.javaspring.lenskyi.carservice.controller.dto;

import jakarta.validation.constraints.NotBlank;

public class BrandDto {

    private Long id;
    @NotBlank
    private String name;

    public BrandDto() {

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
        if (!(o instanceof BrandDto brand)) return false;

        return getName().equals(brand.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
