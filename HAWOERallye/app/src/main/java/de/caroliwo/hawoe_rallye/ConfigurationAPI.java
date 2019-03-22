package de.caroliwo.hawoe_rallye;

import com.google.gson.annotations.SerializedName;

//Für den API-Aufruf GET: Konfiguration laden

public class ConfigurationAPI {

    @SerializedName("data")
    private Configuration config;
    private String message;

    public Configuration getConfig() {
        return config;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
