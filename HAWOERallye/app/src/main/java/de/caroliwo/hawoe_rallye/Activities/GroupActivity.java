package de.caroliwo.hawoe_rallye.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import de.caroliwo.hawoe_rallye.GroupItem;
import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Task;

public class GroupActivity extends AppCompatActivity {
    private ArrayList<Task> taskList;
    private ArrayList<GroupItem> groupList;
    private RecyclerView recyclerView;
    private boolean debug = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (debug) Log.i("GroupActivity-Log","1");
        super.onCreate(savedInstanceState);
//        if (debug) Log.i("GroupActivity-Log","2");
        setContentView(R.layout.activity_group);
//        if (debug) Log.i("GroupActivity-Log","3");
        recyclerView = findViewById(R.id.chooseGroupRV);
//        if (debug) Log.i("GroupActivity-Log","4");

        //RecyclerView mit Gruppen füllen
        groupList = new ArrayList<>();
        String[] testNames = {"ROT","BLAU","GRÜN","GELB","PINK","LILA","ORANGE","SILBER","SCHWARZ","WEISS"};
        String[] testColors = {"#ff0000","#0000ff","#00ff00","#ffff00","#ff00ff","#9600c8","#ff7d00","#939393","#000000","#ffffff"};
        for (int i = 0; i < 10; i++) { //TODO: 10 is Placeholder for .length from number of Groups in JSON
            groupList.add(new GroupItem(getString(R.string.single_group) + " " + testNames[i], testColors[i]));
        }

//        if (debug) Log.i("GroupActivity-Log","5");
        GroupRecyclerViewAdapter adapter = new GroupRecyclerViewAdapter(this, groupList);
//        if (debug) Log.i("GroupActivity-Log","6");
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        if (debug) Log.i("GroupActivity-Log","7");
        recyclerView.setAdapter(adapter);
//        (if (debug) Log.i("GroupActivity-Log","8");

        // Entfernt weil redundant (siehe unten)
        //Button joinButton = findViewById(R.id.joinBTN);

        // NOTTODO: Button kann nur geklickt werden, wenn bereits Gruppe ausgewählt wurde. Ansonsten: TOAST "Bitte wähle deine Gruppe".
        // ---->Gewählt wird über die jeweiligen Buttons in den Gruppen-Dialogen

        /*joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupActivity.this, MainActivity.class);
                //Get putExtra-Data from LogIn
                startActivity(intent);
            }
        });*/
    }
}
