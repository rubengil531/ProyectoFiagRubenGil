package com.example.proyectofiagrubngil;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.util.List;

public class SummaryWorker extends Worker {

    public SummaryWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("FIAG_WORKER", "Iniciando tarea automática...");

        // 1. Obtener datos
        IncidentDao dao = AppDatabase.getDatabase(getApplicationContext()).incidentDao();
        List<Incident> pending = dao.getPending();

        if (pending.isEmpty()) return Result.success();

        // 2. Preparar texto para IA
        StringBuilder sb = new StringBuilder();
        for (Incident i : pending) sb.append("- ").append(i.toString()).append("\n");

        // 3. Llamar a Gemini (IA) [cite: 36]
        String aiSummary = GeminiHelper.generateSummary(sb.toString());

        // 4. Enviar Correo (Simulado para evitar configurar servidor SMTP complejo ahora) [cite: 40]
        sendEmailSimulation(aiSummary);

        return Result.success();
    }

    private void sendEmailSimulation(String body) {
        // Cargar email guardado en la configuración
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("FIAG_PREFS", Context.MODE_PRIVATE);
        String emailDestino = prefs.getString("admin_email", "admin@iescierva.net");

        // Imprimir en Logcat de forma limpia
        Log.i("CORREO_ENVIADO", "--------------------------------------------------");
        Log.i("CORREO_ENVIADO", "PARA: " + emailDestino);
        Log.i("CORREO_ENVIADO", "ASUNTO: Resumen Automático de Incidencias (IA)");
        Log.i("CORREO_ENVIADO", "CUERPO:\n" + body);
        Log.i("CORREO_ENVIADO", "--------------------------------------------------");
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("FIAG_PREFS", Context.MODE_PRIVATE);
        String emailDestino = prefs.getString("admin_email", "admin@iescierva.net");

        Log.i("CORREO_ENVIADO", "PARA: " + emailDestino);
    }
}
