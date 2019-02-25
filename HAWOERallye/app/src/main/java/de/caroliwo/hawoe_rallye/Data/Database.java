package de.caroliwo.hawoe_rallye.Data;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@android.arch.persistence.room.Database(entities = {ConfigurationEntity.class, StudentEntity.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {

    private static Database instance;

    public abstract ConfigurationDAO configDao();

    public abstract StudentDAO studDao();

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
