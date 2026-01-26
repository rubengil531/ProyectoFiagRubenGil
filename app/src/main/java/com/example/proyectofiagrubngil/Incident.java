package com.example.proyectofiagrubngil;

import androidx.room.Entity;
import androidx.room.Ignore; // Importante añadir esto
import androidx.room.PrimaryKey;

@Entity(tableName = "incidents")
public class Incident {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String location;
    public String priority;
    public String deviceType;
    public String deviceId;
    public String description;
    public long timestamp;
    public String status;

    // 1. CONSTRUCTOR VACÍO (Necesario para que Room pueda leer de la BD)
    public Incident() {
    }

    // 2. CONSTRUCTOR PARA INSERTAR (Usamos @Ignore para que Room sepa que este es el auxiliar)
    @Ignore
    public Incident(String location, String priority, String deviceType, String deviceId, String description, long timestamp, String status) {
        this.location = location;
        this.priority = priority;
        this.deviceType = deviceType;
        this.deviceId = deviceId;
        this.description = description;
        this.timestamp = timestamp;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Ubicación: " + location + ", Prioridad: " + priority + ", Equipo: " + deviceType + " (" + deviceId + "), Problema: " + description;
    }
}