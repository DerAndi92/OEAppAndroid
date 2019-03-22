package de.caroliwo.hawoe_rallye;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupAPI {

    //Für den API-Aufruf GET: Eine Gruppe laden

    @SerializedName("data")
    private Group group;
    private String message;

    //GETTTER + SETTER

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



}
