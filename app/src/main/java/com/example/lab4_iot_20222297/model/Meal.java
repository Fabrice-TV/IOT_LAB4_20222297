package com.example.lab4_iot_20222297.model;

import com.google.gson.annotations.SerializedName;

// Modelo de plato simplificado: usado en RecyclerView de MealsFragment (GET 2A y GET 2B)
public class Meal {

    @SerializedName("idMeal")
    private String idMeal;

    @SerializedName("strMeal")
    private String strMeal;

    @SerializedName("strMealThumb")
    private String strMealThumb;

    public String getIdMeal() { return idMeal; }
    public String getStrMeal() { return strMeal; }
    public String getStrMealThumb() { return strMealThumb; }
}
