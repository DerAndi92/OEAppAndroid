package de.caroliwo.hawoe_rallye;

import java.util.List;

public class Task {

    private String name;
    private String icon;
    private String time;
    private String destination;
    private int numberOfTasks;
    private List<String> tasks;
    boolean answerFieldNeeded;
    boolean passwordNeeded;
    boolean buttonNeeded;
    private String buttonText;

    public Task (String name, String icon, String time, String destination, int numberOfTasks, List<String> tasks,
                 boolean answerFieldNeeded, boolean passwordNeeded, boolean buttonNeeded, String buttonText) {
        this.name = name;
        this.icon = icon;
        this.time = time;
        this.destination = destination;
        this.numberOfTasks = numberOfTasks;
        this.tasks = tasks;
        this.answerFieldNeeded = answerFieldNeeded;
        this.passwordNeeded = passwordNeeded;
        this.buttonNeeded= buttonNeeded;
        this.buttonText = buttonText;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getTime() {
        return time;
    }

    public String getDestination() {
        return destination;
    }

    public int getNumberOfTasks() {
        return numberOfTasks;
    }

    public List<String> getTasks() {
        return tasks;
    }

    public boolean isAnswerFieldNeeded() {
        return answerFieldNeeded;
    }

    public boolean isPasswordNeeded() {
        return passwordNeeded;
    }

    public boolean isButtonNeeded() {
        return buttonNeeded;
    }

    public String getButtonText() {
        return buttonText;
    }

}
