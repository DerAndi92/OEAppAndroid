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
import java.util.Date;

public class GroupFragment extends Fragment {

    GroupAdapter groupAdapter;
    ArrayList<User> userList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_group, container,false);


        ListView membersLV = rootView.findViewById(R.id.membersLV);

        //ListView mit Usern füllen
        userList = new ArrayList<User>();//Platzhalter
        userList.add(new User ("Kathajrina", "Schmidt", "MT", new Date()));
        userList.add(new User ("Egon", "Schmand", "MS", new Date()));
        //TODO: userList mit Usern füllen--> JSON download der aktuellen User-Tabelle für eigene Gruppen-ID

        //Adapter setzen
        groupAdapter = new GroupAdapter(getActivity(),userList);
        membersLV.setAdapter(groupAdapter);


        return rootView;

    }
}
