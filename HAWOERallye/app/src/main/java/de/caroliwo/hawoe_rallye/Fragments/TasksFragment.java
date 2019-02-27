package de.caroliwo.hawoe_rallye.Fragments;

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

import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Task;

public class TasksFragment extends Fragment {

    View v;
    private RecyclerView recyclerView;
    ArrayList<Task> taskList;
    private boolean debug = false;

    public TasksFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (debug) Log.i("TasksFragment-Log","3");
        v = inflater.inflate(R.layout.fragment_tasks, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.tasks_recyclerview);
        TasksRecyclerViewAdapter recyclerViewAdapter = new TasksRecyclerViewAdapter(getContext(), taskList);
        if (debug) Log.i("TasksFragment-Log","taskList: " + taskList);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(recyclerViewAdapter);
        if (debug) Log.i("TasksFragment-Log","4");
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (debug) Log.i("TasksFragment-Log","1");
        super.onCreate(savedInstanceState);
        //TODO: Daten aus Bundle für Seitenaufbau verwenden
        //Bundle
        Bundle bundle = getArguments();
        taskList = bundle.getParcelableArrayList("Tasks");
        if (debug) Log.i("TasksFragment-Log","2");
    }

    public List<Task> getTaskList() {
        if (debug) Log.i("TasksFragment-Log","return taskItemList: " + taskList);
        return taskList;
    }
}
