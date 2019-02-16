package de.caroliwo.hawoe_rallye;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DownloadJSONRetrofit {

    //Konfiguration laden
    @GET ("configuration")
    Call<ConfigurationAPI> getConfiguration();

    //---------------Groups----------------
    //Alle Gruppen laden
    @GET ("group")
    Call<GroupsAPI> getGroups();

    //Gruppe mit Studierenden laden
    @GET ("group/{groupID}")
    Call<GroupAPI> getGroup (@Path("groupID") int groupID);

    //Gruppe bearbeiten
    //@PUT ("group/")

    //---------------Students----------------
    //Studierenden hinzufügen
    //@POST ("student")

    //Studierenden bearbeiten
    //@PUT ("student/{studentID}")

    //Studierenden löschen
    //@DELETE ("student/{studentID}")

    //---------------Tasks----------------
    //Alle Aufgaben einer Gruppe laden
    @GET("task/byGroup")
    Call<TaskAPI> getTasks(@Query("group") int groupID); //api/task/byGroup?group={group}

    //Eine Aufgabe einer Gruppe laden
    @GET("task/byGroup")
    Call<TaskAPI> getTask (@Query("group") int groupID);

    //Input zu einem Feld hinzufügen


}
