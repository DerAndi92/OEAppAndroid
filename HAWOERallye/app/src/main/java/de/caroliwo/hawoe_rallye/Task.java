package de.caroliwo.hawoe_rallye;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.sql.Time;
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

    protected Task(Parcel in) {
        taskId = in.readInt();
        name = in.readString();
        icon = in.readString();
        destination = in.readString();
        completed = in.readByte() != 0;
        order = in.readInt();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(taskId);
        dest.writeString(name);
        dest.writeString(icon);
        dest.writeString(destination);
        dest.writeByte((byte) (completed ? 1 : 0));
        dest.writeInt(order);
    }

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


    public class Times {

        private String time_from;
        private String time_to;

        public String getTime_from() {
            return time_from;
        }

        public String getTime_to() {
            return time_to;
        }
    }

    public class Field {
        private int id;
        private String type;
        private String value;
        private int order;
    }

}



