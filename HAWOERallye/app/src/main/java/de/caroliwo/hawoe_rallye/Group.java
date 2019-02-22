package de.caroliwo.hawoe_rallye;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Group implements Parcelable {

    @SerializedName("id")
    private int groupId;
    private String name;
    private String color;
    private int max_members;
    private int members;
    @SerializedName("students")
    private List<Student> studentList;


    //GETTER + SETTER
    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getMax_members() {
        return max_members;
    }

    public void setMax_members(int max_members) {
        this.max_members = max_members;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.groupId);
        dest.writeString(this.name);
        dest.writeString(this.color);
        dest.writeInt(this.max_members);
        dest.writeInt(this.members);
        dest.writeTypedList(this.studentList);
    }

    public Group() {
    }

    protected Group(Parcel in) {
        this.groupId = in.readInt();
        this.name = in.readString();
        this.color = in.readString();
        this.max_members = in.readInt();
        this.members = in.readInt();
        this.studentList = in.createTypedArrayList(Student.CREATOR);
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel source) {
            return new Group(source);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };
}
