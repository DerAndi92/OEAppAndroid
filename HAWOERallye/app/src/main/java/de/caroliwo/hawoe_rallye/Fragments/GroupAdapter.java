package de.caroliwo.hawoe_rallye.fragments;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.User;

public class GroupAdapter extends ArrayAdapter {

    private List<User> userList;
    private final Activity context;

    //Konstruktor
    public GroupAdapter(Activity context, ArrayList<User> userList) {
        super(context, R.layout.group_fragment_listview_layout, userList);
        this.context=context;
        this.userList=userList;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.group_fragment_listview_layout, null, true);

        TextView name = rowView.findViewById(R.id.nameTV);
        TextView lastname = rowView.findViewById(R.id.lastnameTV);
        TextView subject = rowView.findViewById(R.id.subjectTV);


        name.setText(userList.get(position).getName());
        lastname.setText(userList.get(position).getLastname());
        subject.setText(userList.get(position).getSubject());

        return rowView;
    }
}
