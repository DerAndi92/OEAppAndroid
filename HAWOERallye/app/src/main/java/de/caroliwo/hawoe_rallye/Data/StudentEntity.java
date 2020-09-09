package de.caroliwo.hawoe_rallye.Data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//Entität Student der Datenbank

@Entity(tableName = "student_table")
public class StudentEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int studentId;
    private String first_name;
    private String last_name;
    private String course;
    private int groupId;

    public StudentEntity(int studentId, String first_name, String last_name, String course, int groupId) {
        this.studentId = studentId;
        this.first_name = first_name;
        this.last_name = last_name;
        this.course = course;
        this.groupId = groupId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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

    public String getLast_name() {
        return last_name;
    }

    public String getCourse() {
        return course;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "studentId: " + studentId + " --- name: " + first_name + " " + last_name + " --- course: " + course + " --- groupId: " + groupId;
    }
}
