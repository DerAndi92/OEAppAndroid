package de.caroliwo.hawoe_rallye.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import de.caroliwo.hawoe_rallye.Activities.MainActivity;
import de.caroliwo.hawoe_rallye.Data.DataViewModel;
import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Task;

public class TimesFragment extends Fragment {

    private TimesAdapter timesAdapter;
    private ArrayList<Task> taskList;
    private ListView timesLV;
    private DataViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_times, container, false);

        Log.i("TimesFragment", "1" );

        // Viewmodel-Instanz holen
        viewModel = ViewModelProviders.of((MainActivity) getActivity()).get(DataViewModel.class);

        // Tasks holen (beinhalten die Zeiten)
        viewModel.fetchTasks(viewModel.getStudent().getGroupId());

        //ListView erstellen
        timesLV = rootView.findViewById(R.id.timesLV);

        // Tasks holen
        taskList = viewModel.getTaskListLiveData().getValue();
        Log.i("TimesFragment", "taskList: " + taskList);

        // Falls taskList noch nicht verfügbar leere ArrayList zuweisen (wird später vom Observer geupdated)
        if (taskList == null) { taskList = new ArrayList<>(); }

        //Adapter instanziieren
        timesAdapter = new TimesAdapter(getActivity(), taskList);

        // taskList-LiveData observieren
        viewModel.getTaskListLiveData().observe(this, new Observer<ArrayList<Task>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Task> tasks) {

                // Bei Änderungen Adapter updaten
                timesAdapter.setTasks(tasks);
                Log.i("TimesFragment", "Tasks updated by Observer: " + tasks);
            }
        });

        // Adapter setzen
        timesLV.setAdapter(timesAdapter);

        return rootView;
    }
}
