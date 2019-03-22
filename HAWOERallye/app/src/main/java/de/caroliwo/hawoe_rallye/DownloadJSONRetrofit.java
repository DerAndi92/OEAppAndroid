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

    //Hier werden die Requests an die API definiert, die von Retrofit genutzt werden

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
    Call<StudentAPI> sendStudent(@Body Student student);

    //Studierenden bearbeiten
    @PATCH("student/{studentID}")
    Call<StudentAPI> changeStudent(@Path("studentID") int studentID, @Body Student student);

    //Studierenden löschen
    @DELETE ("student/{studentID}")
    Call<Void> deleteStudent(@Path("studentID") int studentID);

    //---------------Tasks----------------
    //Alle Aufgaben einer Gruppe laden
    @GET("task/byGroup")
    Call<TasksAPI> getTasks(@Query("group") int groupID); //api/task/byGroup?group={group}

    //Eine Aufgabe einer Gruppe laden
    @GET("task/{taskID}/byGroup")
    Call<TaskAPI> getTask (@Path("taskID") int taskID, @Query("group") int groupID);

    //Lösung einer Aufgabe abschicken
    @POST ("task/field")
    Call<AnswerAPI> sendAnswer(@Body Answer answer);

}
