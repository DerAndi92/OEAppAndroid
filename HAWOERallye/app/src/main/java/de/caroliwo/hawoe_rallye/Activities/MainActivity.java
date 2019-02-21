package de.caroliwo.hawoe_rallye.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

import de.caroliwo.hawoe_rallye.Fragments.GroupFragment;
import de.caroliwo.hawoe_rallye.Fragments.ImpressumFragment;
import de.caroliwo.hawoe_rallye.Fragments.IntroductionFragment;
import de.caroliwo.hawoe_rallye.Fragments.RoomplansFragment;
import de.caroliwo.hawoe_rallye.Fragments.TasksFragment;
import de.caroliwo.hawoe_rallye.Fragments.TimesFragment;
import de.caroliwo.hawoe_rallye.Group;
import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Student;
import de.caroliwo.hawoe_rallye.Task;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private Fragment timesFragment;
    private ArrayList<Student> studentList;
    private ArrayList<Task> taskList;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Toolbar setzen
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Für Navigationelement Auswahl
        drawer=findViewById(R.id.drawer_layout);
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

        //Startseite
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new IntroductionFragment()).commit();

        //Intent (holen von Infos von LoadingActivity)
        Intent intent = getIntent();
        studentList = intent.getParcelableArrayListExtra("Students");
        taskList= intent.getParcelableArrayListExtra("Tasks");

        //Bundle (senden der Infos an Fragments)
        bundle = new Bundle();
        bundle.putParcelableArrayList("Students", studentList);
        bundle.putParcelableArrayList("Tasks", taskList);
    }

    //Reaktion bei Touch auf Zurück-Button
    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TasksFragment()).commit();
                break;
            case R.id.group:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GroupFragment()).commit();
                break;
            case R.id.credits:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ImpressumFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
