package de.caroliwo.hawoe_rallye.Data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface ConfigurationDAO {

    @Insert
    void insert(ConfigurationEntity configEntity);

    @Update
    void update(ConfigurationEntity configEntity);

    @Delete
    void delete(ConfigurationEntity configEntity);

    @Query("SELECT * FROM config_table WHERE id IN (SELECT MAX(id) FROM config_table)")
    LiveData<ConfigurationEntity> getConfig();

    @Query("SELECT COUNT(*) FROM config_table")
    LiveData<Integer> countEntries();

    @Query("DELETE FROM config_table")
    void deleteAllEntries();

}

