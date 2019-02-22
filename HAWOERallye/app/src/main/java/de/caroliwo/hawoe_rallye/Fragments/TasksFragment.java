package de.caroliwo.hawoe_rallye.Fragments;

import android.annotation.TargetApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Task;

public class TasksFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    ArrayList<Task> taskList;
    private TasksFragmentViewPagerAdapter adapter;
    private TasksFragmentAltbau fragmentAltbau;
    private TasksFragmentNeubau fragmentNeubau;
    private boolean debug = false;


    //TODO: Kommentare
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (debug) Log.i("TasksFragment-Log","1");

        View rootView = inflater.inflate(R.layout.fragment_tasks, container,false);
        if (debug) Log.i("TasksFragment-Log","2");

        //TODO: Daten aus Bundle für Seitenaufbau verwenden
        //Bundle
        Bundle bundle = getArguments();
        taskList= bundle.getParcelableArrayList("Tasks");

        tabLayout = (TabLayout) rootView.findViewById(R.id.tasks_tablayout);
        addTablayoutShadow(tabLayout);
        if (debug) Log.i("TasksFragment-Log","3");

        viewPager = (ViewPager) rootView.findViewById(R.id.tasks_viewpager);
        if (debug) Log.i("TasksFragment-Log","4");
        //TODO: keine Aufteilung in ALtbau und Neubau. Einfach alle Aufgaben auf eine Seite
        adapter = new TasksFragmentViewPagerAdapter(getChildFragmentManager());
        if (debug) Log.i("TasksFragment-Log","5");
        if (debug) Log.i("TasksFragment-Log","adapter: " + adapter);
        if (debug) Log.i("TasksFragment-Log","getFragmentManager(): " + getFragmentManager());

        fragmentAltbau = new TasksFragmentAltbau();
        if (debug) Log.i("TasksFragment-Log","fragmentAltbau.getTaskItemList(): " + fragmentAltbau.getTaskItemList());

        fragmentNeubau = new TasksFragmentNeubau();
        if (debug) Log.i("TasksFragment-Log","fragmentNeubau.getTaskItemList(): " + fragmentNeubau.getTaskItemList());

        adapter.addFragment(fragmentAltbau,getString(R.string.building_old));
        adapter.addFragment(fragmentNeubau,getString(R.string.buidling_new));
        if (debug) Log.i("TasksFragment-Log","6");
        if (debug) Log.i("TasksFragment-Log","viewPager: " + viewPager);

        viewPager.setAdapter(adapter);
        if (debug) Log.i("TasksFragment-Log","7");

        tabLayout.setupWithViewPager(viewPager);
        if (debug) Log.i("TasksFragment-Log","8");

        //TODO: Retrofit laden der einzelnen Aufgabe nach Anklicken + Aufbau der Seite --> keine RecyclerView sondern neues Fragment aus Daten generieren

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Schatten von Actionbar entfernen
        removeActionbarShadow();
    }

    @Override
    public void onPause() {
        super.onPause();
        addActionbarShadow();
    }

    @TargetApi(21)
    private void removeActionbarShadow() {
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null){
            ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (debug) Log.i("TasksFragment-Log", "getActivity(): " + getActivity());
            if (debug) Log.i("TasksFragment-Log", "((AppCompatActivity)getActivity()).getSupportActionBar(): " + actionbar);

            actionbar.setElevation(0);
        }
    }

    @TargetApi(21)
    private void addActionbarShadow() {
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null){
            ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (debug) Log.i("TasksFragment-Log", "getActivity(): " + getActivity());
            if (debug) Log.i("TasksFragment-Log", "((AppCompatActivity)getActivity()).getSupportActionBar(): " + actionbar);

            actionbar.setElevation(4);
        }
    }
    @TargetApi(21)
    private void addTablayoutShadow(TabLayout tablayout) {
        tablayout.setElevation(4);
    }


}
