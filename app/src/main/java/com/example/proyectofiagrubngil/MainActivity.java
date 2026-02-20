package com.example.proyectofiagrubngil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private AppDatabase db;

    // Variable para controlar el filtro (False = Ver todas, True = Ver solo pendientes)
    private boolean showOnlyPending = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = findViewById(R.id.listView);
        FloatingActionButton fab = findViewById(R.id.fabAdd);
        db = AppDatabase.getDatabase(this);

        // --- BOTONES DE FILTRO Y CONFIGURACIÓN ---
        Button btnAll = findViewById(R.id.btnAll);
        Button btnPending = findViewById(R.id.btnPending);
        Button btnTestAI = findViewById(R.id.btnTestAI);

        // --- PASO C IMPLEMENTADO: Botón de Configuración ---
        // Asegúrate de tener un botón con android:id="@+id/btnConfig" en tu XML

        // En onCreate
        Button btnConfig = findViewById(R.id.btnConfig); // Asegúrate de crearlo en el XML
        btnConfig.setOnClickListener(v -> startActivity(new Intent(this, ConfigActivity.class)));

        if (btnAll != null) {
            btnAll.setOnClickListener(v -> {
                showOnlyPending = false;
                loadIncidents(); // Recargar lista completa
            });
        }

        if (btnPending != null) {
            btnPending.setOnClickListener(v -> {
                showOnlyPending = true;
                loadIncidents(); // Recargar solo pendientes
            });
        }

        // Lógica para abrir la pantalla de configuración
        if (btnConfig != null) {
            btnConfig.setOnClickListener(v -> startActivity(new Intent(this, ConfigActivity.class)));
        }
        if (btnTestAI != null) {
            btnTestAI.setOnClickListener(v -> {
                android.widget.Toast.makeText(this, "Pidiendo resumen a Gemini...", android.widget.Toast.LENGTH_SHORT).show();

                // Creamos una petición de un solo uso para que se ejecute YA
                androidx.work.OneTimeWorkRequest testRequest = new androidx.work.OneTimeWorkRequest.Builder(SummaryWorker.class).build();
                WorkManager.getInstance(this).enqueue(testRequest);
            });
        }
        // ------------------------------------------------

        // Configurar navegación a pantalla de alta
        fab.setOnClickListener(v -> startActivity(new Intent(this, AddIncidentActivity.class)));

        // PROGRAMAR TAREA AUTOMÁTICA (CADA 1 HORA)
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(SummaryWorker.class, 1, TimeUnit.HOURS).build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("DailySummary", ExistingPeriodicWorkPolicy.KEEP, workRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadIncidents();
    }

    private void loadIncidents() {
        new Thread(() -> {
            // --- LÓGICA DE FILTRO ---
            List<Incident> list;
            if (showOnlyPending) {
                // Si el filtro está activo, pedimos solo las pendientes (No "Resuelta")
                list = db.incidentDao().getPending();
            } else {
                // Si no, pedimos todas
                list = db.incidentDao().getAll();
            }

            List<String> displayList = new ArrayList<>();
            for (Incident i : list) {
                displayList.add("[" + i.priority + "] " + i.location + ": " + i.description + " (" + i.status + ")");
            }

            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
                listView.setAdapter(adapter);

                // --- CLICK PARA ACTUALIZAR ESTADO ---
                // Al pulsar una fila, abrimos el diálogo para cambiar estado
                listView.setOnItemClickListener((parent, view, position, id) -> {
                    Incident selected = list.get(position);
                    showUpdateStatusDialog(selected);
                });
            });
        }).start();
    }

    // --- MÉTODO: DIÁLOGO PARA CAMBIAR A "RESUELTA" ---
    private void showUpdateStatusDialog(Incident incident) {
        String[] options = {"Abierta", "En curso", "Resuelta"};

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Cambiar Estado: " + incident.location)
                .setSingleChoiceItems(options, -1, (dialog, which) -> {
                    incident.status = options[which]; // Cambiamos el estado en el objeto

                    // Guardamos en Base de Datos en segundo plano
                    new Thread(() -> {
                        db.incidentDao().update(incident);
                        loadIncidents(); // Recargamos la lista para ver el cambio
                    }).start();

                    dialog.dismiss(); // Cerramos el diálogo
                })
                .show();
    }
}