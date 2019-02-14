package de.caroliwo.hawoe_rallye.Fragments;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Student;

public class GroupAdapter extends ArrayAdapter {

    private List<Student> studentList;
    private final Activity context;


    //Konstruktor
    public GroupAdapter(Activity context, ArrayList<Student> studentList) {
        super(context, R.layout.group_fragment_listview_layout, studentList);
        this.context=context;
        this.studentList=studentList;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.group_fragment_listview_layout, null, true);

        TextView name = rowView.findViewById(R.id.nameTV);
        TextView lastname = rowView.findViewById(R.id.lastnameTV);
        TextView subject = rowView.findViewById(R.id.subjectTV);

        name.setText(studentList.get(position).getFirst_name());
        lastname.setText(studentList.get(position).getLast_name());
        subject.setText(studentList.get(position).getCourse());

        return rowView;
    }
}
