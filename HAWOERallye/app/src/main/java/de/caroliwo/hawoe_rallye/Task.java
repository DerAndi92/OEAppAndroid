package de.caroliwo.hawoe_rallye;

import java.sql.Time;


public class Task {

    private int id;
    private String name;
    private String icon;
    private String destination;
    private Times times;
    private boolean completed;
    //private List<Field> fieldList;
    private int order;

    //GETTER-Methoden
    public int getId() {
        return id;
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

   /* public List<Field> getFieldList() {
        return fieldList;
    }*/

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

   /* //Parcelable Konstruktor
    protected Task(Parcel in) { //needs to have the same order as writeToParcel-Method
        name = in.readString();
        icon = in.readString();
        time = in.readString();
        destination = in.readString();
    }*/

        //Parcelable Methods
   /* @Override
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
    }; */

}



