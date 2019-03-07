package de.caroliwo.hawoe_rallye;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TaskAPI {

    @SerializedName("data")
    private Task task;
    private String message;

    public Task getTask() {
        return task;
    }

    public String getMessage() {
        return message;
    }
}
