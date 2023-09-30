package com.example.myusta;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.List;

public class FavoriteCustomAdapter extends ArrayAdapter<Favorite> {
    List<Favorite> list;
    Context context;
    int xmlResource;
    int selectedIndex = -1;

    public FavoriteCustomAdapter(@NonNull Context context, int resource, @NonNull List<Favorite> objects) {
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


        TextView gender = adapterLayout.findViewById(R.id.gender4);
        TextView distance = adapterLayout.findViewById(R.id.distance4);
        TextView zipcode = adapterLayout.findViewById(R.id.zipcode3);

        gender.setText(list.get(position).getGender());
        distance.setText(list.get(position).getDist()+"");
        zipcode.setText(list.get(position).getZipCode());

        return adapterLayout;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
}
