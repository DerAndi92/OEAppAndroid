package de.caroliwo.hawoe_rallye;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnswerAPI {

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
