package de.caroliwo.hawoe_rallye.Activities;

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

import de.caroliwo.hawoe_rallye.Configuration;
import de.caroliwo.hawoe_rallye.Group;
import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Task;

public class LogInActivity extends AppCompatActivity {
private ArrayList<Task> taskList;
private Configuration configuration;
private ArrayList<Group> groupsList;
private Context applicationContext;

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

        //Get Configuration from LoadingActivity
        Intent intentFromLoading = getIntent();
        configuration = intentFromLoading.getParcelableExtra("Configuration");
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
                //TODO: Keine Zahlen im Namen + Nachnamen erlaubt
                    Log.i("TEST", "onClick: Login 2");
                    if (isNewUser(userData)) {
                        Intent intent = new Intent(LogInActivity.this, GroupActivity.class);
                        intent.putParcelableArrayListExtra("Groups", groupsList);
                        Log.i("TEST", "onClick: " + groupsList.get(1).getName() + "Login 3");
                        startActivity(intent);
                    } else {
                        validateUser(userData);
                        Log.i("TEST", "onClick: Login 4");
                    }
                }
                else {
                    Toast.makeText(LogInActivity.this, "Fülle bitte alle Felder aus.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isNewUser(Map<String, String> userData) {
        // TODO: Testen ob es sich um einen neuen User handelt
        return true;
    }

    private void validateUser(Map<String, String> userData) {
        //TODO: Passwort abfragen und mit Eingabe vergleichen, wenn falsch, dann Toast PW falsch
        //final String password = configuration.getPassword(); //Passwort aus GET-Request

       /* if (password.equals()) {
            Toast.makeText(LogInActivity.this, "Falsches Passwort.", Toast.LENGTH_SHORT).show();
        } */
    }
}
