package de.caroliwo.hawoe_rallye.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.caroliwo.hawoe_rallye.Activities.LoadingActivity;
import de.caroliwo.hawoe_rallye.Activities.MainActivity;
import de.caroliwo.hawoe_rallye.Data.DataViewModel;
import de.caroliwo.hawoe_rallye.Data.StudentEntity;
import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Student;


public class GroupAdapter extends ArrayAdapter {

    private List<Student> studentList;
    private final Activity context;
    private Integer studentID;
    private EditText nameDialog;
    private EditText lastnameDialog;
    private Spinner spinnerDialog;
    private Dialog dialog;
    private DataViewModel viewModel;

    //Konstruktor
    public GroupAdapter(Activity context, ArrayList<Student> studentList) {
        super(context, R.layout.group_fragment_listview_layout, studentList);
        this.context = context;
        this.studentList = studentList;
    }

    public View getView(final int position, final View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.group_fragment_listview_layout, null, true);

        // ViewModel-Instanz holen
        viewModel = ViewModelProviders.of((MainActivity) context).get(DataViewModel.class);
        // Eigenen Student aus Datenbank holen
        final StudentEntity studentEntity = viewModel.getStudent();

        // Views zuweisen
        TextView name = rowView.findViewById(R.id.nameTV);
        TextView lastname = rowView.findViewById(R.id.lastnameTV);
        final TextView subject = rowView.findViewById(R.id.subjectTV);
        ImageButton changeButton = rowView.findViewById(R.id.changeBtn);
        ImageButton deleteButton = rowView.findViewById(R.id.deleteBtn);

        name.setText(studentList.get(position).getFirst_name());
        lastname.setText(studentList.get(position).getLast_name());
        subject.setText(studentList.get(position).getCourse());

        //Delete
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // geklickten Studenten holen
                Student student = studentList.get(position);

                // Checken ob Student manuell hinzugefügt wurde
                if (student.getManually() == 1) {

                    // Student-ID holen
                    studentID = student.getStudentId();

                    // Student im Web-Interface löschen
                    Log.i("GroupAdapter", "student: " + student.toString());
                    viewModel.deleteStudent(studentID);
                    Log.i("GroupAdapter", "student: " + student.toString());

                    Log.i("GroupAdapter", "studentID: " + studentID);
                    Log.i("GroupAdapter", "studentEntity.getStudentId(): " + studentEntity.getStudentId());

                    // Checken ob man sich selbst gelöscht hat
                    if (studentID == studentEntity.getStudentId()) {

                        // Studenten-Datensatz aus Datenbank löschen
                        viewModel.deleteAllStudents();

                        // Mit Intent zur LoadingActivity
                        Intent intent = new Intent(context, LoadingActivity.class);
                        context.startActivity(intent);
                    }

                    // Student aus LiveData-Liste im Repository löschen
                    Log.i("GroupAdapter", "student: " + student.toString());
                    viewModel.removeStudent(student);
                } else {
                    Toast.makeText(context, "Nur manuell hinzugefügte Studenten können gelöscht werden", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Change
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Dialog für Änderungen
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_student);
                nameDialog = (EditText) dialog.findViewById(R.id.nameET);
                lastnameDialog = dialog.findViewById(R.id.lastnameET);
                spinnerDialog = dialog.findViewById(R.id.MTMS_spinner);
                Button changeButton = dialog.findViewById(R.id.changeButton);

                //Aktuelle Daten in Dialog setzen
                nameDialog.setText(studentList.get(position).getFirst_name());
                lastnameDialog.setText(studentList.get(position).getLast_name());
                String course = studentList.get(position).getCourse();
                int spinPos = course.equals("MT") ? 1 : 2;
                Log.i("TEST", "GroupAdapter spinner1 " + studentList.get(position).getCourse());
                Log.i("TEST", "GroupAdapter spinner2 " + spinPos);
                spinnerDialog.setSelection(spinPos);

                //Wenn Änderungen vorhanden: changeStudent() aufrufen und Daten aktualisieren
                changeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       String name = nameDialog.getText().toString();
                       String lastname = lastnameDialog.getText().toString();
                       String course = spinnerDialog.getSelectedItem().toString();
                       course = course.equals("Medientechnik") ? "MT" : (course.equals("Media Systems") ? "MS" : course);
                       studentID = studentList.get(position).getStudentId();
                       Integer groupID = studentList.get(position).getGroupId();
                       Integer manually = studentList.get(position).getManually();

                       // Checken ob alle Felder ausgefüllt sind
                       if(name.length() > 0 && lastname.length() > 0 && !course.equals("Studiengang wählen")) {

                           Student student = new Student(groupID, studentID, name, lastname, course, manually);
                           // Änderung an Web-Interface schicken (wird im Repository automatisch bei Response eigener Gruppe hinzugefügt & per LiveData geupdated)
                           viewModel.changeStudent(student);

                           //Bei Änderungen am eigenen Account: Student in interner DB aktualisieren
                           if (studentEntity.getStudentId() == studentID) {
                               studentEntity.setCourse(course);
                               studentEntity.setFirst_name(name);
                               studentEntity.setLast_name(lastname);
                               viewModel.updateStudent(studentEntity);
                           }
                           dialog.hide();


                       } else {
                           Toast.makeText((MainActivity) context, "Fülle bitte alle Felder aus.", Toast.LENGTH_SHORT).show();
                       }

                    }
                });
                dialog.show();
            }
        });

        return rowView;
    }

    public void setStudents(ArrayList<Student> students) {
        this.studentList.clear();
        this.studentList.addAll(students);
        Log.i("GroupAdapter", "setStudents() input: " + students.toString());
        Log.i("GroupAdapter", "setStudents() this.studentList: " + this.studentList.toString());
        notifyDataSetChanged();
    }
}
