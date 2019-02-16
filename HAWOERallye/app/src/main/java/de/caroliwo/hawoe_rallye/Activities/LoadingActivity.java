package de.caroliwo.hawoe_rallye.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import de.caroliwo.hawoe_rallye.ConfigurationAPI;
import de.caroliwo.hawoe_rallye.DownloadJSONRetrofit;
import de.caroliwo.hawoe_rallye.Group;
import de.caroliwo.hawoe_rallye.GroupsAPI;
import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Retrofit;
import de.caroliwo.hawoe_rallye.Configuration;
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
    Intent intent;

    //TODO: Beim Starten der App feststellen, ob bereits Nutzer angelegt, wenn nicht: normaler Ablauf; wenn Nutzer bereits eingeloggt: mit zweiter LoadingActivity starten (die nach der Gruppenwahl)
    //Idee dazu: Checken, ob interne Datenbank schon GroupID enthält....
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        //Progressbar
        progressBar = findViewById(R.id.progressBar);

        //Intent erstellen
        applicationContext = getApplicationContext();
        intent = new Intent(applicationContext, LogInActivity.class);

        //Retrofit
        Retrofit retrofitClass = new Retrofit();
        downloadJSONRetrofit = retrofitClass.createlogInterceptor();

        //Methode, um HTTP-Request durchzuführen
        getConfig();
        getGroups();

    }

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
                //TODO: configuration.getPassword und configuration.getMaxTime in interne Datenbank speichern
                progressBar.setProgress(progressBar.getProgress()+50);
                Log.i("TEST", "configuration");
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
               //TODO: groups per Parcelable an LogIn und dann an GroupActivity weiterleiten
               //groupsList im Intent übergeben
               intent.putParcelableArrayListExtra("Groups", groupsList);
               //LogIn Activity starten
               progressBar.setProgress(progressBar.getProgress()+50);
               Log.i("TEST", "groupsList");
               progressCheck();
           }

           @Override
           public void onFailure(Call<GroupsAPI> call, Throwable t) {
               // something went completely south (like no internet connection)
               Log.i("TEST Error", t.getMessage() + "Error");
           }
       });
    }

    public void progressCheck (){
        if (progressBar.getProgress() == 100) {
            applicationContext.startActivity(intent);
            Log.i("Test", "startActivity");
        }
    }
}


