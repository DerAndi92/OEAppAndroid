package de.caroliwo.hawoe_rallye;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Answer {

    @SerializedName("group")
    private int groupID;
    @SerializedName("task")
    private int taskID;
    private String password;
    private List<AnswerField> inputs;

    // Konstruktor

    public Answer(int groupID, int taskID, String password, List<AnswerField> inputs) {
        this.groupID = groupID;
        this.taskID = taskID;
        this.password = password;
        this.inputs = inputs;
    }


    // Getter + Setter

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<AnswerField> getInputs() {
        return inputs;
    }

    public void setInputs(List<AnswerField> inputs) {
        this.inputs = inputs;
    }
}
