package de.caroliwo.hawoe_rallye.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import de.caroliwo.hawoe_rallye.DownloadJSONRetrofit;
import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Retrofit;
import de.caroliwo.hawoe_rallye.Task;
import de.caroliwo.hawoe_rallye.TaskAPI;
import de.caroliwo.hawoe_rallye.Fragments.TimesAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimesFragment extends Fragment {

    TimesAdapter timesAdapter;
    ArrayList<Task> taskList;
    List<Task> tasks;
    DownloadJSONRetrofit downloadJSONRetrofit;
    int groupID = 1; //TODO: aus interner Datenbank laden
    ListView timesLV;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_times, container, false);

        //ListView erstellen
        timesLV = rootView.findViewById(R.id.timesLV);

        //Retrofit
        Retrofit retrofitClass = new Retrofit();
        downloadJSONRetrofit = retrofitClass.createlogInterceptor();

        //Methode, um HTTP-Request durchzuführen
        getTimes();

        return rootView;
    }

    //TODO: löschen bis auf Adapterimplementation, wenn interne Datenbank funktioniert
    private void getTimes() {
        Call<TaskAPI> call = downloadJSONRetrofit.getTasks(groupID);

        //execute on background-thread
        call.enqueue(new Callback<TaskAPI>() {
            @Override
            public void onResponse(Call<TaskAPI> call, Response<TaskAPI> response) {
                //wenn HTTP-Request nicht erfolgreich:
                if (!response.isSuccessful()) {
                    Log.i("TEST ErrorResponse: ", String.valueOf(response.code()));
                    return;
                }

                //wenn HTTP-Request erfolgreich:
                TaskAPI taskAPI = response.body();
                tasks = taskAPI.getTaskList();
                taskList= new ArrayList<>(tasks); //List in ArrayList umwandeln
                timesAdapter = new TimesAdapter(getActivity(), taskList); //timesAdapter mit Listeninhalten füllen
                timesLV.setAdapter(timesAdapter);
            }

            @Override
            public void onFailure(Call<TaskAPI> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("TEST Error", t.getMessage() + "Error");
            }
        });
    }
}
