package com.example.lab4_iot_20222297.fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4_iot_20222297.R;
import com.example.lab4_iot_20222297.adapter.CategoryAdapter;
import com.example.lab4_iot_20222297.api.ApiClient;
import com.example.lab4_iot_20222297.model.Category;
import com.example.lab4_iot_20222297.model.CategoryResponse;
import com.example.lab4_iot_20222297.model.MealDetail;
import com.example.lab4_iot_20222297.model.MealDetailResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Fragmento principal (búsqueda principal):
 * - Muestra RecyclerView con categorías obtenidas de GET 1 (categories.php)
 * - Acelerómetro activo: al agitar > 4 m/s² obtiene plato aleatorio (GET 4) y navega a Recipe
 * - Al hacer click en una categoría navega a MealsFragment pasando el nombre de categoría
 *
 * LLM asistido: Claude (Anthropic) - alineado con lo visto en clase
 * (RecyclerView, Fragments, Navigation Component, Sensores/Acelerómetro)
 */
public class CategoriesFragment extends Fragment implements SensorEventListener {

    private CategoryAdapter adapter;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    // Cooldown de 2 segundos entre agitaciones para evitar múltiples disparos
    private long lastShakeTime = 0;
    private static final long SHAKE_COOLDOWN_MS = 2000;
    // Umbral de aceleración en m/s² (restando gravedad)
    private static final float SHAKE_THRESHOLD = 4f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewCategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        NavController navController = Navigation.findNavController(view);

        // Al hacer click en una categoría, navega a MealsFragment con el nombre de categoría
        adapter = new CategoryAdapter(category -> {
            Bundle bundle = new Bundle();
            bundle.putString("categoryName", category.getStrCategory());
            navController.navigate(R.id.action_categories_to_meals, bundle);
        });

        recyclerView.setAdapter(adapter);

        // Inicializar el acelerómetro
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        loadCategories();
    }

    private void loadCategories() {
        ApiClient.getService().getCategories().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<CategoryResponse> call,
                                   @NonNull Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body().getCategories();
                    if (categories != null) {
                        adapter.setCategories(categories);
                    }
                } else {
                    Toast.makeText(getContext(), "Error al obtener categorías", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CategoryResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Registrar el acelerómetro solo cuando el fragmento está visible
    @Override
    public void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    // Desregistrar el acelerómetro al salir del fragmento para ahorrar batería
    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // Calcular magnitud total y restar la gravedad para obtener aceleración neta
        float magnitude = (float) Math.sqrt(x * x + y * y + z * z);
        float acceleration = magnitude - SensorManager.GRAVITY_EARTH;

        if (acceleration > SHAKE_THRESHOLD) {
            long now = System.currentTimeMillis();
            if (now - lastShakeTime > SHAKE_COOLDOWN_MS) {
                lastShakeTime = now;
                Toast.makeText(getContext(), "¡Agitación detectada! Buscando plato sorpresa...",
                        Toast.LENGTH_SHORT).show();
                fetchRandomMeal();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    // GET 4: Obtener un plato aleatorio y navegar a RecipeFragment con su ID
    private void fetchRandomMeal() {
        ApiClient.getService().getRandomMeal().enqueue(new Callback<MealDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealDetailResponse> call,
                                   @NonNull Response<MealDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().getMeals() != null
                        && !response.body().getMeals().isEmpty()) {
                    MealDetail meal = response.body().getMeals().get(0);
                    if (getView() != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString("mealId", meal.getIdMeal());
                        NavController navController = Navigation.findNavController(getView());
                        navController.navigate(R.id.action_categories_to_recipe, bundle);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MealDetailResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error al obtener plato sorpresa", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
