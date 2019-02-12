package de.caroliwo.hawoe_rallye.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TasksFragmentViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> titleList = new ArrayList<>();
    private boolean debug = false;

    public TasksFragmentViewPagerAdapter(FragmentManager fm) {

        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        return fragmentList.get(i);
    }

    @Override
    public int getCount() {

        return titleList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return titleList.get(position);
    }


    public void addFragment (Fragment frag, String title) {
        if (debug) Log.i("TFViewPagerAdapter-Log","addFragment:frag: " + frag + "---- addFragment:title: " + title);
        fragmentList.add(frag);
        titleList.add(title);
    }
}
