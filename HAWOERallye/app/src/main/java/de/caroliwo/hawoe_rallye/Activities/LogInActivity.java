package de.caroliwo.hawoe_rallye.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Task;

public class LogInActivity extends AppCompatActivity {
private ArrayList<Task> taskList;

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

        //Get putExtra-Data from DownloadJSON
       // Intent intentFromDownload = getIntent();
       // taskList = intentFromDownload.getParcelableArrayListExtra("Task List");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // userData-Objekt mit Inputs füllen
                userData.put("name", name.getText().toString());
                userData.put("lastname", lastname.getText().toString());
                userData.put("major", spinner.getSelectedItem().toString());
                userData.put("password", password.getText().toString());

                // Checken ob jedes Feld ausgefüllt ist
                if(userData.get("password").length() > 0 && userData.get("name").length() > 0 && userData.get("lastname").length() > 0 && !userData.get("major").equals("Studiengang wählen")){

                    if (isNewUser(userData)) {
                        Intent intent = new Intent(LogInActivity.this, GroupActivity.class);
                        // intent.putParcelableArrayListExtra("Task List", taskList);
                        startActivity(intent);
                    } else {
                        validateUser(userData);
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
        //TODO: Passwort validieren
    }
}
