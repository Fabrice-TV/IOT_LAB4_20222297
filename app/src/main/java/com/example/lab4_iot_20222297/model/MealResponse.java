package com.example.lab4_iot_20222297.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// Wrapper del JSON de respuesta del Método GET 2A y GET 2B (filter.php)
public class MealResponse {

    @SerializedName("meals")
    private List<Meal> meals;

    public List<Meal> getMeals() { return meals; }
}
