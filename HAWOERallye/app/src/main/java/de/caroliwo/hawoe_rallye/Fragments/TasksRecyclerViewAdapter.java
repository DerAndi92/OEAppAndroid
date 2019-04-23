package de.caroliwo.hawoe_rallye.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.caroliwo.hawoe_rallye.Activities.MainActivity;
import de.caroliwo.hawoe_rallye.Data.DataViewModel;
import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Task;

public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TasksRecyclerViewAdapter.TasksViewHolder> {

    private Context context;
    private List<Task> taskList;
    private DataViewModel viewModel;

    public TasksRecyclerViewAdapter(Context context, List<Task> data) {
        this.context = context;
        this.taskList = data;

        // ViewModel-Instanz holen
        viewModel = ViewModelProviders.of((MainActivity) context).get(DataViewModel.class);
    }

    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_task, viewGroup, false);
        final TasksViewHolder viewHolder = new TasksViewHolder(view);

        // Click-Listener für Aufgaben
        viewHolder.taskItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Task-Variable zuweisen
                final Task task = taskList.get(viewHolder.getAdapterPosition());

                // Fragment für Aufgabe öffnen
                openTaskFragment(task);
            }
        });
        return viewHolder;
    }

    // Wird für jede Aufgabe ausgeführt
    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder tasksViewHolder, int i) {
        // Namen der Aufgabe zu TextView zuweisen
        tasksViewHolder.textView.setText(taskList.get(i).getName());
        // Icon der Aufgabe zuweisen
        int id = context.getResources().getIdentifier(taskList.get(i).getIcon(), "drawable", "de.caroliwo.hawoe_rallye");
        tasksViewHolder.imageView.setImageResource(id);

        if (taskList.get(i).isCompleted()) {
            tasksViewHolder.imageView.setColorFilter(Color.parseColor("#00FF00"));
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    // Klasse für Aufgaben-ViewHolder
    public static class TasksViewHolder extends RecyclerView.ViewHolder {

        // Variablen für die Views einer Aufgabe
        private TextView textView;
        private ConstraintLayout taskItem;
        private ImageView imageView;

        public TasksViewHolder(@NonNull View itemView) {
            super(itemView);

            // Variablen den entsprechenden Views zuweisen
            taskItem = (ConstraintLayout) itemView.findViewById(R.id.task_item_CL);
            textView = (TextView) itemView.findViewById(R.id.task_item_TW);
            imageView = (ImageView) itemView.findViewById(R.id.task_item_IW);
        }
    }

    // Aufgaben updaten falls es neue/veränderte gibt
    public void setTasks(ArrayList<Task> tasks) {
        this.taskList.clear();
        this.taskList.addAll(tasks);
        notifyDataSetChanged();
    }

    // Fragment für angeklickte Aufgabe öffnen
    private void openTaskFragment(Task task) {
        TaskFragment fragment = TaskFragment.newInstance(task);
        FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.add(R.id.fragment_container, fragment, "TASK_FRAGMENT");
        transaction.addToBackStack(null); // Damit man mit Back-Button hierher zurück kommt
        transaction.commit();
    }
}
