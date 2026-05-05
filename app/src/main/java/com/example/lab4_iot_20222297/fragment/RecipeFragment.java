package com.example.lab4_iot_20222297.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.lab4_iot_20222297.R;
import com.example.lab4_iot_20222297.api.ApiClient;
import com.example.lab4_iot_20222297.model.MealDetail;
import com.example.lab4_iot_20222297.model.MealDetailResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Fragmento de Receta (Vista Detalle):
 * - Tiene un EditText para ingresar el ID del plato manualmente + botón Buscar.
 * - Si llega con mealId (desde MealsFragment o acelerómetro), carga automáticamente.
 * - Muestra: nombre, categoría, área/origen, instrucciones y mínimo 3 ingredientes.
 *
 * LLM asistido: Claude (Anthropic) - alineado con lo visto en clase
 * (Fragments, Navigation Component, API Retrofit, Glide)
 */
public class RecipeFragment extends Fragment {

    private EditText etMealId;
    private LinearLayout recipeContent;
    private ImageView ivMealImage;
    private TextView tvMealName;
    private TextView tvCategory;
    private TextView tvArea;
    private TextView tvIngredients;
    private TextView tvInstructions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etMealId = view.findViewById(R.id.etMealId);
        Button btnSearchRecipe = view.findViewById(R.id.btnSearchRecipe);
        recipeContent = view.findViewById(R.id.recipeContent);
        ivMealImage = view.findViewById(R.id.ivMealImage);
        tvMealName = view.findViewById(R.id.tvMealName);
        tvCategory = view.findViewById(R.id.tvCategory);
        tvArea = view.findViewById(R.id.tvArea);
        tvIngredients = view.findViewById(R.id.tvIngredients);
        tvInstructions = view.findViewById(R.id.tvInstructions);

        // Botón de búsqueda manual por ID
        btnSearchRecipe.setOnClickListener(v -> {
            String mealId = etMealId.getText().toString().trim();
            if (!mealId.isEmpty()) {
                fetchRecipe(mealId);
            } else {
                Toast.makeText(getContext(), "Ingresa el ID del plato", Toast.LENGTH_SHORT).show();
            }
        });

        // Si llegó con un mealId (desde MealsFragment o acelerómetro), carga automáticamente
        String mealId = null;
        if (getArguments() != null) {
            mealId = getArguments().getString("mealId");
        }
        if (mealId != null && !mealId.isEmpty()) {
            etMealId.setText(mealId);
            fetchRecipe(mealId);
        }
    }

    // GET 3: buscar detalle completo del plato por idMeal
    private void fetchRecipe(String mealId) {
        ApiClient.getService().getMealById(mealId).enqueue(new Callback<MealDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealDetailResponse> call,
                                   @NonNull Response<MealDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().getMeals() != null
                        && !response.body().getMeals().isEmpty()) {
                    displayRecipe(response.body().getMeals().get(0));
                } else {
                    Toast.makeText(getContext(), "Plato no encontrado. Verifica el ID.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealDetailResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Muestra todos los atributos del plato en la vista
    private void displayRecipe(MealDetail meal) {
        recipeContent.setVisibility(View.VISIBLE);

        Glide.with(this)
                .load(meal.getStrMealThumb())
                .into(ivMealImage);

        tvMealName.setText(meal.getStrMeal());
        tvCategory.setText("Categoría: " + meal.getStrCategory());
        tvArea.setText("Origen: " + meal.getStrArea());
        tvInstructions.setText(meal.getStrInstructions());

        // Construir lista de ingredientes con medidas (mínimo 3, hasta 5)
        List<String> ingredientsList = new ArrayList<>();
        addIngredient(ingredientsList, meal.getStrIngredient1(), meal.getStrMeasure1());
        addIngredient(ingredientsList, meal.getStrIngredient2(), meal.getStrMeasure2());
        addIngredient(ingredientsList, meal.getStrIngredient3(), meal.getStrMeasure3());
        addIngredient(ingredientsList, meal.getStrIngredient4(), meal.getStrMeasure4());
        addIngredient(ingredientsList, meal.getStrIngredient5(), meal.getStrMeasure5());

        StringBuilder sb = new StringBuilder();
        for (String ingredient : ingredientsList) {
            sb.append("• ").append(ingredient).append("\n");
        }
        tvIngredients.setText(sb.toString().trim());
    }

    // Agrega un ingrediente con su medida si no está vacío
    private void addIngredient(List<String> list, String ingredient, String measure) {
        if (ingredient != null && !ingredient.trim().isEmpty()) {
            String entry = ingredient.trim();
            if (measure != null && !measure.trim().isEmpty()) {
                entry = measure.trim() + " " + entry;
            }
            list.add(entry);
        }
    }
}
