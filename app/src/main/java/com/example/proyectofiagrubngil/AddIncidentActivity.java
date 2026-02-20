
package com.example.proyectofiagrubngil;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddIncidentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_incident);

        EditText etLocation = findViewById(R.id.etLocation);
        EditText etDeviceId = findViewById(R.id.etDeviceId);
        EditText etDesc = findViewById(R.id.etDesc);
        Spinner spPriority = findViewById(R.id.spPriority);
        Spinner spType = findViewById(R.id.spType);
        Button btnSave = findViewById(R.id.btnSave);

        // Mensaje de privacidad obligatorio por rúbrica
        Toast.makeText(this, "ATENCIÓN: No incluyas datos personales sensibles en la descripción.", Toast.LENGTH_LONG).show();

        String[] priorities = {"Baja", "Media", "Alta"};
        spPriority.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, priorities));

        String[] types = {"PC", "Portátil", "Proyector", "Red", "Otro"};
        spType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types));

        btnSave.setOnClickListener(v -> {
            String location = etLocation.getText().toString();
            String desc = etDesc.getText().toString();

            // Validación para no guardar incidencias vacías
            if (location.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Ubicación y Descripción son obligatorias", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                Incident incident = new Incident(
                        location,
                        spPriority.getSelectedItem().toString(),
                        spType.getSelectedItem().toString(),
                        etDeviceId.getText().toString(),
                        desc,
                        System.currentTimeMillis(),
                        "Abierta"
                );
                AppDatabase.getDatabase(this).incidentDao().insert(incident);
                finish();
            }).start();
        });
    }
}