package com.example.proyectofiagrubngil;


import org.json.JSONArray;
import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class GeminiHelper {
    // Reemplaza esto con tu API Key real de Google AI Studio
    private static final String API_KEY = "AIzaSyA6CZB3ijS9dp3gx7s4XWCo8xskJ7CsBkk";
    private static final String URL_STR = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + API_KEY;

    public static String generateSummary(String incidentsText) {
        try {
            URL url = new URL(URL_STR);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Prompt según requisitos [cite: 100, 102]
            String prompt = "Actúa como administrador de sistemas. Genera un resumen ejecutivo para enviar por email basado en estas incidencias: \n"
                    + incidentsText +
                    "\n Agrupa por Ubicación y destaca las de Prioridad Alta. Sé conciso.";

            // Construcción manual del JSON para Gemini
            JSONObject content = new JSONObject();
            JSONObject parts = new JSONObject();
            parts.put("text", prompt);
            content.put("parts", new JSONArray().put(parts));

            JSONObject payload = new JSONObject();
            payload.put("contents", new JSONArray().put(content));

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = payload.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Leer respuesta
            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) response.append(scanner.nextLine());
            scanner.close();

            // Parsear respuesta básica
            JSONObject jsonResponse = new JSONObject(response.toString());
            return jsonResponse.getJSONArray("candidates")
                    .getJSONObject(0).getJSONObject("content")
                    .getJSONArray("parts").getJSONObject(0).getString("text");

        } catch (Exception e) {
            e.printStackTrace();
            return "Error generando resumen IA: " + e.getMessage();
        }
    }
}