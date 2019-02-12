package de.caroliwo.hawoe_rallye.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.TaskItem;

public class TasksFragmentNeubau extends Fragment {

    View v;
    private RecyclerView recyclerView;
    private List<TaskItem> taskItemList;

    public TasksFragmentNeubau() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_tasks_altbau, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.tasks_altbau_recyclerview);
        TasksRecyclerViewAdapter recyclerViewAdapter = new TasksRecyclerViewAdapter(getContext(), taskItemList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(recyclerViewAdapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        taskItemList = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            taskItemList.add(new TaskItem(getString(R.string.single_task) + " " + (i+1)));
        }
    }

    public List<TaskItem> getTaskItemList() {
        return taskItemList;
    }
}
