package com.example.lab4_iot_20222297.model;

import com.google.gson.annotations.SerializedName;

// Modelo completo de un plato: usado en RecipeFragment (GET 3 y GET 4)
// Incluye nombre, categoría, área, instrucciones, imagen e ingredientes con medidas
public class MealDetail {

    @SerializedName("idMeal")        private String idMeal;
    @SerializedName("strMeal")       private String strMeal;
    @SerializedName("strCategory")   private String strCategory;
    @SerializedName("strArea")       private String strArea;
    @SerializedName("strInstructions") private String strInstructions;
    @SerializedName("strMealThumb")  private String strMealThumb;

    // Al menos 5 ingredientes con sus medidas
    @SerializedName("strIngredient1") private String strIngredient1;
    @SerializedName("strIngredient2") private String strIngredient2;
    @SerializedName("strIngredient3") private String strIngredient3;
    @SerializedName("strIngredient4") private String strIngredient4;
    @SerializedName("strIngredient5") private String strIngredient5;

    @SerializedName("strMeasure1") private String strMeasure1;
    @SerializedName("strMeasure2") private String strMeasure2;
    @SerializedName("strMeasure3") private String strMeasure3;
    @SerializedName("strMeasure4") private String strMeasure4;
    @SerializedName("strMeasure5") private String strMeasure5;

    public String getIdMeal()          { return idMeal; }
    public String getStrMeal()         { return strMeal; }
    public String getStrCategory()     { return strCategory; }
    public String getStrArea()         { return strArea; }
    public String getStrInstructions() { return strInstructions; }
    public String getStrMealThumb()    { return strMealThumb; }
    public String getStrIngredient1()  { return strIngredient1; }
    public String getStrIngredient2()  { return strIngredient2; }
    public String getStrIngredient3()  { return strIngredient3; }
    public String getStrIngredient4()  { return strIngredient4; }
    public String getStrIngredient5()  { return strIngredient5; }
    public String getStrMeasure1()     { return strMeasure1; }
    public String getStrMeasure2()     { return strMeasure2; }
    public String getStrMeasure3()     { return strMeasure3; }
    public String getStrMeasure4()     { return strMeasure4; }
    public String getStrMeasure5()     { return strMeasure5; }
}
