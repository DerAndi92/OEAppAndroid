package de.caroliwo.hawoe_rallye;

import com.google.gson.annotations.SerializedName;

import java.util.List;

//Group-Klasse, mit der die Instanzen der einzelnen Gruppen erstellt werden können

public class Group {

    @SerializedName("id")
    private int groupId;
    private String name;
    private String color;
    private Integer max_members;
    private Integer members;
    @SerializedName("students")
    private List<Student> studentList;

    public Group(int groupId, String name, String color, int max_members, int members, List<Student> studentList) {
        this.groupId = groupId;
        this.name = name;
        this.color = color;
        this.max_members = max_members;
        this.members = members;
        this.studentList = studentList;
    }

    public Group(int groupId, String name) {
        this.groupId = groupId;
        this.name = name;
    }

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

}
