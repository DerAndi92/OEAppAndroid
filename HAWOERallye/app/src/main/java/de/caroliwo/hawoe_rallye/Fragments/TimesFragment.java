package de.caroliwo.hawoe_rallye.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import de.caroliwo.hawoe_rallye.DownloadJSONRetrofit;
import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Retrofit;
import de.caroliwo.hawoe_rallye.Task;
import de.caroliwo.hawoe_rallye.TaskAPI;
import de.caroliwo.hawoe_rallye.Fragments.TimesAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimesFragment extends Fragment {

    TimesAdapter timesAdapter;
    ArrayList<Task> taskList;
    ListView timesLV;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_times, container, false);

        //ListView erstellen
        timesLV = rootView.findViewById(R.id.timesLV);

        //Bundle
        Bundle bundle = new Bundle();
        taskList= bundle.getParcelableArrayList("Tasks");

        //Adapter setzen
        timesAdapter = new TimesAdapter(getActivity(), taskList);
        timesLV.setAdapter(timesAdapter);

        return rootView;
    }
}
