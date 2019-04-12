package de.caroliwo.hawoe_rallye;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

//Klasse, mit der die Instanzen der einzelnen Aufgaben erstellt werden können

public class Task implements Parcelable {

    @SerializedName("id")
    private int taskId;
    private String name;
    private String icon;
    private String destination;
    private Times times;
    private boolean password;
    private boolean completed;
    @SerializedName("fields")
    private List<TaskField> fieldList;
    private int order;

    public Task() {
    }

    //GETTER + SETTER-Methoden
    public int getId() {
        return taskId;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getDestination() {
        return destination;
    }

    public Times getTime() {
        return times;
    }

    public boolean isCompleted() {
        return completed;
    }

    public List<TaskField> getFieldList() {
        return fieldList;
    }

    public int getOrder() {
        return order;
    }

    public boolean getPassword() { return password; }

    public void setTaskId(int taskId) { this.taskId = taskId; }

    public void setName(String name) { this.name = name; }

    public void setIcon(String icon) { this.icon = icon; }

    public void setDestination(String destination) { this.destination = destination; }

    public void setTimes(Times times) { this.times = times; }

    public void setPassword(boolean password) { this.password = password; }

    public void setCompleted(boolean completed) { this.completed = completed; }

    public void setFieldList(List<TaskField> fieldList) { this.fieldList = fieldList; }

    public void setOrder(int order) { this.order = order; }

    //Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.taskId);
        dest.writeString(this.name);
        dest.writeString(this.icon);
        dest.writeString(this.destination);
        dest.writeParcelable(this.times, flags);
        dest.writeByte(this.completed ? (byte) 1 : (byte) 0);
        dest.writeList(this.fieldList);
        dest.writeInt(this.order);
    }


    protected Task(Parcel in) {
        this.taskId = in.readInt();
        this.name = in.readString();
        this.icon = in.readString();
        this.destination = in.readString();
        this.times = in.readParcelable(Times.class.getClassLoader());
        this.completed = in.readByte() != 0;
        this.fieldList = new ArrayList<TaskField>();
        in.readList(this.fieldList, TaskField.class.getClassLoader());
        this.order = in.readInt();
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}



