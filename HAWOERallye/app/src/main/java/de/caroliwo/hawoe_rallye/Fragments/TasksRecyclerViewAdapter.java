package de.caroliwo.hawoe_rallye.Fragments;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.caroliwo.hawoe_rallye.Activities.MainActivity;
import de.caroliwo.hawoe_rallye.Data.DataViewModel;
import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Task;
import de.caroliwo.hawoe_rallye.TaskItem;

public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TasksRecyclerViewAdapter.TasksViewHolder> {

    private Context context;
    private List<Task> taskList;
    private List<Task> taskDetailList;
    private Dialog taskDialog;
    private boolean debug = false;
    private DataViewModel viewModel;

    public TasksRecyclerViewAdapter(Context context, List<Task> data, List<Task> details) {
        this.context = context;
        this.taskList = data;
        this.taskDetailList = details;
        viewModel = ViewModelProviders.of((MainActivity) context).get(DataViewModel.class);
    }


    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, viewGroup, false);
        final TasksViewHolder viewHolder = new TasksViewHolder(view);

        //TODO: Statt Dialog evtl lieber ein neues Fragment öffnen. Manche Aufgaben brauchen viel Platz; TOBI: Ich wäre eher für ScrollView im Dialog
        taskDialog = new Dialog(context);
        taskDialog.setContentView(R.layout.dialog_tasks);

        viewHolder.taskItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Task-Variable zuweisen
                Task task = taskDetailList.get(viewHolder.getAdapterPosition());
                Log.i("TasksRVAdapter-Log", "OnClick() task: " + task.toString());
                //Für Get-Request: JSON bei jeder Aufgabe anders --> JSON manuell Parsen ähnlich wie in GroupFragment POST
                //Unterschied: Objekt muss anhand des JSON-Aufbaus erstellt werden ohne, dass es bereits vorher als Klasse angelegt wurde
                //Stichwort: Dynamic JSON
                //TODO: POST-Request Lösung zur einer Aufgabe abschicken

                if (debug) Toast.makeText(context, "Test Click"+String.valueOf(viewHolder.getAdapterPosition()),Toast.LENGTH_SHORT).show();
                TextView taskTitle = (TextView) taskDialog.findViewById(R.id.task_dialog_TV);
                TextView taskDestination = taskDialog.findViewById(R.id.task_dialog_TV2);
                ImageView taskIcon = (ImageView) taskDialog.findViewById(R.id.student_dialog_IV);
                List<Task.Field> fieldList = task.getFieldList();
                if (task.isCompleted()) {
                    taskIcon.setColorFilter(Color.parseColor("#00FF00"));
                }
                taskTitle.setText(task.getName());
                taskDestination.setText(task.getDestination());
                for (Task.Field field: fieldList) {
                    // TODO: fieldList in Elemente im View umwandeln
                }

                taskDialog.show();
                if (debug) Log.i("TasksRVAdapter-Log","taskDialog.isShowing(): " + taskDialog.isShowing());
                if (debug) Log.i("TasksRVAdapter-Log","taskDialog.getContext(): " + taskDialog.getContext());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder tasksViewHolder, int i) {
        tasksViewHolder.textView.setText(taskList.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TasksViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ConstraintLayout taskItem;

        public TasksViewHolder(@NonNull View itemView) {
            super(itemView);
            taskItem = (ConstraintLayout) itemView.findViewById(R.id.task_item_CL);
            textView = (TextView) itemView.findViewById(R.id.task_item_TW);
        }
    }

    public void setTasks(ArrayList<Task> tasks, ArrayList<Task> taskDetails) {
        this.taskList.clear();
        this.taskList.addAll(tasks);
        this.taskDetailList.clear();
        this.taskDetailList.addAll(taskDetails);
        notifyDataSetChanged();
    }
}
