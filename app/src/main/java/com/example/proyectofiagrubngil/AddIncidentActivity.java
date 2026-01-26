package com.example.proyectofiagrubngil;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

        // Configurar Spinners
        String[] priorities = {"Baja", "Media", "Alta"};
        spPriority.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, priorities));

        String[] types = {"PC", "Portátil", "Proyector", "Red", "Otro"};
        spType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types));

        btnSave.setOnClickListener(v -> {
            new Thread(() -> {
                Incident incident = new Incident(
                        etLocation.getText().toString(),
                        spPriority.getSelectedItem().toString(),
                        spType.getSelectedItem().toString(),
                        etDeviceId.getText().toString(),
                        etDesc.getText().toString(),
                        System.currentTimeMillis(),
                        "Abierta"
                );
                AppDatabase.getDatabase(this).incidentDao().insert(incident);
                finish();
            }).start();
        });
    }
}