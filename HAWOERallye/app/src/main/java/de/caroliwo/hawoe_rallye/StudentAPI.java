package de.caroliwo.hawoe_rallye;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentAPI {

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
