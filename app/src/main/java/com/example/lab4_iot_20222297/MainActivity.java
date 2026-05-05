package com.example.lab4_iot_20222297;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/*
 * Pantalla Principal de la Aplicación:
 * - Muestra el nombre de la app, 2 imágenes gastronómicas y el botón "Ingresar".
 * - Al presionar "Ingresar" valida la conexión a Internet:
 *     · Con conexión → navega al AppActivity.
 *     · Sin conexión → muestra Dialog con botón "Configuración" que abre los ajustes Wi-Fi.
 * - Último elemento: "Elaborado por: Fabricio Tirado / 20222297"
 *
 * LLM asistido: Claude (Anthropic) - alineado con lo visto en clase
 * (Activity, ConnectivityManager, AlertDialog, Intents)
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnIngresar = findViewById(R.id.btnIngresar);

        // Validar conexión al intentar ingresar
        btnIngresar.setOnClickListener(v -> {
            if (isNetworkAvailable()) {
                startActivity(new Intent(this, AppActivity.class));
            } else {
                showNoInternetDialog();
            }
        });
    }

    // Verifica si hay conectividad de red activa (Wi-Fi, datos móviles o Ethernet)
    private boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = cm.getActiveNetwork();
        if (network == null) return false;
        NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
        return capabilities != null && (
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        );
    }

    // Muestra Dialog de sin conexión con opción "Configuración" → ajustes Wi-Fi del dispositivo
    private void showNoInternetDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.no_internet_title))
                .setMessage(getString(R.string.no_internet_message))
                .setPositiveButton(getString(R.string.configuracion), (dialog, which) -> {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                })
                .setNegativeButton(getString(R.string.cancelar), null)
                .setCancelable(false)
                .show();
    }
}
