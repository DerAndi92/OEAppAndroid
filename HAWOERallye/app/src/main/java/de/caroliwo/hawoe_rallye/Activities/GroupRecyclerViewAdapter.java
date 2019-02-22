package de.caroliwo.hawoe_rallye.Activities;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.caroliwo.hawoe_rallye.Data.DataViewModel;
import de.caroliwo.hawoe_rallye.Data.StudentEntity;
import de.caroliwo.hawoe_rallye.Group;
import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Student;

public class GroupRecyclerViewAdapter extends RecyclerView.Adapter<GroupRecyclerViewAdapter.GroupViewHolder> {

    private Context context;
    private ArrayList<Group> groupsList;
    private Student student;
    private Dialog groupDialog;
    private int groupID;
    private DataViewModel viewModel;

    public GroupRecyclerViewAdapter(Context context, ArrayList<Group> groupsList, Student student) {
        this.context = context;
        this.groupsList = groupsList;
        this.student = student;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group, viewGroup, false);
        final GroupViewHolder viewHolder = new GroupViewHolder(view);

        // Gruppen-Dialog erstellen
        groupDialog = new Dialog(context);
        groupDialog.setContentView(R.layout.dialog_group);

        // Click-Listener für Group-Item
        viewHolder.groupItemCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gruppenname, -Farbe und Join-Button erstellen
                final ImageView groupIcon = (ImageView) groupDialog.findViewById(R.id.group_dialog_IV);
                final TextView groupName = (TextView) groupDialog.findViewById(R.id.group_dialog_TV);
                Button joinButton = (Button) groupDialog.findViewById(R.id.group_dialog_BTN);

                groupIcon.setColorFilter(Color.parseColor("#" + groupsList.get(viewHolder.getAdapterPosition()).getColor()));
                groupName.setText(groupsList.get(viewHolder.getAdapterPosition()).getName());

                // Click-Listener für Join-Button
                joinButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Mit Intent über Kontext-Klasse (GroupActivity) weiter zur LoadingActivity schicken
                        Intent intent = new Intent(context, LoadingActivity.class);
                        groupID = groupsList.get(viewHolder.getAdapterPosition()).getGroupId();

                        viewModel = ViewModelProviders.of((GroupActivity)context).get(DataViewModel.class);
                        viewModel.insertStudent(new StudentEntity(student.getFirst_name(), student.getLast_name(), student.getCourse(), groupID));

                        Toast.makeText(context, groupName.getText(), Toast.LENGTH_SHORT).show();
                        context.startActivity(intent);
                    }
                });
                groupDialog.show();

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder groupViewHolder, int i) {
        // Gruppenname und -Farbe für jeden Viewholder setzen
        groupViewHolder.imageView.setColorFilter(Color.parseColor("#" + groupsList.get(i).getColor()));
        groupViewHolder.textView.setText(groupsList.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout groupItemCL;
        private ImageView imageView;
        private TextView textView;

        public GroupViewHolder(@NonNull View itemView){
            super(itemView);
            groupItemCL = (ConstraintLayout) itemView.findViewById(R.id.group_item_CL);
            imageView = (ImageView) itemView.findViewById(R.id.group_item_IV);
            textView = itemView.findViewById(R.id.group_item_TV);
        }
    }
}

