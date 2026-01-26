package com.example.proyectofiagrubngil;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface IncidentDao {
    @Insert
    void insert(Incident incident);

    @Update
    void update(Incident incident);

    @Query("SELECT * FROM incidents ORDER BY timestamp DESC")
    List<Incident> getAll();

    // Filtro para enviar a la IA solo las pendientes [cite: 34]
    @Query("SELECT * FROM incidents WHERE status != 'Resuelta'")
    List<Incident> getPending();
}
