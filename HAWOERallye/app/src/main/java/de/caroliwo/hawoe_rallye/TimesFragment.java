package de.caroliwo.hawoe_rallye;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class TimesFragment extends Fragment {

    TimesAdapter timesAdapter;
    List<Task> taskList = new ArrayList<>(); //Platzhalter
    List<String> tasksInhalt = new ArrayList<>(); //Platzhalter

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_times, container, false);
        //ListView mit Zeiten füllen
        ListView timesLV = rootView.findViewById(R.id.timesLV);
        //PLATZHALTER!!!! Daten aus JSON-Objekt holen
        tasksInhalt.add("das hier ist ein Beispieltext");
        taskList.add(new Task ("Wettrennen","ic_car_icon", "12.00 - 15.00 Uhr", "E39 Altbau", 1, tasksInhalt, false, false, true, "Erledigt" ));
        taskList.add(new Task ("Produktionslabor","ic_clapperboard_icon", "11.15 - 13.00 Uhr", "EG im Neubau", 2, tasksInhalt, false, true, true, "Abgeben" ));
        taskList.add(new Task ("Wettrennen","ic_car_icon", "12.00 - 15.00 Uhr", "E39 Altbau", 1, tasksInhalt, false, false, true, "Erledigt" ));
        taskList.add(new Task ("Produktionslabor","ic_clapperboard_icon", "11.15 - 13.00 Uhr", "EG im Neubau", 2, tasksInhalt, false, true, true, "Abgeben" ));
        taskList.add(new Task ("Wettrennen","ic_car_icon", "12.00 - 15.00 Uhr", "E39 Altbau", 1, tasksInhalt, false, false, true, "Erledigt" ));
        taskList.add(new Task ("Produktionslabor","ic_clapperboard_icon", "11.15 - 13.00 Uhr", "EG im Neubau", 2, tasksInhalt, false, true, true, "Abgeben" ));
        taskList.add(new Task ("Wettrennen","ic_car_icon", "12.00 - 15.00 Uhr", "E39 Altbau", 1, tasksInhalt, false, false, true, "Erledigt" ));
        taskList.add(new Task ("Produktionslabor","ic_clapperboard_icon", "11.15 - 13.00 Uhr", "EG im Neubau", 2, tasksInhalt, false, true, true, "Abgeben" ));
        taskList.add(new Task ("Wettrennen","ic_car_icon", "12.00 - 15.00 Uhr", "E39 Altbau", 1, tasksInhalt, false, false, true, "Erledigt" ));
        taskList.add(new Task ("Produktionslabor","ic_clapperboard_icon", "11.15 - 13.00 Uhr", "EG im Neubau", 2, tasksInhalt, false, true, true, "Abgeben" ));

        timesAdapter = new TimesAdapter(getActivity(), taskList);
        timesLV.setAdapter(timesAdapter);

        return rootView;
    }
}
