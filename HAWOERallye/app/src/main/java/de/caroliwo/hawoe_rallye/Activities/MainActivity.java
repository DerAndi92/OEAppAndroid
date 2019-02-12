package de.caroliwo.hawoe_rallye.activities;

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

import de.caroliwo.hawoe_rallye.fragments.GroupFragment;
import de.caroliwo.hawoe_rallye.fragments.ImpressumFragment;
import de.caroliwo.hawoe_rallye.fragments.IntroductionFragment;
import de.caroliwo.hawoe_rallye.fragments.RoomplansFragment;
import de.caroliwo.hawoe_rallye.fragments.TasksFragment;
import de.caroliwo.hawoe_rallye.fragments.TimesFragment;
import de.caroliwo.hawoe_rallye.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private Fragment timesFragment;

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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TimesFragment()).commit();
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
