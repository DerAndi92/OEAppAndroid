package de.caroliwo.hawoe_rallye;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity {
    private ArrayList<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Button joinButton = findViewById(R.id.joinBTN);

        Intent intentFromLogIn = getIntent();
        taskList = intentFromLogIn.getParcelableArrayListExtra("Task List");

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupActivity.this, MainActivity.class);
                //Get putExtra-Data from LogIn
                intent.putParcelableArrayListExtra("Task List", taskList);
                startActivity(intent);
            }
        });
    }
}
