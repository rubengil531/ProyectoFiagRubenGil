package com.example.proyectofiagrubngil;


import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Incident.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract IncidentDao incidentDao();
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "fiag_database").build();
                }
            }
        }
        return INSTANCE;
    }
}