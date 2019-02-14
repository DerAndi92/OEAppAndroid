package de.caroliwo.hawoe_rallye;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConfigurationAPI {

    @SerializedName("data")
    private List<Configuration> configList;
    private String message;

    public List<Configuration> getConfigList() {
        return configList;
    }

    public void setConfigList(List<Configuration> configList) {
        this.configList = configList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Configuration {
        private String password;
        private String maxTime;

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
}
