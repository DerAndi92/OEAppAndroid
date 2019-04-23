package de.caroliwo.hawoe_rallye;

import com.google.gson.annotations.SerializedName;


public class StudentAPI {

    //Für den API-Aufruf POST: Studierenden hinzufügen und PATCH: Studierenden bearbeiten

    @SerializedName("data")
    private Student student;
    private String message;

    public Student getStudent() {
        return student;
    }

    public String getMessage() {
        return message;
    }
}
