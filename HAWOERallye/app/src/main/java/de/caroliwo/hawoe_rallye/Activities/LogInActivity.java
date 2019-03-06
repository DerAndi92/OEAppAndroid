package de.caroliwo.hawoe_rallye.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import de.caroliwo.hawoe_rallye.Configuration;
import de.caroliwo.hawoe_rallye.Data.ConfigurationEntity;
import de.caroliwo.hawoe_rallye.Data.DataViewModel;
import de.caroliwo.hawoe_rallye.Group;
import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Student;
import de.caroliwo.hawoe_rallye.Task;

public class LogInActivity  extends AppCompatActivity  {
private ArrayList<Task> taskList;
private Configuration configuration;
private ArrayList<Group> groupsList;
private Context applicationContext;
private ConfigurationEntity configEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText name = findViewById(R.id.nameET);
        final EditText lastname = findViewById(R.id.lastnameET);
        final Spinner spinner = findViewById(R.id.MTMS_spinner);
        final EditText password = findViewById(R.id.passwordET);
        Button loginButton = findViewById(R.id.logInBTN);
        final Map<String, String> userData = new HashMap<>();

        // ViewModel für Daten aus Datenbank (über Repository)
        DataViewModel viewModel = ViewModelProviders.of(this).get(DataViewModel.class);
        Log.i("LogInActivity", "viewModel.getConfig().getValue(): " + configEntity);

        // Konfigurations aus Datenbank holen
        configEntity = viewModel.getConfig();

        // Konfigurations-LiveData observieren
        viewModel.getConfigLiveData().observe(this, new Observer<ConfigurationEntity>() {
            @Override
            public void onChanged(@Nullable ConfigurationEntity configurationEntity) {

                // Bei geladener Konfiguration neu zuweisen
                configEntity = configurationEntity;
                Log.i("LogInActivity", "configEntity assigned by Observer: " + configEntity);
            }
        });

        // Groups holen <-------------------------ERSETZT DURCH DIREKTE DATENABFRAGE ÜBER VIEWMODEL
//        Intent intentFromLoading = getIntent();
//        groupsList = intentFromLoading.getParcelableArrayListExtra("Groups");
//        Log.i("TEST", "onClick: " + groupsList.get(1).getColor() + " Login 1a");


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {

                Log.i("TEST", "onClick Login 1aaaa");
                // userData-Objekt mit Inputs füllen
                userData.put("name", name.getText().toString());
                userData.put("lastname", lastname.getText().toString());
                String majorTemp = spinner.getSelectedItem().toString();
                majorTemp = majorTemp.equals("Medientechnik") ? "MT" : (majorTemp.equals("Media Systems") ? "MS" : majorTemp);
                userData.put("major", majorTemp);
                userData.put("password", password.getText().toString());
                Log.i("TEST", "onClick Login 1b");

                Student student = new Student(null, null, userData.get("name"), userData.get("lastname"), userData.get("major"), 1);

                Log.i("TEST", "onClick Login 1c");

                // Checken ob jedes Feld ausgefüllt ist
                if(userData.get("password").length() > 0 && userData.get("name").length() > 0 && userData.get("lastname").length() > 0 && !userData.get("major").equals("Studiengang wählen")){

                    // Auf Zahlen im Namen checken
                    if (stringContainsNumber(userData.get("name")) | stringContainsNumber(userData.get("lastname"))) {
                        Toast.makeText(LogInActivity.this, "Keine Zahlen in Namen erlaubt", Toast.LENGTH_SHORT).show();
                    }else {
                        //Checken ob Passwort korrekt
                        Log.i("TEST", "onClick: Login 2");
                        if (passwordCorrect(userData.get("password"))) {
                            Intent intent = new Intent(LogInActivity.this, GroupActivity.class);
//                            intent.putParcelableArrayListExtra("Groups", groupsList); <--------------------ERSETZT DURCH DIREKTE DATENABFRAGE ÜBER VIEWMODEL
                            intent.putExtra("StudentData", student);
                            startActivity(intent);

                        } else {
                            Toast.makeText(LogInActivity.this, "Falsches Passwort", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(LogInActivity.this, "Fülle bitte alle Felder aus.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean passwordCorrect(String password) {
        if (!TextUtils.isEmpty(password)) {
            return password.equals(configEntity.getPassword());
        } else {
            return false;
        }
    }


    public boolean stringContainsNumber( String s )
    {
        return Pattern.compile( "[0-9]" ).matcher( s ).find();
    }
}
