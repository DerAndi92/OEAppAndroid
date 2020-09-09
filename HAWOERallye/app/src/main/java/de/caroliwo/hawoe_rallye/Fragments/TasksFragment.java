package de.caroliwo.hawoe_rallye.Fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import de.caroliwo.hawoe_rallye.Activities.MainActivity;
import de.caroliwo.hawoe_rallye.Data.DataViewModel;
import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Task;

public class TasksFragment extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private ArrayList<Task> taskList; // <------Liste mit allen Aufgaben + Aufgabendetails der Gruppe
    private boolean debug = false;
    private DataViewModel viewModel;
    private TasksRecyclerViewAdapter recyclerViewAdapter;

    public TasksFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Viewmodel-Instanz holen
        viewModel = ViewModelProviders.of((MainActivity) getActivity()).get(DataViewModel.class);
        // Tasks holen
        viewModel.fetchTasks(viewModel.getStudent().getGroupId());

        taskList = viewModel.getTaskListLiveData().getValue();

        // Falls noch nicht verfügbar leere ArrayList zuweisen (wird später vom Observer geupdated)
        if (taskList == null) taskList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Fragment-Layout inflaten
        v = inflater.inflate(R.layout.fragment_tasks, container, false);

        // RecyclerView zuweisen
        recyclerView = (RecyclerView) v.findViewById(R.id.tasks_recyclerview);

        // neuen Adapter erstellen und zuweisen
        recyclerViewAdapter = new TasksRecyclerViewAdapter((MainActivity) getActivity(), taskList);

        // taskList-LiveData observieren, diese wird im Repository nach dem Laden der taskList automatisch vervollständigt
        viewModel.getTaskListLiveData().observe(this, new Observer<ArrayList<Task>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Task> tasks) {

                // Bei Änderung die Liste neu zuweisen
                taskList = viewModel.getTaskListLiveData().getValue();

                // Adapter updaten
                recyclerViewAdapter.setTasks(taskList);
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(recyclerViewAdapter);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerViewAdapter.setTasks(taskList);
    }

    public List<Task> getTaskList() {
        return taskList;
    }
}
