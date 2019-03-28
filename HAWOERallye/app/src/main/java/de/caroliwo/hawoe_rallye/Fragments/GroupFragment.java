package de.caroliwo.hawoe_rallye.Fragments;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import java.util.ArrayList;

import de.caroliwo.hawoe_rallye.Activities.MainActivity;
import de.caroliwo.hawoe_rallye.Data.DataViewModel;
import de.caroliwo.hawoe_rallye.Group;
import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Student;

public class GroupFragment extends Fragment {

    private GroupAdapter groupAdapter;
    private ArrayList<Student> studentList;
    private ListView studentsLV;
    private Dialog dialog;
    private Student student;
    private EditText nameDialog;
    private EditText lastnameDialog;
    private Spinner spinnerDialog;
    private DataViewModel viewModel;

    public GroupFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_group, container,false);

        Button changeGroupNameBtn = rootView.findViewById(R.id.groupNameBTN);
        final EditText changeGroupNameET = rootView.findViewById(R.id.groupNameET);
        Button addStudentBtn = rootView.findViewById(R.id.addStudentBTN);
        studentsLV = rootView.findViewById(R.id.membersLV);

        // Viewmodel-Instanz aus MainActivity holen
        viewModel = ViewModelProviders.of((MainActivity) getActivity()).get(DataViewModel.class);
        final Integer groupId = viewModel.getStudent().getGroupId();
        viewModel.fetchGroup(groupId);

        // aktuellen Gruppennamen in EditText schreiben
        viewModel.getGroupListLiveData().observe(this, new Observer<ArrayList<Group>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Group> groups) {
                for (Group group: groups) {
                    if (groupId == group.getGroupId()) {
                        changeGroupNameET.setText(group.getName());
                    }
                }
            }
        });

        // Gruppenname ändern
        changeGroupNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Group group = new Group(groupId,changeGroupNameET.getText().toString());
                viewModel.changeGroupName(group);
            }
        });

        // studentList holen
        studentList = viewModel.getStudentListLiveData().getValue();

        // Falls studentList noch nicht verfügbar leere ArrayList zuweisen (wird später vom Observer geupdated)
        if (studentList == null) { studentList = new ArrayList<>(); }

        //Adapter instanziieren
        groupAdapter = new GroupAdapter(getActivity(), studentList);

       //Student zu Liste hinzufügen
        addStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Dialog für Student erstellen
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_student);
                nameDialog = (EditText) dialog.findViewById(R.id.nameET);
                lastnameDialog = dialog.findViewById(R.id.lastnameET);
                spinnerDialog = dialog.findViewById(R.id.MTMS_spinner);
                Button addButtonDialog = dialog.findViewById(R.id.changeButton);
                addButtonDialog.setText("Hinzufügen");
                Log.i("TEST Dialog ", spinnerDialog.getSelectedItem().toString());

                //Erstellen absenden
                addButtonDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String name = nameDialog.getText().toString();
                        String lastname = lastnameDialog.getText().toString();
                        String majorTemp = spinnerDialog.getSelectedItem().toString();
                        String course = majorTemp.equals("Medientechnik") ? "MT" : (majorTemp.equals("Media Systems") ? "MS" : majorTemp);

                        // Neuen Studenten erstellen
                        student = new Student(groupId, null ,name, lastname, course, 1);

                        // Neuen Studenten per POST-Request an API senden (wird im Repository automatisch bei Response eigener Gruppe hinzugefügt & per LiveData geupdated)
                        viewModel.sendStudent(student);

                        dialog.hide();
                    }
                });
                dialog.show();
            }
        });


        // studentList-LiveData observieren
        viewModel.getStudentListLiveData().observe(this, new Observer<ArrayList<Student>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Student> students) {

                // Bei Änderung Adapter updaten
                groupAdapter.setStudents(students);
                Log.i("GroupFragment", "Adapter updated by Observer: " + students.toString());
            }
        });

        // adapter setzen
        studentsLV.setAdapter(groupAdapter);

        return rootView;
    }
}
