package de.caroliwo.hawoe_rallye;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TaskAPI {

    @SerializedName("data")
    private List<Task> taskList;
    private String message;

    public List<Task> getTaskList() {
        return taskList;
    }

    public String getMessage() {
        return message;
    }
}
