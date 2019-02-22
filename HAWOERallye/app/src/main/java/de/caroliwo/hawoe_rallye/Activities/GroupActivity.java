package de.caroliwo.hawoe_rallye.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import de.caroliwo.hawoe_rallye.Data.DataViewModel;
import de.caroliwo.hawoe_rallye.DownloadJSONRetrofit;
import de.caroliwo.hawoe_rallye.Group;
import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Student;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private boolean debug = false;
    private ArrayList<Group> groupsList;
    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (debug) Log.i("GroupActivity-Log","1");
        super.onCreate(savedInstanceState);
        Log.i("Test", "start GroupActivity");

//        if (debug) Log.i("GroupActivity-Log","2");
        setContentView(R.layout.activity_group);
//        if (debug) Log.i("GroupActivity-Log","3");
        recyclerView = findViewById(R.id.chooseGroupRV);
//        if (debug) Log.i("GroupActivity-Log","4");

        //Daten von LoadingActivity übergeben
        Intent intentFromLogIn = getIntent();
        groupsList = intentFromLogIn.getParcelableArrayListExtra("Groups");
        student = intentFromLogIn.getParcelableExtra("StudentData");

        //RecyclerView mit Gruppen füllen
//        if (debug) Log.i("GroupActivity-Log","5");
        GroupRecyclerViewAdapter adapter = new GroupRecyclerViewAdapter(this, groupsList, student);
//        if (debug) Log.i("GroupActivity-Log","6");
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        Log.i("GroupActivity-Log test","7");
        recyclerView.setAdapter(adapter);
        Log.i("GroupActivity-Log test","8");

    }
}
