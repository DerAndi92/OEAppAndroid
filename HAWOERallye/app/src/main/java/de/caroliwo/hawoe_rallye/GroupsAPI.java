package de.caroliwo.hawoe_rallye;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupsAPI {

    //Für den API-Aufruf GET: Alle Gruppen laden

    @SerializedName("data")
    private List<Group> groupList;
    private String message;

    //GETTER + SETTER
    public List<Group> getGroupList() {
        return groupList;
    }
    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
