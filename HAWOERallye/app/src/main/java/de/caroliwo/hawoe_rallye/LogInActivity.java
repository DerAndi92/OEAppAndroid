package de.caroliwo.hawoe_rallye;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class LogInActivity extends AppCompatActivity {
private ArrayList<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.logInBTN);

        //Get putExtra-Data from DownloadJSON
        Intent intentFromDownload = getIntent();
        taskList = intentFromDownload.getParcelableArrayListExtra("Task List");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this, GroupActivity.class);
                intent.putParcelableArrayListExtra("Task List", taskList);
                startActivity(intent);
            }
        });
    }
}
