package com.example.proyectofiagrubngil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.proyectofiagrubngil.AppDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        FloatingActionButton fab = findViewById(R.id.fabAdd);
        db = AppDatabase.getDatabase(this);

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
            List<Incident> list = db.incidentDao().getAll();
            List<String> displayList = new ArrayList<>();
            for (Incident i : list) {
                // Formato simple para la lista: [Alta] Aula 2: PC Roto
                displayList.add("[" + i.priority + "] " + i.location + ": " + i.description + " (" + i.status + ")");
            }
            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
                listView.setAdapter(adapter);
            });
        }).start();
    }
}