package com.example.proyectofiagrubngil;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ConfigActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        EditText etEmail = findViewById(R.id.etAdminEmail);
        Button btnSave = findViewById(R.id.btnSaveConfig);

        // Cargar email guardado
        SharedPreferences prefs = getSharedPreferences("FIAG_PREFS", MODE_PRIVATE);
        String currentEmail = prefs.getString("admin_email", "admin@iescierva.net");
        etEmail.setText(currentEmail);

        btnSave.setOnClickListener(v -> {
            prefs.edit().putString("admin_email", etEmail.getText().toString()).apply();
            Toast.makeText(this, "Configuración guardada", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}