package de.caroliwo.hawoe_rallye;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class Introduction extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        //Menü + Titel der App in Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_icon);

    }

    //actionListener für "Hamburger" im main_menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //hier Intent mit Verweis aufs Menü einfügen
        //ODER
        //navigation drawer
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
        return true;
    }
}
