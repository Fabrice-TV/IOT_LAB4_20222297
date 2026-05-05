package com.example.lab4_iot_20222297.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// Wrapper del JSON de respuesta del Método GET 3 (lookup.php) y GET 4 (random.php)
public class MealDetailResponse {

    @SerializedName("meals")
    private List<MealDetail> meals;

    public List<MealDetail> getMeals() { return meals; }
}
