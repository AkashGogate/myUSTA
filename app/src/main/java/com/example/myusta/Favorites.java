package com.example.myusta;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Favorites extends AppCompatActivity {

    Button back, search;
    ListView listView;
    ArrayList<Favorite> favoriteArrayList;
    Favorite selectedFavorite;
    static int NUMBER_CODE = 63476;
    DecimalFormat f = new DecimalFormat("00");
    FavoriteCustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        back = findViewById(R.id.back6);
        search = findViewById(R.id.search3);
        listView = findViewById(R.id.listView2);
        favoriteArrayList = new ArrayList<>();
        readFavorites();
        if (favoriteArrayList.size() > 0)
            selectedFavorite = favoriteArrayList.get(0);
        else
            search.setEnabled(false);

        adapter = new FavoriteCustomAdapter(this, R.layout.favorites_adapter_layout, favoriteArrayList);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
            }
        });
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setSelectedIndex(i);
                setBackColor(adapterView,i);
                selectedFavorite = adapter.list.get(i);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedFavorite != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        callTournamentsActivity(v);
                    }
                }else{
                    Toast toast = Toast.makeText(Favorites.this, "Select A Favorite", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });

    }

    public void readFavorites() {
        SharedPreferences sh = getSharedPreferences("MyFavorites", MODE_PRIVATE);

        String s1 = sh.getString("favs", "");
        System.out.println("Read: " + s1);

        Gson gson = new GsonBuilder().create();

        Favorite[] favs = gson.fromJson(s1, Favorite[].class);

        if (favs != null) {
            for (Favorite f : favs) {
                favoriteArrayList.add(f);
            }
        } else {
            System.out.println("Nothing to show");
        }
    }

    public void updateFavorites() {
        Gson gson = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(favoriteArrayList).getAsJsonArray();
        System.out.println("Write: " + myCustomArray.toString());
        SharedPreferences sharedPreferences = getSharedPreferences("MyFavorites", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // write all the data entered by the user in SharedPreference and apply
        myEdit.putString("favs", myCustomArray.toString());

        myEdit.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void callTournamentsActivity(View view) {

        Intent sendInfo = new Intent(Favorites.this, Tournaments.class);
        sendInfo.putExtra("latitude", selectedFavorite.getLatitude());
        sendInfo.putExtra("longitude", selectedFavorite.getLongitude());
        sendInfo.putExtra("distance", selectedFavorite.getDist()+"");
        String selectedGender = selectedFavorite.getGender();
        Log.i("gender", selectedGender);
        if (!selectedGender.equals("all")) {
            sendInfo.putExtra("gender", selectedGender);
        }

        LocalDate dStart = LocalDate.now();
        int yearS = dStart.getYear();
        int monthOfYearS = dStart.getMonthValue();
        int dayOfMonthS = dStart.getDayOfMonth();

        LocalDate dEnd = dStart.plusDays(selectedFavorite.getNumDays());
        int yearE = dEnd.getYear();
        int monthOfYearE = dEnd.getMonthValue();
        int dayOfMonthE = dEnd.getDayOfMonth();
        String selectedStartDate = yearS + "-" + f.format(monthOfYearS) + "-" + f.format(dayOfMonthS);
        String selectedEndDate = yearE + "-" + f.format(monthOfYearE) + "-" + f.format(dayOfMonthE);

        sendInfo.putExtra("start", selectedStartDate);
        sendInfo.putExtra("end", selectedEndDate);

        //send additional params
        startActivityForResult(sendInfo, NUMBER_CODE);
    }

    public void setBackColor(AdapterView<?> adapterView, int selectedIndex){
        for (int i=0; i<adapterView.getCount();i++){
            int sel = adapterView.getFirstVisiblePosition();
            if (adapterView.getChildAt(i-sel) != null)
                adapterView.getChildAt(i-sel).setBackgroundColor(i==selectedIndex ? Color.LTGRAY: Color.WHITE);
        }
    }
}