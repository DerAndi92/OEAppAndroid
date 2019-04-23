package de.caroliwo.hawoe_rallye.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import de.caroliwo.hawoe_rallye.Data.DataViewModel;
import de.caroliwo.hawoe_rallye.Group;
import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Student;


public class GroupActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private boolean debug = false;
    private ArrayList<Group> groupsList;
    private Student student;
    private DataViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewModel-Instanz holen
        viewModel = ViewModelProviders.of(this).get(DataViewModel.class);

        // Gruppen laden
        viewModel.fetchGroups();

        //Layouts und Elemente zuweisen
        setContentView(R.layout.activity_group);
        recyclerView = findViewById(R.id.chooseGroupRV);

        //Daten von LoadingActivity holen
        Intent intentFromLogIn = getIntent();
        student = intentFromLogIn.getParcelableExtra("StudentData");

        // Gruppen holen
        groupsList = viewModel.getGroupListLiveData().getValue();
        // Falls groupList noch nicht verfügbar leere ArrayList zuweisen (wird später vom Observer geupdated)
        if (groupsList == null) {
            groupsList = new ArrayList<>();
        }

        //RecyclerView mit Gruppen füllen
        final GroupRecyclerViewAdapter adapter = new GroupRecyclerViewAdapter(this, groupsList, student);

        // groupsList-LiveData observieren
        viewModel.getGroupListLiveData().observe(this, new Observer<ArrayList<Group>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Group> groups) {
                // Bei Änderung Adapter updaten
                adapter.setGroups(groups);
            }
        });

        //LayoutManager und Adapter setzen
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

    }
}
