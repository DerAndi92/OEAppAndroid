package de.caroliwo.hawoe_rallye;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Student implements Parcelable {

    @SerializedName("group")
    private int groupId;
    @SerializedName("id")
    private int studentId;
    private String first_name;
    private String last_name;
    private String course;
    private int manually;

    protected Student(Parcel in) {
        groupId = in.readInt();
        studentId = in.readInt();
        first_name = in.readString();
        last_name = in.readString();
        course = in.readString();
        manually = in.readInt();
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(groupId);
        dest.writeInt(studentId);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(course);
        dest.writeInt( (manually));
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
}
