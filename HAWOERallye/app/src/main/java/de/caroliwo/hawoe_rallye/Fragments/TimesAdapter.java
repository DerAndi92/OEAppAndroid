package de.caroliwo.hawoe_rallye.Fragments;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Task;

public class TimesAdapter extends ArrayAdapter {

    /*wird benötigt, um Informationen aus den Task-Objekten aus dem JSON Download
    in Gruppen nach Labor einzuteilen und dann diese Gruppen in ein Layout einzufügen,
    welches in TimesFragment in eine ListView gesetzt wird.*/

    private List<Task> taskList;
    private final Activity context;

    //Konstruktor
    public TimesAdapter(Activity context, ArrayList<Task> taskList) {
        super(context, R.layout.times_listview_layout2, taskList);
        this.context = context;
        this.taskList = taskList;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.times_listview_layout2, null, true);

        ImageView laborIconIV = rowView.findViewById(R.id.laborIconIV);
        TextView laborNameTV = rowView.findViewById(R.id.laborNameTV);
        TextView locationTV = rowView.findViewById(R.id.locationTV);
        TextView timeTV = rowView.findViewById(R.id.timeTV);

        //Icon
        String iconName = taskList.get(position).getIcon();
        int id = context.getResources().getIdentifier(iconName, "drawable", "de.caroliwo.hawoe_rallye");
        laborIconIV.setImageResource(id);

        //Name, Destination
        laborNameTV.setText(taskList.get(position).getName());
        locationTV.setText(taskList.get(position).getDestination());

        //Time
        String time_from = (String) taskList.get(position).getTime().getTime_from();
        String time_to = (String) taskList.get(position).getTime().getTime_to();
        if (time_from != null && time_to != null) {
            timeTV.setText(time_from + "-" + time_to + " Uhr");
        } else if (time_from != null && time_to== null) {
            timeTV.setText(time_from + " Uhr");
        }
        else {
            timeTV.setText("Durchgehend geöffnet");
        }

        return rowView;
    }

    public void setTasks(ArrayList<Task> tasks) {
        taskList.clear();
        taskList.addAll(tasks);
        notifyDataSetChanged();
    }
}
