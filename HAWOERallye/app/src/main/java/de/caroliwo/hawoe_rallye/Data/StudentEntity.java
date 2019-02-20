package de.caroliwo.hawoe_rallye.Data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity (tableName = "student_table")
public class StudentEntity {

    @PrimaryKey (autoGenerate = true)
    private int id;
    private String first_name;
    private String last_name;
    private String course;
    private int groupId;


    public StudentEntity(String first_name, String last_name, String course, int groupId) {
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
}
