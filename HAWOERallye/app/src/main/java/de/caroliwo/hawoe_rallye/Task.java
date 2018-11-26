package de.caroliwo.hawoe_rallye;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Task implements Parcelable {

    private String name;
    private String icon;
    private String time;
    private String destination;
    //private Form form; enthält Aufbaufelder für Layout

    //Konstruktor
    public Task (String name, String icon, String time, String destination, String buttonText) {
        this.name = name;
        this.icon = icon;
        this.time = time;
        this.destination = destination;
    }

    //Parcelable Konstruktor
    protected Task(Parcel in) { //needs to have the same order as writeToParcel-Method
        name = in.readString();
        icon = in.readString();
        time = in.readString();
        destination = in.readString();
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
