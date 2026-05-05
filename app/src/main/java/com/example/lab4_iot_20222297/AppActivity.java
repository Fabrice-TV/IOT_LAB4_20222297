package com.example.lab4_iot_20222297;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/*
 * AppActivity - Contenedor principal con BottomNavigationView y Navigation Component:
 *
 * Diseño: NavHostFragment (arriba, peso=1) + BottomNavigationView (abajo).
 * Los 3 fragmentos (Categories, Meals, Recipe) se conectan via Navigation Component.
 *
 * Manejo del BackStack:
 * - Al navegar por el BottomNav se usa popUpTo(categoriesFragment, inclusive=true)
 *   para limpiar toda la pila → presionar Atrás siempre regresa al MainActivity.
 * - La navegación interna (ej. Categories→Meals al hacer click) sí agrega a la pila.
 *
 * LLM asistido: Claude (Anthropic) - alineado con lo visto en clase
 * (Navigation Component, BottomNavigationView, Fragment BackStack)
 */
public class AppActivity extends AppCompatActivity {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        // Obtener el NavController desde el NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        /*
         * Listener del BottomNav:
         * Al cambiar de tab, se limpia TODA la pila de fragmentos (popUpTo inclusive=true)
         * y se navega al fragmento seleccionado como único elemento de la pila.
         * Esto garantiza que "Atrás" regrese al MainActivity sin importar cuántos tabs
         * se hayan visitado previamente (requisito del laboratorio).
         */
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            int currentDest = navController.getCurrentDestination() != null
                    ? navController.getCurrentDestination().getId() : -1;

            // Si ya estamos en el destino seleccionado, no hacer nada
            if (currentDest == itemId) return true;

            // NavOptions: limpiar toda la pila hasta el inicio (inclusive) antes de navegar
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.categoriesFragment, true)
                    .setLaunchSingleTop(true)
                    .build();

            navController.navigate(itemId, null, navOptions);
            return true;
        });

        // Sincronizar el ícono del BottomNav cuando la navegación interna cambia de fragmento
        // (ej. Categories → Meals por click en una categoría)
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int destId = destination.getId();
            if (destId == R.id.categoriesFragment
                    || destId == R.id.mealsFragment
                    || destId == R.id.recipeFragment) {
                bottomNav.setSelectedItemId(destId);
            }
        });

        // Manejo moderno del botón Atrás: si no hay nada en la pila, finalizar AppActivity
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!navController.popBackStack()) {
                    // Pila vacía → volver al MainActivity
                    finish();
                }
            }
        });
    }
}
