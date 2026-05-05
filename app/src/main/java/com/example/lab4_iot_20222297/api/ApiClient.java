package com.example.lab4_iot_20222297.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Cliente Retrofit singleton para consumir TheMealDB API
// API Key público para desarrollo: 1 (incluido en la base URL)
public class ApiClient {

    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static Retrofit retrofit;

    public static MealApiService getService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(MealApiService.class);
    }
}
