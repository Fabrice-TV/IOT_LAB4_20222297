package com.example.lab4_iot_20222297.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// Wrapper del JSON de respuesta del Método GET 1 (categories.php)
public class CategoryResponse {

    @SerializedName("categories")
    private List<Category> categories;

    public List<Category> getCategories() { return categories; }
}
