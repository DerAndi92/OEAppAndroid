package de.caroliwo.hawoe_rallye.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import de.caroliwo.hawoe_rallye.Data.DataViewModel;
import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Task;

public class TasksFragment extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private ArrayList<Task> taskList;
    private boolean debug = false;
    private DataViewModel viewModel;

    public TasksFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (debug) Log.i("TasksFragment-Log","3");
        v = inflater.inflate(R.layout.fragment_tasks, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.tasks_recyclerview);
        final TasksRecyclerViewAdapter recyclerViewAdapter = new TasksRecyclerViewAdapter(getContext(), taskList);
        if (debug) Log.i("TasksFragment-Log","taskList: " + taskList);

        // taskList-LiveData observieren
        viewModel.getTaskListLiveData().observe(this, new Observer<ArrayList<Task>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Task> tasks) {

                // Bei Änderung Adapter updaten
                recyclerViewAdapter.setTasks(tasks);
                if (debug) Log.i("TasksFragment-Log","Tasks updated by Observer: " + tasks);
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(recyclerViewAdapter);
        if (debug) Log.i("TasksFragment-Log","4");
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (debug) Log.i("TasksFragment-Log","1");
        super.onCreate(savedInstanceState);

        // Viewmodel-Instanz holen
        viewModel = ViewModelProviders.of(this).get(DataViewModel.class);
        // Tasks holen
        viewModel.fetchTasks(viewModel.getStudent().getGroupId());

        //TaskList holen
//        Bundle bundle = getArguments();
//        taskList = bundle.getParcelableArrayList("Tasks");

        taskList = viewModel.getTaskListLiveData().getValue();
        // Falls taskList noch nicht verfügbar leere ArrayList zuweisen (wird später vom Observer geupdated)
        if (taskList == null) { taskList = new ArrayList<>(); }

        if (debug) Log.i("TasksFragment-Log","2");
    }

    public List<Task> getTaskList() {
        if (debug) Log.i("TasksFragment-Log","return taskItemList: " + taskList);
        return taskList;
    }
}
