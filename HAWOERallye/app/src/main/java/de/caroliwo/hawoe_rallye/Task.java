package de.caroliwo.hawoe_rallye;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Task implements Parcelable {

    private String name;
    private String icon;
    private String time;
    private String destination;
    private int numberOfTasks;
    //private List<String> tasks;
    private boolean answerFieldNeeded;
    private boolean passwordNeeded;
    private boolean buttonNeeded;
    private String buttonText;

    //Konstruktor
    public Task (String name, String icon, String time, String destination, int numberOfTasks, /*List<String> tasks,*/
                 boolean answerFieldNeeded, boolean passwordNeeded, boolean buttonNeeded, String buttonText) {
        this.name = name;
        this.icon = icon;
        this.time = time;
        this.destination = destination;
        this.numberOfTasks = numberOfTasks;
       // this.tasks = tasks;
        this.answerFieldNeeded = answerFieldNeeded;
        this.passwordNeeded = passwordNeeded;
        this.buttonNeeded= buttonNeeded;
        this.buttonText = buttonText;
    }

    //Parcelable Konstruktor
    protected Task(Parcel in) { //needs to have the same order as writeToParcel-Method
        name = in.readString();
        icon = in.readString();
        time = in.readString();
        destination = in.readString();
        numberOfTasks = in.readInt();
        answerFieldNeeded = in.readByte() != 0;
        passwordNeeded = in.readByte() != 0;
        buttonNeeded = in.readByte() != 0;
        buttonText = in.readString();
    }

    //Getter
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

    //public List<String> getTasks() {return tasks;}

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


    //Parcelable Methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(icon);
        dest.writeString(time);
        dest.writeString(destination);
        dest.writeInt(numberOfTasks);
        dest.writeByte((byte) (answerFieldNeeded ? 1 : 0));
        dest.writeByte((byte) (passwordNeeded ? 1 : 0));
        dest.writeByte((byte) (buttonNeeded ? 1 : 0));
        dest.writeString(buttonText);
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
