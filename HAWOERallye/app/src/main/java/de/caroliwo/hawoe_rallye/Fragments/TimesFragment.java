package de.caroliwo.hawoe_rallye.Fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_times, container, false);

        // Viewmodel-Instanz holen
        viewModel = ViewModelProviders.of((MainActivity) getActivity()).get(DataViewModel.class);

        // Tasks holen (beinhalten die Zeiten)
        viewModel.fetchTasks(viewModel.getStudent().getGroupId());

        //ListView erstellen
        timesLV = rootView.findViewById(R.id.timesLV);

        // Tasks holen
        taskList = viewModel.getTaskListLiveData().getValue();

        // Falls taskList noch nicht verfügbar leere ArrayList zuweisen (wird später vom Observer geupdated)
        if (taskList == null) {
            taskList = new ArrayList<>();
        }

        //Adapter instanziieren
        timesAdapter = new TimesAdapter(getActivity(), taskList);

        // taskList-LiveData observieren
        viewModel.getTaskListLiveData().observe(this, new Observer<ArrayList<Task>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Task> tasks) {

                // Bei Änderungen Adapter updaten
                timesAdapter.setTasks(tasks);
            }
        });

        // Adapter setzen
        timesLV.setAdapter(timesAdapter);

        return rootView;
    }
}
