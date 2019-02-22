package de.caroliwo.hawoe_rallye;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


public class Task implements Parcelable {

    @SerializedName("id")
    private int taskId;
    private String name;
    private String icon;
    private String destination;
    private Times times;
    private boolean completed;
    private List<Field> fieldList;
    private int order;


    //GETTER-Methoden
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

    public List<Field> getFieldList() {
        return fieldList;
    }

    public int getOrder() {
        return order;
    }


    public class Field {
        private int id;
        private String type;
        private String value;
        private int order;
    }

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

    public Task() {
    }

    protected Task(Parcel in) {
        this.taskId = in.readInt();
        this.name = in.readString();
        this.icon = in.readString();
        this.destination = in.readString();
        this.times = in.readParcelable(Times.class.getClassLoader());
        this.completed = in.readByte() != 0;
        this.fieldList = new ArrayList<Field>();
        in.readList(this.fieldList, Field.class.getClassLoader());
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



