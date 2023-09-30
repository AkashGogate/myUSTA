package com.example.myusta;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<Tournament> {
    List<Tournament> list;
    Context context;
    int xmlResource;
    int selectedIndex = -1;
    DecimalFormat f = new DecimalFormat("##.00");


    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<Tournament> objects) {
        super(context, resource, objects);
        xmlResource = resource;
        list = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View adapterLayout = layoutInflater.inflate(xmlResource, null);

        TextView name = adapterLayout.findViewById(R.id.name);
        TextView distance = adapterLayout.findViewById(R.id.distance);
        name.setText(list.get(position).getTournamentName());
        distance.setText(f.format(list.get(position).getDistance())+ " Miles");

        return adapterLayout;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }


}