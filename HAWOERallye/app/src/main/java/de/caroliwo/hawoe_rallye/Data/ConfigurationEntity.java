package de.caroliwo.hawoe_rallye.Data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "config_table")
public class ConfigurationEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String password;

    private String maxTime;


    public ConfigurationEntity(String password, String maxTime) {
        this.password = password;
        this.maxTime = maxTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getMaxTime() {
        return maxTime;
    }
}
