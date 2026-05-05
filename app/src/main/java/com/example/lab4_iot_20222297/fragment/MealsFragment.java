package com.example.lab4_iot_20222297.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4_iot_20222297.R;
import com.example.lab4_iot_20222297.adapter.MealAdapter;
import com.example.lab4_iot_20222297.api.ApiClient;
import com.example.lab4_iot_20222297.model.Meal;
import com.example.lab4_iot_20222297.model.MealResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Fragmento de Platos:
 * - Primera Parte: si se llegó desde CategoriesFragment (categoryName != null),
 *   carga platos de esa categoría automáticamente (GET 2A) y oculta la búsqueda.
 * - Segunda Parte: si se llegó por el BottomNav (categoryName == null),
 *   muestra un campo de texto para buscar por ingrediente (GET 2B).
 * - Al hacer click en un plato, navega a RecipeFragment pasando el idMeal.
 *
 * LLM asistido: Claude (Anthropic) - alineado con lo visto en clase
 * (RecyclerView, Fragments, Navigation Component, API Retrofit)
 */
public class MealsFragment extends Fragment {

    private MealAdapter adapter;
    private LinearLayout searchLayout;
    private EditText etIngredient;
    private TextView tvMealsTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewMeals);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchLayout = view.findViewById(R.id.searchLayout);
        etIngredient = view.findViewById(R.id.etIngredient);
        Button btnBuscar = view.findViewById(R.id.btnBuscar);
        tvMealsTitle = view.findViewById(R.id.tvMealsTitle);

        NavController navController = Navigation.findNavController(view);

        // Al hacer click en un plato, navega a RecipeFragment con el idMeal
        adapter = new MealAdapter(meal -> {
            Bundle bundle = new Bundle();
            bundle.putString("mealId", meal.getIdMeal());
            navController.navigate(R.id.action_meals_to_recipe, bundle);
        });
        recyclerView.setAdapter(adapter);

        // Determinar el modo según el argumento recibido
        String categoryName = null;
        if (getArguments() != null) {
            categoryName = getArguments().getString("categoryName");
        }

        if (categoryName != null) {
            // PRIMERA PARTE: llegó desde Categories → filtrar por categoría
            tvMealsTitle.setText("Platos: " + categoryName);
            searchLayout.setVisibility(View.GONE);
            fetchMealsByCategory(categoryName);
        } else {
            // SEGUNDA PARTE: llegó por BottomNav → mostrar búsqueda por ingrediente
            searchLayout.setVisibility(View.VISIBLE);
            btnBuscar.setOnClickListener(v -> {
                String ingredient = etIngredient.getText().toString().trim();
                if (!ingredient.isEmpty()) {
                    fetchMealsByIngredient(ingredient);
                } else {
                    Toast.makeText(getContext(), "Ingresa un ingrediente", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // GET 2A: filtrar platos por categoría
    private void fetchMealsByCategory(String category) {
        ApiClient.getService().getMealsByCategory(category).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealResponse> call,
                                   @NonNull Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Meal> meals = response.body().getMeals();
                    if (meals != null) {
                        adapter.setMeals(meals);
                    } else {
                        Toast.makeText(getContext(), "No se encontraron platos", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // GET 2B: filtrar platos por ingrediente
    private void fetchMealsByIngredient(String ingredient) {
        ApiClient.getService().getMealsByIngredient(ingredient).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealResponse> call,
                                   @NonNull Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Meal> meals = response.body().getMeals();
                    if (meals != null) {
                        adapter.setMeals(meals);
                    } else {
                        Toast.makeText(getContext(), "No se encontraron platos para ese ingrediente",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
