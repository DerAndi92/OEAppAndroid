package de.caroliwo.hawoe_rallye.Activities;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
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

        // Viewmodel-Instanz holen
        viewModel = ViewModelProviders.of((GroupActivity) context).get(DataViewModel.class);

        // Gruppen-Dialog erstellen
        groupDialog = new Dialog(context);
        groupDialog.setContentView(R.layout.dialog_group);

        // Click-Listener für Group-Item
        viewHolder.groupItemCL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // lokale Variable für geklickte Gruppe
                final Group thisGroup = groupsList.get(viewHolder.getAdapterPosition());

                // Checken ob in Gruppe noch Platz ist
                if (thisGroup.getMembers() < thisGroup.getMax_members()) {

                    // Gruppenname, -Farbe und Join-Button erstellen
                    final ImageView groupIcon = (ImageView) groupDialog.findViewById(R.id.group_dialog_IV);
                    final TextView groupName = (TextView) groupDialog.findViewById(R.id.group_dialog_TV);
                    Button joinButton = (Button) groupDialog.findViewById(R.id.group_dialog_BTN);
                    groupIcon.setColorFilter(Color.parseColor(thisGroup.getColor()));
                    groupName.setText(thisGroup.getName());

                    // Click-Listener für Join-Button
                    joinButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            // Mit Intent über Kontext-Klasse (GroupActivity) weiter zur LoadingActivity schicken
                            Intent intent = new Intent(context, LoadingActivity.class);
                            groupID = thisGroup.getGroupId();
                            student.setGroupId(groupID);
                            intent.putExtra("student", student);

                            //POST-Request
                            viewModel.sendStudent(student);

                            // Student mit vorläufiger Student-ID in Datenbank speichern, Student-ID wird nachgetragen wenn Liste aller Gruppenmitglieder vorliegt
                            viewModel.insertStudent(new StudentEntity(-1, student.getFirst_name(), student.getLast_name(), student.getCourse(), student.getGroupId()));

                            Toast.makeText(context, groupName.getText(), Toast.LENGTH_SHORT).show();
                            groupDialog.dismiss();
                            context.startActivity(intent);
                        }
                    });
                    groupDialog.show();  //Dialog anzeigen
                } else {
                    //Wen die maximale Gruppenanzahl bereits erreicht ist:
                    Toast.makeText( context, "Gruppe ist voll", Toast.LENGTH_SHORT ).show();
                }
            }
        });
        return viewHolder;

    }

//aktualisiert die Gruppenliste
    public void setGroups(ArrayList<Group> groups) {
        this.groupsList.clear();
        this.groupsList.addAll(groups);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder groupViewHolder, int i) {
        // Gruppenname und -Farbe für jeden Viewholder setzen
        groupViewHolder.imageView.setColorFilter(Color.parseColor( groupsList.get(i).getColor()));
        groupViewHolder.textView.setText(groupsList.get(i).getName());
    }

    //Gibt die Anzahl der Gruppen an
    @Override
    public int getItemCount() {
        return groupsList.size();
    }

    //TODO Kommentar: Was passiert hier?
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

