package com.eventorium.data.dtos.categories;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryRequestDto {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    private String description;

    public CategoryRequestDto() {
    }

    public CategoryRequestDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
