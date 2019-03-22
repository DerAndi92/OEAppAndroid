package de.caroliwo.hawoe_rallye.Fragments;

//TODO: Kommentare
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private Dialog taskDialog;
    private boolean debug = false;
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

        //TODO: Manchmal öffnet er wenn man auf Poststelle klickt den Dialog für eine andere Aufgabe, z.B. Wettrennen usw.
        taskDialog = new Dialog(context);
        taskDialog.setContentView(R.layout.dialog_tasks);

        viewHolder.taskItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Task-Variable zuweisen
                final Task task = taskList.get(viewHolder.getAdapterPosition());
                Log.i("TasksRVAdapter-Log", "OnClick() task: " + task.toString());

                openTaskFragment(task);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder tasksViewHolder, int i) {
        tasksViewHolder.textView.setText(taskList.get(i).getName());
        int id = context.getResources().getIdentifier(taskList.get(i).getIcon(), "drawable", "de.caroliwo.hawoe_rallye");
        tasksViewHolder.imageView.setImageResource(id);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TasksViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ConstraintLayout taskItem;
        private ImageView imageView;

        public TasksViewHolder(@NonNull View itemView) {
            super(itemView);
            taskItem = (ConstraintLayout) itemView.findViewById(R.id.task_item_CL);
            textView = (TextView) itemView.findViewById(R.id.task_item_TW);
            imageView = (ImageView) itemView.findViewById(R.id.task_item_IW);
        }
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.taskList.clear();
        this.taskList.addAll(tasks);
        notifyDataSetChanged();
    }

    private void openTaskFragment(Task task) {
        TaskFragment fragment = TaskFragment.newInstance(task);
        FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.fragment_container, fragment, "TASK_FRAGMENT").commit();
    }
}
