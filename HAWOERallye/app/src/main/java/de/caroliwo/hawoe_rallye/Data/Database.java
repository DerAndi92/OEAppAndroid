package de.caroliwo.hawoe_rallye.Data;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

//Datenbank

@androidx.room.Database(entities = {ConfigurationEntity.class, StudentEntity.class}, version = 3, exportSchema = false)
public abstract class Database extends RoomDatabase {

    private static Database instance;

    public abstract ConfigurationDAO configDao();

    public abstract StudentDAO studDao();

    // Methode um die existierende Instanz der Datenbank bereitzustellen
    static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), Database.class, "sqlite_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
