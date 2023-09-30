package com.example.myusta;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.util.List;

public class EventCustomAdapter extends ArrayAdapter<Event> {
    List<Event> list;
    Context context;
    int xmlResource;
    int selectedIndex = -1;

    public EventCustomAdapter(@NonNull Context context, int resource, @NonNull List<Event> objects) {
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

        TextView eventType = adapterLayout.findViewById(R.id.eventType);
        TextView gender = adapterLayout.findViewById(R.id.gender3);
        TextView price = adapterLayout.findViewById(R.id.price);
        TextView maxAge = adapterLayout.findViewById(R.id.maxAge);
        eventType.setText(list.get(position).geteventType());
        price.setText(list.get(position).getprice());
        gender.setText(list.get(position).getgender());
        maxAge.setText(String.valueOf(list.get(position).getAge()));
        return adapterLayout;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }


}