package de.caroliwo.hawoe_rallye;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class TimesFragment extends Fragment {

    TimesAdapter timesAdapter;
    ArrayList<Task> taskList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_times, container, false);

        //ListView mit Zeiten füllen
        ListView timesLV = rootView.findViewById(R.id.timesLV);

        //Daten von Activity Parcelable holen
        Bundle bundle = getArguments();
        if(bundle!=null) {
            taskList = bundle.getParcelableArrayList("Task List");
        }

        timesAdapter = new TimesAdapter(getActivity(), taskList);
        timesLV.setAdapter(timesAdapter);

        return rootView;
    }
}
