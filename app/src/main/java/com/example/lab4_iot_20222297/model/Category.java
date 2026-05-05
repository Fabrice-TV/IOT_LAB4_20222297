package com.example.lab4_iot_20222297.model;

import com.google.gson.annotations.SerializedName;

// Modelo que representa una categoría de comida del Método GET 1
public class Category {

    @SerializedName("idCategory")
    private String idCategory;

    @SerializedName("strCategory")
    private String strCategory;

    @SerializedName("strCategoryThumb")
    private String strCategoryThumb;

    @SerializedName("strCategoryDescription")
    private String strCategoryDescription;

    public String getIdCategory() { return idCategory; }
    public String getStrCategory() { return strCategory; }
    public String getStrCategoryThumb() { return strCategoryThumb; }
    public String getStrCategoryDescription() { return strCategoryDescription; }
}
