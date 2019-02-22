package de.caroliwo.hawoe_rallye;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Student implements Parcelable {

    @SerializedName("group")
    private Integer groupId;
    @SerializedName("id")
    private Integer studentId;
    private String first_name;
    private String last_name;
    private String course;
    private int manually;

    //Konstruktor
    public Student(Integer groupId, Integer studentId ,String first_name, String last_name, String course, int manually) {
        this.groupId = groupId;
        this.studentId = studentId;
        this.first_name = first_name;
        this.last_name = last_name;
        this.course = course;
        this.manually = manually;
    }

    //GETTER + SETTER
    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int isManually() {
        return manually;
    }

    public void setManually(int manually) {
        this.manually = manually;
    }


     //Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.groupId);
        dest.writeValue(this.studentId);
        dest.writeString(this.first_name);
        dest.writeString(this.last_name);
        dest.writeString(this.course);
        dest.writeInt(this.manually);
    }

    protected Student(Parcel in) {
        this.groupId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.studentId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.first_name = in.readString();
        this.last_name = in.readString();
        this.course = in.readString();
        this.manually = in.readInt();
    }

    public static final Parcelable.Creator<Student> CREATOR = new Parcelable.Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel source) {
            return new Student(source);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };
}
