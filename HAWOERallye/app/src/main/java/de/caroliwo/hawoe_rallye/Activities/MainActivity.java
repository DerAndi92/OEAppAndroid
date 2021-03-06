package de.caroliwo.hawoe_rallye.Activities;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import de.caroliwo.hawoe_rallye.Data.DataViewModel;
import de.caroliwo.hawoe_rallye.Data.StudentEntity;
import de.caroliwo.hawoe_rallye.Fragments.GroupFragment;
import de.caroliwo.hawoe_rallye.Fragments.ImpressumFragment;
import de.caroliwo.hawoe_rallye.Fragments.IntroductionFragment;
import de.caroliwo.hawoe_rallye.Fragments.RoomplansFragment;
import de.caroliwo.hawoe_rallye.Fragments.TasksFragment;
import de.caroliwo.hawoe_rallye.Fragments.TimesFragment;
import de.caroliwo.hawoe_rallye.Group;
import de.caroliwo.hawoe_rallye.R;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Instanz des ViewModels holen
        final DataViewModel viewModel = ViewModelProviders.of(this).get(DataViewModel.class);

        // Laden der Gruppen um Gruppenname/-Farbe zu holen
        viewModel.fetchGroups();

        //Toolbar setzen
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Für Navigationelement Auswahl
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Menü einblenden
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Menübutton + Titel der App in Toolbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_icon);

        //nav_header mit Infos füllen
        View headerLayout = navigationView.getHeaderView(0);
        final TextView navName = headerLayout.findViewById(R.id.navName);
        final TextView navTeam = headerLayout.findViewById(R.id.navTeam);
        Button logOutButton = headerLayout.findViewById(R.id.logOutButton);

        // Eigenen Namen in nav_header schreiben
        final StudentEntity studentEntity = viewModel.getStudent();
        navName.setText(studentEntity.getFirst_name() + " " + studentEntity.getLast_name());

        // Eigenen Datenbankeintrag observieren & bei Änderungen automatisch nav_header updaten
        viewModel.getStudentLiveData().observe(this, new Observer<StudentEntity>() {
            @Override
            public void onChanged(@Nullable StudentEntity studentEntity) {

                // Check auf nicht-null weil der Observer sonst im Moment eines LogOuts versucht auf die bereits gelöschte studentEntity zuzugreifen
                if (studentEntity != null) {
                    navName.setText(studentEntity.getFirst_name() + " " + studentEntity.getLast_name());
                }
            }
        });

        // Gruppenname und -Farbe holen & in nav_header zuweisen
        final int groupId = studentEntity.getGroupId();
        viewModel.getGroupListLiveData().observe(this, new Observer<ArrayList<Group>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Group> groups) {
                for (Group group : groups) {
                    if (groupId == group.getGroupId()) {
                        navTeam.setText(group.getName());
                        navTeam.setTextColor(Color.parseColor(group.getColor()));
                    }
                }
            }
        });

        // Click-Listener für LogOut-Button
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Studenten-Einträge aus interner Datenbank löschen
                viewModel.deleteAllStudents();
                // API-Call um Student aus Web-Interface zu löschen
                viewModel.deleteStudent(studentEntity.getStudentId());
                // Intent zu Loading-Activity
                Context applicationContext = getApplicationContext();
                Intent intent = new Intent(applicationContext, LoadingActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                applicationContext.startActivity(intent);
            }
        });

        //Startseite
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new IntroductionFragment()).commit();
    }

    //Reaktion bei Touch auf Zurück-Button
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Weiterleiten zum richtigen Fragment, bei Klick auf MenuItem
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.manual:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new IntroductionFragment()).commit();
                break;
            case R.id.timeTable:
                TimesFragment timesFragment = new TimesFragment();
                timesFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, timesFragment).commit();
                break;
            case R.id.roomplans:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RoomplansFragment()).commit();
                break;
            case R.id.tasks:
                TasksFragment tasksFragment = new TasksFragment();
                tasksFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, tasksFragment).commit();
                break;
            case R.id.group:
                GroupFragment groupFragment = new GroupFragment();
                groupFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, groupFragment).commit();
                break;
            case R.id.credits:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ImpressumFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
