package de.caroliwo.hawoe_rallye;


//Klasse, mit der die Instanz der Konfiguration erstellt werden kann

public class Configuration {
    private String password;
    private String maxTime;

    //GETTER + SETTER
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(String maxTime) {
        this.maxTime = maxTime;
    }
}
