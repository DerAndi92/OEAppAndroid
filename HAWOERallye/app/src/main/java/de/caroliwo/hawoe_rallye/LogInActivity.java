package de.caroliwo.hawoe_rallye;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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

        //Get putExtra-Data from DownloadJSON
        Intent intentFromDownload = getIntent();
        taskList = intentFromDownload.getParcelableArrayListExtra("Task List");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Passwort validieren, Eingaben aus Feldern holen .getText...
                if(password!=null && name!=null && lastname!=null && !spinner.getSelectedItem().toString().equals("Studiengang wählen")){
                    Intent intent = new Intent(LogInActivity.this, GroupActivity.class);
                    intent.putParcelableArrayListExtra("Task List", taskList);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(LogInActivity.this, "Fülle bitte alle Felder aus.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
