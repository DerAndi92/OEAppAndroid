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

import android.arch.lifecycle.ViewModelProviders;

import de.caroliwo.hawoe_rallye.Data.ConfigurationEntity;
import de.caroliwo.hawoe_rallye.Data.DataViewModel;
import de.caroliwo.hawoe_rallye.Data.StudentEntity;
import de.caroliwo.hawoe_rallye.Group;
import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Student;
import de.caroliwo.hawoe_rallye.Task;


public class LoadingActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private Context applicationContext;
    private DataViewModel viewModel;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // ViewModel für Daten
        viewModel = ViewModelProviders.of(this).get(DataViewModel.class);
        // Student aus Datenbank laden
        StudentEntity studentEntity = viewModel.getStudent();

        // Progressbar zuweisen
        progressBar = findViewById(R.id.progressBar);

        // Kontext für Intent
        applicationContext = getApplicationContext();

        // Checken ob ein Student geladen wurde
        if (studentEntity != null) {

            // Bei existierendem Eintrag: Intent zur Main-Activity
            intent = new Intent(applicationContext, MainActivity.class);

            // Gruppen-ID holen
            int groupID = studentEntity.getGroupId();

            // Aufgaben der Gruppe aus API laden
            viewModel.fetchTasks(groupID);

            // Laden der Aufgaben per LiveData observieren
            viewModel.getTaskListLiveData().observe(this, new Observer<ArrayList<Task>>() {
                @Override
                public void onChanged(@Nullable ArrayList<Task> groups) {

                    // bei geladenen Aufgaben Progressbar erhöhen
                    progressBar.setProgress(progressBar.getProgress() + 50);
                    progressCheck();
                }
            });

            // Gruppenmitglieder laden
            viewModel.fetchGroup(groupID);

            // Laden der Gruppenmitglieder per LiveData observieren
            viewModel.getStudentListLiveData().observe(this, new Observer<ArrayList<Student>>() {
                @Override
                public void onChanged(@Nullable ArrayList<Student> students) {

                    // bei geladenen Gruppenmitgliedern Progressbar erhöhen
                    progressBar.setProgress(progressBar.getProgress() + 50);
                    progressCheck();
                }
            });

        } else {
            // Bei neuem User: Intent zur Login-Activity
            intent = new Intent(applicationContext, LogInActivity.class);

            // Konfiguration laden
            viewModel.fetchConfig();

            // Laden der Konfiguration per LiveData observieren
            viewModel.getConfigLiveData().observe(this, new Observer<ConfigurationEntity>() {
                @Override
                public void onChanged(@Nullable ConfigurationEntity configurationEntity) {

                    // bei geladener Konfiguration Progressbar erhöhen
                    progressBar.setProgress(progressBar.getProgress() + 50);
                    progressCheck();
                }
            });

            // Gruppen laden
            viewModel.fetchGroups();

            // Laden der Gruppen observieren
            viewModel.getGroupListLiveData().observe(this, new Observer<ArrayList<Group>>() {
                @Override
                public void onChanged(@Nullable ArrayList<Group> groups) {

                    // bei geladenen Gruppen Progressbar erhöhen
                    progressBar.setProgress(progressBar.getProgress() + 50);
                    progressCheck();
                }
            });
        }
    }

    public void progressCheck() {
        if (progressBar.getProgress() == 100) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            applicationContext.startActivity(intent);
        }
    }
}


