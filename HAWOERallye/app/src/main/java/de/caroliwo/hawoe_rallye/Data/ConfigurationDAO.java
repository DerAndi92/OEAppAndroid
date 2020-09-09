package de.caroliwo.hawoe_rallye.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

//Datenbankaktionen für Konfiguration

@Dao
public interface ConfigurationDAO {

    @Insert
    void insert(ConfigurationEntity configEntity);

    @Update
    void update(ConfigurationEntity configEntity);

    @Delete
    void delete(ConfigurationEntity configEntity);

    @Query("SELECT * FROM config_table WHERE id IN (SELECT MAX(id) FROM config_table)")
    ConfigurationEntity getConfig();

    @Query("SELECT * FROM config_table WHERE id IN (SELECT MAX(id) FROM config_table)")
    LiveData<ConfigurationEntity> getConfigLiveData();

    @Query("SELECT COUNT(*) FROM config_table")
    LiveData<Integer> countEntries();

    @Query("DELETE FROM config_table")
    void deleteAllEntries();

}

