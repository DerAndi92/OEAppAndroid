package de.caroliwo.hawoe_rallye;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TasksRecyclerViewAdapter.TasksViewHolder> {

    Context context;
    List<TaskItem> data;
    Dialog taskDialog;
    private boolean debug = true;

    public TasksRecyclerViewAdapter(Context context, List<TaskItem> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, viewGroup, false);
        final TasksViewHolder viewHolder = new TasksViewHolder(view);

        taskDialog = new Dialog(context);
        taskDialog.setContentView(R.layout.dialog_tasks);

        viewHolder.taskItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (debug) Toast.makeText(context, "Test Click"+String.valueOf(viewHolder.getAdapterPosition()),Toast.LENGTH_SHORT).show();
                TextView taskTitle = (TextView) taskDialog.findViewById(R.id.task_dialog_TV);
                ImageView taskIcon = (ImageView) taskDialog.findViewById(R.id.task_dialog_IV);
                taskTitle.setText(data.get(viewHolder.getAdapterPosition()).getName());
                taskDialog.show();
                if (debug) Log.i("TasksRVAdapter-Log","taskDialog.isShowing(): " + taskDialog.isShowing());
                if (debug) Log.i("TasksRVAdapter-Log","taskDialog.getContext(): " + taskDialog.getContext());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder tasksViewHolder, int i) {
        tasksViewHolder.textView.setText(data.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
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
}
