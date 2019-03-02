package de.caroliwo.hawoe_rallye.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import de.caroliwo.hawoe_rallye.Activities.GroupActivity;
import de.caroliwo.hawoe_rallye.Activities.MainActivity;
import de.caroliwo.hawoe_rallye.Data.DataViewModel;
import de.caroliwo.hawoe_rallye.DownloadJSONRetrofit;
import de.caroliwo.hawoe_rallye.Group;
import de.caroliwo.hawoe_rallye.GroupAPI;
import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Retrofit;
import de.caroliwo.hawoe_rallye.Student;
import de.caroliwo.hawoe_rallye.Fragments.GroupAdapter;
import de.caroliwo.hawoe_rallye.StudentAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupFragment extends Fragment {

    private GroupAdapter groupAdapter;
    private ArrayList<Student> studentList;
    private ListView studentsLV;
    private Dialog dialog;
    private Student student;
    private DownloadJSONRetrofit downloadJSONRetrofit;
    private EditText nameDialog;
    private EditText lastnameDialog;
    private Spinner spinnerDialog;

    public GroupFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_group, container,false);

        Button addStudentBtn = rootView.findViewById(R.id.addStudentBTN);
        studentsLV = rootView.findViewById(R.id.membersLV);

        //Retrofit
        Retrofit retrofitClass = new Retrofit();
        downloadJSONRetrofit = retrofitClass.createlogInterceptor();

        //Bundle
        Bundle bundle = getArguments();
        studentList = bundle.getParcelableArrayList("Students");

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

                        //Student der Liste hinzufügen
                        DataViewModel viewModel = ViewModelProviders.of((MainActivity) getContext()).get(DataViewModel.class);
                        Integer groupId = viewModel.getStudent().getGroupId();
                        student = new Student(groupId, null ,name, lastname, course, 0);

                        //POST-Request
                        sendStudent();
                        dialog.hide();
                    }
                });
                dialog.show();
            }
        });

        //Adapter setzen
        groupAdapter = new GroupAdapter(getActivity(), studentList);
        studentsLV.setAdapter(groupAdapter);


        return rootView;

    }

    //POST Student zu Gruppe hinzufügen
    private void sendStudent(){
        Call<Object> call = downloadJSONRetrofit.sendObject(student);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (!response.isSuccessful()) {
                    Log.i("ErrorRes:GroupFragment ", String.valueOf(response.code()));
                     Toast.makeText(getContext(),"Gruppe ist voll",Toast.LENGTH_SHORT).show();
                    return;
                }

                Gson gson = new Gson();
                StudentAPI stud = gson.fromJson(gson.toJson(response.body()), StudentAPI.class);
                student = stud.getStudent();
                studentList.add(student);
                groupAdapter = new GroupAdapter(getActivity(), studentList);
                studentsLV.setAdapter(groupAdapter);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("Error GroupFragment", t.getMessage());
            }
        });
    }
}
