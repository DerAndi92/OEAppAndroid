package de.caroliwo.hawoe_rallye.Fragments;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.caroliwo.hawoe_rallye.Activities.MainActivity;
import de.caroliwo.hawoe_rallye.Data.DataViewModel;
import de.caroliwo.hawoe_rallye.Field;
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

        //TODO: Für alle Dialoge einen Exit-Button, um den Dialog zu schließen
        //TODO: Zeiten auch mit auf Dialog 

        //TODO: Manchmal öffnet er wenn man auf Poststelle klickt den Dialog für eine andere Aufgabe, z.B. Wettrennen usw. --> Bug fixen
        taskDialog = new Dialog(context);
        taskDialog.setContentView(R.layout.dialog_tasks);

        viewHolder.taskItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Task-Variable zuweisen
                final Task task = taskDetailList.get(viewHolder.getAdapterPosition());
                Log.i("TasksRVAdapter-Log", "OnClick() task: " + task.toString());


                if (debug) Toast.makeText(context, "Test Click"+String.valueOf(viewHolder.getAdapterPosition()),Toast.LENGTH_SHORT).show();
                TextView taskTitle = (TextView) taskDialog.findViewById(R.id.task_dialog_TV);
                TextView taskDestination = taskDialog.findViewById(R.id.task_dialog_TV2);
                ImageView taskIcon = (ImageView) taskDialog.findViewById(R.id.student_dialog_IV);
                List<Field> fieldList = task.getFieldList();
                if (task.isCompleted()) {
                    taskIcon.setColorFilter(Color.parseColor("#00FF00"));
                }
                taskTitle.setText(task.getName());
                taskDestination.setText(task.getDestination());

                //Dynamisches Erstellen der Dialog-Inhalte (Aufgaben)
                LinearLayout fieldsContainer = (LinearLayout) taskDialog.findViewById(R.id.FieldsContainer);
                //Für Abstand zwischen den Views im LinearLayout
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(50,0,0,50);
                //TODO: Breite der Elemente Button, EditText etc. an Dialog-Breite anpassen mit je 8dp Abstand zum Rand
                fieldsContainer.removeAllViews();
                for (Field field: fieldList) {
                        String type = field.getType();
                        switch (type) {
                            case "button":
                                Button button = new Button(context);
                                button.setText(field.getValue());
                                button.setTextColor(context.getResources().getColor( R.color.colorText));
                                button.setBackground(context.getResources().getDrawable(R.drawable.buttonshape));
                                fieldsContainer.addView(button, layoutParams);
                                Log.i("TasksRVAdapter-Switch", "Button created");
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        //TODO: Daten für POST vervollständigen
                                       /* int group = ; //groupID
                                        int task = ; //taskID
                                        String password = ; //optional
                                        ArrayList<> inputs = ;

                                        Object task1 = new Object(group, task, password, inputsArraylist);

                                        viewModel.sendAnswer(task1); */
                                    }
                                });
                                break;
                            case "text":
                                TextView textV = new TextView(context);
                                textV.setText(field.getValue());
                                textV.setTextColor(context.getResources().getColor( R.color.colorText));
                                fieldsContainer.addView(textV,layoutParams);
                                Log.i("TasksRVAdapter-Switch", "TV created");
                                break;
                            case "inputField":
                                EditText editText = new EditText(context);
                                if(field.getValue()!=null){
                                    editText.setText(field.getValue());
                                }
                                editText.setTextColor(context.getResources().getColor( R.color.colorText));
                                fieldsContainer.addView(editText,layoutParams);
                                Log.i("TasksRVAdapter-Switch", "ET created");
                                break;
                            case "inputText":
                                EditText editTextBig = new EditText(context);
                                if(field.getValue()!=null) {
                                    editTextBig.setText(field.getValue());
                                }
                                editTextBig.setHeight(50);
                                editTextBig.setTextColor(context.getResources().getColor( R.color.colorText));
                                fieldsContainer.addView(editTextBig,layoutParams);
                                Log.i("TasksRVAdapter-Switch", "ET big created");
                                break;
                            case "inputInvisible":
                                EditText editTextInvisible = new EditText(context);
                                if(field.getValue()!=null) {
                                    editTextInvisible.setText(field.getValue());
                                }
                                editTextInvisible.setVisibility(View.INVISIBLE);
                                fieldsContainer.addView(editTextInvisible);
                                Log.i("TasksRVAdapter-Switch", "ET invisible created");
                                break;
                            default:
                                Log.i("TasksRVAdapter-Switch", "Field type is not defined");
                        }
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
