package com.example.lab4_iot_20222297.api;

import com.example.lab4_iot_20222297.model.CategoryResponse;
import com.example.lab4_iot_20222297.model.MealDetailResponse;
import com.example.lab4_iot_20222297.model.MealResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// Interfaz Retrofit con los 4 métodos GET requeridos por el laboratorio
// LLM asistido: Claude (Anthropic) - alineado con lo visto en clase (Retrofit, API REST)
public interface MealApiService {

    // GET 1: Obtener todas las categorías
    @GET("categories.php")
    Call<CategoryResponse> getCategories();

    // GET 2A: Filtrar platos por categoría (ej. c=Seafood)
    @GET("filter.php")
    Call<MealResponse> getMealsByCategory(@Query("c") String category);

    // GET 2B: Filtrar platos por ingrediente (ej. i=chicken_breast)
    @GET("filter.php")
    Call<MealResponse> getMealsByIngredient(@Query("i") String ingredient);

    // GET 3: Obtener detalle completo de un plato por ID (ej. i=52959)
    @GET("lookup.php")
    Call<MealDetailResponse> getMealById(@Query("i") String mealId);

    // GET 4: Obtener una receta aleatoria
    @GET("random.php")
    Call<MealDetailResponse> getRandomMeal();
}
