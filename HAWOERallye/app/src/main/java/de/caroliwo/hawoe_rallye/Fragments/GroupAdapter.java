package de.caroliwo.hawoe_rallye.Fragments;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

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
    Integer studentID;


    //Konstruktor
    public GroupAdapter(Activity context, ArrayList<Student> studentList) {
        super(context, R.layout.group_fragment_listview_layout, studentList);
        this.context=context;
        this.studentList=studentList;
    }

    public View getView(final int position, View view, ViewGroup parent){
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.group_fragment_listview_layout, null, true);

        studentID = studentList.get(position).getStudentId();

        TextView name = rowView.findViewById(R.id.nameTV);
        TextView lastname = rowView.findViewById(R.id.lastnameTV);
        TextView subject = rowView.findViewById(R.id.subjectTV);
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
                deleteStudent();
                //TODO: sofortige Aktualisierung der studentList + Neuladen des Fragments
            }
        });

        //Change
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Dialog/RecyclerView öffnen mit Name, Nachname, Studiengang in TextViews mit Bearbeitungsmöglichkeit + Ändern Button
                //Wenn Änderungen vorhanden: changeStudent() aufrufen und Daten aktualisieren
                changeStudent(position);
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

    private void changeStudent(Integer position) {
        Call<Student> call = downloadJSONRetrofit.changeStudent(studentID, studentList.get(position));

        call.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {

                if (!response.isSuccessful()) {
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
