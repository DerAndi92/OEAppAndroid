package de.caroliwo.hawoe_rallye;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnswerAPI {

    //Für den API-Aufruf POST: Lösung einer Aufgabe abschicken

    @SerializedName("data")
    private Answer answer;
    private String message;

    public Answer getAnswer() {
        return answer;
    }

    public String getMessage() {
        return message;
    }
}
