package de.caroliwo.hawoe_rallye;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import java.util.Date;
import io.reactivex.annotations.NonNull;

public class User {


    private int id;
    private String name;
    private String lastname;
    private String subject;
    private Date logInDate;


public User (String name, String lastname, String subject, Date logInDate){
    this.name = name;
    this.lastname = lastname;
    this.subject = subject;
    this.logInDate=logInDate;
}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getSubject() {
        return subject;
    }

    public Date getLogInDate() {
        return logInDate;
    }
}
