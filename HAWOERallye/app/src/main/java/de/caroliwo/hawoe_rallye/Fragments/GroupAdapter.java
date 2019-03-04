package de.caroliwo.hawoe_rallye.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
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

import de.caroliwo.hawoe_rallye.Activities.MainActivity;
import de.caroliwo.hawoe_rallye.Data.DataViewModel;
import de.caroliwo.hawoe_rallye.Data.StudentEntity;
import de.caroliwo.hawoe_rallye.DownloadJSONRetrofit;
import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Retrofit;
import de.caroliwo.hawoe_rallye.Student;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupAdapter extends ArrayAdapter {

    private List<Student> studentList;
    private final Activity context;
    private DownloadJSONRetrofit downloadJSONRetrofit;
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

        TextView name = rowView.findViewById(R.id.nameTV);
        TextView lastname = rowView.findViewById(R.id.lastnameTV);
        final TextView subject = rowView.findViewById(R.id.subjectTV);
        ImageButton changeButton = rowView.findViewById(R.id.changeBtn);
        ImageButton deleteButton = rowView.findViewById(R.id.deleteBtn);

        name.setText(studentList.get(position).getFirst_name());
        lastname.setText(studentList.get(position).getLast_name());
        subject.setText(studentList.get(position).getCourse());

        //Retrofit
        Retrofit retrofitClass = new Retrofit();
        downloadJSONRetrofit = retrofitClass.createlogInterceptor();

        //Delete
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student student = studentList.get(position);
                if (student.getManually() == 1) {
                    studentID = student.getStudentId();
                    deleteStudent(); //Daten in API löschen
                    remove(studentList.get(position)); //Daten aus aktueller Liste löschen
                    notifyDataSetChanged(); //Fragment aktualisieren
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
                int spinPos = getPosition(studentList.get(position).getCourse());
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
                       studentID = studentList.get(position).getStudentId();
                       Integer groupID = studentList.get(position).getGroupId();
                       Integer manually = studentList.get(position).getManually();

                       changeStudent(studentID, name, lastname, course);

                       //Fragment aktualisieren
                       Student student = new Student(groupID, studentID, name, lastname, course, manually );
                       remove(studentList.get(position)); //Daten aus aktueller Liste löschen
                        insert(student, position);
                        notifyDataSetChanged(); //Fragment aktualisieren
                        dialog.hide();

                        //Bei Änderungen am eigenen Account: Student in interner DB aktualisieren
                        viewModel = ViewModelProviders.of((MainActivity) context).get(DataViewModel.class);
                        StudentEntity studentEntity = viewModel.getStudent();
                        if(studentEntity.getId() == studentID) {
                            studentEntity.setCourse(course);
                            studentEntity.setFirst_name(name);
                            studentEntity.setLast_name(lastname);
                            viewModel.updateStudent(studentEntity);
                       }

                    }
                });
                dialog.show();
            }
        });

        return rowView;
    }

    private void deleteStudent () {
        Call<Void> call = downloadJSONRetrofit.deleteStudent(studentID);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i("TEST Response", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("TEST Error", t.getMessage() + "Error");
            }
        });
    }

    private void changeStudent(Integer studentID, String name, String lastname, String course) {
        Student student = new Student(null, studentID, name, lastname, course, null);

        Call<Student> call = downloadJSONRetrofit.changeStudent(studentID, student);

        call.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {

                if (!response.isSuccessful()) {  //TODO: Response ist hier immer !successful --> Fehler: Benennung Api studentId, hier: id
                    Log.i("TEST ErrorResponse: ", String.valueOf(response.code()));
                    return;
                }

                Student studResponse = response.body();
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("TEST Error", t.getMessage() + "Error");
            }
        });
    }
}
