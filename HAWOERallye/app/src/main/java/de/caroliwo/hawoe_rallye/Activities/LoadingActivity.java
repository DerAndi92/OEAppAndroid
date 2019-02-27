package de.caroliwo.hawoe_rallye.Activities;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import android.arch.lifecycle.ViewModelProviders;
import android.widget.Toast;

import de.caroliwo.hawoe_rallye.ConfigurationAPI;
import de.caroliwo.hawoe_rallye.Data.ConfigurationEntity;
import de.caroliwo.hawoe_rallye.Data.DataViewModel;
import de.caroliwo.hawoe_rallye.Data.StudentEntity;
import de.caroliwo.hawoe_rallye.DownloadJSONRetrofit;
import de.caroliwo.hawoe_rallye.Group;
import de.caroliwo.hawoe_rallye.GroupAPI;
import de.caroliwo.hawoe_rallye.GroupsAPI;
import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Retrofit;
import de.caroliwo.hawoe_rallye.Configuration;
import de.caroliwo.hawoe_rallye.Student;
import de.caroliwo.hawoe_rallye.Task;
import de.caroliwo.hawoe_rallye.TaskAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private DownloadJSONRetrofit downloadJSONRetrofit;
    private Configuration configuration;
    private List<Group> groups;
    private ArrayList<Group> groupsList;
    private Context applicationContext;
    private DataViewModel viewModel;
    private StudentEntity student;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("LoadingActivity", "1");
        setContentView(R.layout.activity_loading);
        Log.i("LoadingActivity", "2");

        // ViewModel für Datenbank (über Repository)
        viewModel = ViewModelProviders.of(this).get(DataViewModel.class);
        student = viewModel.getStudent();

        //Progressbar
        progressBar = findViewById(R.id.progressBar);
        Log.i("LoadingActivity", "4");

        //für Intent
        applicationContext = getApplicationContext();
        Log.i("LoadingActivity", "5");


        //Retrofit
        Retrofit retrofitClass = new Retrofit();
        Log.i("LoadingActivity", "6");
        downloadJSONRetrofit = retrofitClass.createlogInterceptor();
        Log.i("LoadingActivity", "7");

        // Zum testen: Alle Einträge löschen bzw Student in Datenbank einfügen (App zweimal starten)
        //viewModel.deleteAllStudents();
        //viewModel.insertStudent(new StudentEntity("Karl", "Mustermann", "Medientechnik", 1));

        // Checken ob ein Student übergeben wurde
        if (student != null) {
            Log.i("LoadingActivity", "student vorhanden ");
            // Bei existierendem Eintrag: Intent zur Main-Activity
            intent = new Intent(applicationContext, MainActivity.class);
            // Gruppen-ID zuweisen & Aufgaben/Gruppe laden
            int groupID = student.getGroupId();

            getTasks(groupID); //für Zeiten und Aufgaben
            getGroup(groupID); //Eigene Gruppe laden
            Log.i("LoadingActivity","8");

        } else {
            // Bei neuem User: Intent zur Login-Activity
            intent = new Intent(applicationContext, LogInActivity.class);
            Log.i("LoadingActivity","9");
            getConfig();
            getGroups();
        }


    }

    //Methoden, um HTTP-Request durchzuführen

    //Konfigurationen laden
    public void getConfig () {
        Call<ConfigurationAPI> call = downloadJSONRetrofit.getConfiguration();

        //execute on background-thread
        call.enqueue(new Callback<ConfigurationAPI>() {
            @Override
            public void onResponse(Call<ConfigurationAPI> call, Response<ConfigurationAPI> response) {
                //wenn HTTP-Request nicht erfolgreich:
                if (!response.isSuccessful()) {
                    Log.i("TEST ErrorResponse: ", String.valueOf(response.code()));
                    return;
                }

                //wenn HTTP-Request erfolgreich:
                ConfigurationAPI configurationAPI = response.body();
                configuration = configurationAPI.getConfig();

                //configuration im Intent übergeben
                intent.putExtra("Configuration", configuration);

                // Passwort und maxTime über viewModel in interner Datenbank speichern
                viewModel.deleteAllConfigs();
                viewModel.insertConfig(new ConfigurationEntity("bla"/*configuration.getPassword()*/, configuration.getMaxTime()));
                Log.i("LoadingActivity", "configuration.getPassword(): " + configuration.getPassword());

                progressBar.setProgress(progressBar.getProgress()+50);
                Log.i("LoadingActivity","Configuration loaded");
                progressCheck ();
            }

            @Override
            public void onFailure(Call<ConfigurationAPI> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("TEST Error", t.getMessage() + "Error");
            }
        });
    }

    //Erstes Laden der Gruppen
   public void getGroups () {
       Call<GroupsAPI> call = downloadJSONRetrofit.getGroups();

       //execute on background-thread
       call.enqueue(new Callback<GroupsAPI>() {
           @Override
           public void onResponse(Call<GroupsAPI> call, Response<GroupsAPI> response) {

               //wenn HTTP-Request nicht erfolgreich:
               if (!response.isSuccessful()) {
                   Log.i("TEST ErrorResponse: ", String.valueOf(response.code()));
                   return;
               }

               //wenn HTTP-Request erfolgreich:
               GroupsAPI groupsAPI = response.body();
               groups = groupsAPI.getGroupList();
               groupsList = new ArrayList<>(groups);

               //groupsList im Intent übergeben
               intent.putParcelableArrayListExtra("Groups", groupsList);

               progressBar.setProgress(progressBar.getProgress()+50);
               Log.i("LoadingActivity","Groups loaded");
               progressCheck();
           }

           @Override
           public void onFailure(Call<GroupsAPI> call, Throwable t) {
               // something went completely south (like no internet connection)
               Log.i("TEST Error", t.getMessage() + "Error");
           }
       });
    }

    //Tasks der eigenen Gruppe laden
    private void getTasks(int groupID) {
        Call<TaskAPI> call = downloadJSONRetrofit.getTasks(groupID);

        //execute on background-thread
        call.enqueue(new Callback<TaskAPI>() {
            @Override
            public void onResponse(Call<TaskAPI> call, Response<TaskAPI> response) {

                //wenn HTTP-Request nicht erfolgreich:
                if (!response.isSuccessful()) {
                    Log.i("TEST Error get Tasks ", String.valueOf(response.code()));
                    return;
                }

                //wenn HTTP-Request erfolgreich:
                TaskAPI taskAPI = response.body();
                List<Task> tasks = taskAPI.getTaskList();
                ArrayList<Task> taskList = new ArrayList<>(tasks); //List in ArrayList umwandeln
                intent.putParcelableArrayListExtra("Tasks", taskList);
                progressBar.setProgress(progressBar.getProgress()+50);
                Log.i("LoadingActivity","Tasks loaded: " + taskList);
                progressCheck();
            }

            @Override
            public void onFailure(Call<TaskAPI> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("TEST Error", t.getMessage() + "Error");
            }
        });
    }

    //Eigene Gruppe laden
    private void getGroup(int groupID) {
        Call<GroupAPI> call = downloadJSONRetrofit.getGroup(groupID);

        //execute on background-thread
        call.enqueue(new Callback<GroupAPI>() {
            @Override
            public void onResponse(Call<GroupAPI> call, Response<GroupAPI> response) {

                //wenn HTTP-Request nicht erfolgreich:
                if (!response.isSuccessful()) {
                    Log.i("TEST Error: getGroup ", String.valueOf(response.code()));
                    return;
                }

                //wenn HTTP-Request erfolgreich:
                GroupAPI groupAPI = response.body();
                List<Student>students = groupAPI.getGroup().getStudentList();
                ArrayList<Student>studentList= new ArrayList<>(students); //List in ArrayList umwandeln
                intent.putParcelableArrayListExtra("Students", studentList);
                progressBar.setProgress(progressBar.getProgress()+50);
                Log.i("LoadingActivity","Group " + groupAPI.getGroup().getName() + " Loaded");
                progressCheck();
            }

            @Override
            public void onFailure(Call<GroupAPI> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("TEST Error", t.getMessage() + "Error");
            }
        });
    }

    public void progressCheck (){
        if (progressBar.getProgress() == 100) {
            applicationContext.startActivity(intent);
            Log.i("Test", "start from LoadingActivity");
        }
    }
}


