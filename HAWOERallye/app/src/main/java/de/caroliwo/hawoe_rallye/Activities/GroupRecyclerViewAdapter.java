package de.caroliwo.hawoe_rallye.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import de.caroliwo.hawoe_rallye.GroupItem;
import de.caroliwo.hawoe_rallye.R;

public class GroupRecyclerViewAdapter extends RecyclerView.Adapter<GroupRecyclerViewAdapter.GroupViewHolder> {

    private Context context;
    private List<GroupItem> groupList;
    private Dialog groupDialog;

    public GroupRecyclerViewAdapter(Context context, List<GroupItem> groupList) {
        this.context = context;
        this.groupList = groupList;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group, viewGroup, false);
        final GroupViewHolder viewHolder = new GroupViewHolder(view);

        groupDialog = new Dialog(context);
        groupDialog.setContentView(R.layout.dialog_group);

        viewHolder.groupItemCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView groupIcon = (ImageView) groupDialog.findViewById(R.id.group_dialog_IV);
                final TextView groupName = (TextView) groupDialog.findViewById(R.id.group_dialog_TV);
                Button joinButton = (Button) groupDialog.findViewById(R.id.group_dialog_BTN);
                groupIcon.setColorFilter(Color.parseColor(groupList.get(viewHolder.getAdapterPosition()).getColor()));
                groupName.setText(groupList.get(viewHolder.getAdapterPosition()).getName());
                joinButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, MainActivity.class);
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
        groupViewHolder.imageView.setColorFilter(Color.parseColor(groupList.get(i).getColor()));
        groupViewHolder.textView.setText(groupList.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return groupList.size();
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

