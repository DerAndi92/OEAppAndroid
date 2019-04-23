package de.caroliwo.hawoe_rallye;

import com.google.gson.annotations.SerializedName;

import java.util.List;

//Für den API-Aufruf GET: Alle Aufgaben einer Gruppe laden

public class TasksAPI {

    @SerializedName("data")
    private List<Task> taskList;
    private String message;

    //GETTER
    public List<Task> getTaskList() {
        return taskList;
    }

    public String getMessage() {
        return message;
    }
}


