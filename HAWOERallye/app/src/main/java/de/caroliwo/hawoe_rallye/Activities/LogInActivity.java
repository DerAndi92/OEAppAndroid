package de.caroliwo.hawoe_rallye.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import de.caroliwo.hawoe_rallye.Configuration;
import de.caroliwo.hawoe_rallye.Data.ConfigurationEntity;
import de.caroliwo.hawoe_rallye.Data.DataViewModel;
import de.caroliwo.hawoe_rallye.Group;
import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Task;

public class LogInActivity extends AppCompatActivity {
private ArrayList<Task> taskList;
private Configuration configuration;
private ArrayList<Group> groupsList;
private Context applicationContext;
private DataViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText name = findViewById(R.id.nameET);
        final EditText lastname = findViewById(R.id.lastnameET);
        final Spinner spinner = findViewById(R.id.MTMS_spinner);
        final EditText password = findViewById(R.id.passwordET);
        Button loginButton = findViewById(R.id.logInBTN);
        final Map<String, String> userData = new HashMap<>();

        // ViewModel für Daten aus Datenbank (über Repository)
        viewModel = ViewModelProviders.of(this).get(DataViewModel.class);

        // Testkonfiguration in Datenbank einfügen
        viewModel.insertConfig(new ConfigurationEntity("wassport", "12.03.2019"));

        //Get Groups from LoadingActivity
        Intent intentFromLoading = getIntent();
        groupsList = intentFromLoading.getParcelableArrayListExtra("Groups");
        Log.i("TEST", "onClick: " + groupsList.get(1).getColor() + " Login 1a");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // userData-Objekt mit Inputs füllen
                userData.put("name", name.getText().toString());
                userData.put("lastname", lastname.getText().toString());
                userData.put("major", spinner.getSelectedItem().toString());
                userData.put("password", password.getText().toString());

                Log.i("TEST", "onClick Login 1b");

                // Checken ob jedes Feld ausgefüllt ist
                if(userData.get("password").length() > 0 && userData.get("name").length() > 0 && userData.get("lastname").length() > 0 && !userData.get("major").equals("Studiengang wählen")){

                    // Auf Zahlen im Namen checken
                    if (stringContainsNumber(userData.get("name")) | stringContainsNumber(userData.get("lastname"))) {
                        Toast.makeText(LogInActivity.this, "Keine Zahlen in Namen erlaubt", Toast.LENGTH_SHORT).show();
                    }else {

                        Log.i("TEST", "onClick: Login 2");
                        if (passwordCorrect(userData.get("password"))) {
                            Intent intent = new Intent(LogInActivity.this, GroupActivity.class);
                            intent.putParcelableArrayListExtra("Groups", groupsList);
                            Log.i("TEST", "onClick: " + groupsList.get(1).getName() + "Login 3");
                            startActivity(intent);
                        } else {
                            Toast.makeText(LogInActivity.this, "Passwort: " + viewModel.getConfig().getPassword(), Toast.LENGTH_SHORT).show();
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
        if (password.equals(viewModel.getConfig().getPassword() != null)) {
            return password.equals(viewModel.getConfig().getPassword());
        } else {
            return false;
        }
    }


    public boolean stringContainsNumber( String s )
    {
        return Pattern.compile( "[0-9]" ).matcher( s ).find();
    }
}
