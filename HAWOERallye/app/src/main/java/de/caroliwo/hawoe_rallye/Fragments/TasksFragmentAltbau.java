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
import de.caroliwo.hawoe_rallye.TaskItem;

public class TasksFragmentAltbau extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private List<TaskItem> taskItemList;
    private boolean debug = false;

    public TasksFragmentAltbau() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (debug) Log.i("TasksFragmentAltbau-Log","3");
        v = inflater.inflate(R.layout.fragment_tasks_altbau, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.tasks_altbau_recyclerview);
        TasksRecyclerViewAdapter recyclerViewAdapter = new TasksRecyclerViewAdapter(getContext(), taskItemList);
        if (debug) Log.i("TasksFragmentAltbau-Log","taskItemList: " + taskItemList);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(recyclerViewAdapter);
        if (debug) Log.i("TasksFragmentAltbau-Log","4");
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (debug) Log.i("TasksFragmentAltbau-Log","1");
        super.onCreate(savedInstanceState);

        taskItemList = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            taskItemList.add(new TaskItem(getString(R.string.single_task) + " " + (i+1)));
        }
        if (debug) Log.i("TasksFragmentAltbau-Log","2");
        if (debug) Log.i("TasksFragmentAltbau-Log","taskItemList: " + taskItemList);
    }

    public List<TaskItem> getTaskItemList() {
        if (debug) Log.i("TasksFragmentAltbau-Log","return taskItemList: " + taskItemList);
        return taskItemList;
    }
}
