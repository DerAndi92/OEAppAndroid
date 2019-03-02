package de.caroliwo.hawoe_rallye;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
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
    @PUT ("group/{groupID}")
    Call<Group> changeGroup(@Path("groupID") int groupID, @Body Group group);

    //---------------Students----------------
    //Studierenden hinzufügen
    @POST ("student")
    Call<Object> sendObject(@Body Student student);

    @POST ("student")
    Call<Student> sendStudent(@Body Student student);

    //Studierenden bearbeiten
    @PATCH("student/{studentID}")
    Call<Student> changeStudent(@Path("studentID") int studentID, @Body Student student);

    //Studierenden löschen
    @DELETE ("student/{studentID}")
    Call<Void> deleteStudent(@Path("studentID") int studentID);

    //---------------Tasks----------------
    //Alle Aufgaben einer Gruppe laden
    @GET("task/byGroup")
    Call<TaskAPI> getTasks(@Query("group") int groupID); //api/task/byGroup?group={group}

    //Eine Aufgabe einer Gruppe laden
    @GET("task/byGroup")
    Call<TaskAPI> getTask (@Query("group") int groupID);

    //Lösung einer Aufgabe abschicken
    @POST ("task/field")
    Call<Task> sendAnswer(@Body Task task);

}
