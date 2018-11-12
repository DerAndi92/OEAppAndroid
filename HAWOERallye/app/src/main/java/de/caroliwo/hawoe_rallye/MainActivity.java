package de.caroliwo.hawoe_rallye;

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
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private Fragment timesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //TaskList von Intent von GroupActivity Parcelable laden
        Intent intent = getIntent();
        ArrayList<Task> taskList = intent.getParcelableArrayListExtra("Task List");

        //Senden von Daten an TimesFragment per Bundle mit Parcelable
        timesFragment = new TimesFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Task List", taskList);
        timesFragment.setArguments(bundle);

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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, timesFragment).commit();
                break;
            case R.id.roomplans:
                break;
            case R.id.tasks:
                break;
            case R.id.group:
                break;
            case R.id.credits:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ImpressumFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
