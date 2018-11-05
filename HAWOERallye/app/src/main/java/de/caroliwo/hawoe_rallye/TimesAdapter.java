package de.caroliwo.hawoe_rallye;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class TimesAdapter extends ArrayAdapter {

    /*wird benötigt, um Informationen aus den Task-Objekten aus dem JSON Download
    in Gruppen nach Labor einzuteilen und dann diese Gruppen in ein Layout einzufügen,
    welches in TimesFragment in eine ListView gesetzt wird.*/

    private List<Task> taskList;
    private final Activity context;

    //Konstruktor
    public TimesAdapter(Activity context, List<Task> taskList) {
        super(context, R.layout.times_listview_layout, taskList);
        this.context=context;
        this.taskList=taskList;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.times_listview_layout, null, true);

        ImageView laborIconIV = rowView.findViewById(R.id.laborIconIV);
        TextView laborNameTV = rowView.findViewById(R.id.laborNameTV);
        TextView locationTV = rowView.findViewById(R.id.locationTV);
        TextView timeTV = rowView.findViewById(R.id.timeTV);

        String iconname = taskList.get(position).getIcon();
        int id = context.getResources().getIdentifier(iconname, "drawable", context.getPackageName());
        laborIconIV.setImageResource(id);

        laborNameTV.setText(taskList.get(position).getName());
        locationTV.setText(taskList.get(position).getDestination());
        timeTV.setText(taskList.get(position).getTime());

        return rowView;
    }
}
