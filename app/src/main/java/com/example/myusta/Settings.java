package com.example.myusta;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Settings extends AppCompatActivity {
    Button confirmation, back3, addFav;
    EditText dist, zipCode;
    DatePicker date1 = null;
    DatePicker date2 = null;
    double latitude, longitude;
    Spinner gender;
    String selectedStartDate = "";
    String selectedEndDate = "";
    DecimalFormat f = new DecimalFormat("00");
    ArrayList<Favorite> favList = new ArrayList<>();

    static final int NUMBER_CODE = 345326;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        confirmation = findViewById(R.id.confirm);
        date1 = findViewById(R.id.date1);
        date2 = findViewById(R.id.date2);
        dist = findViewById(R.id.dist);
        zipCode = findViewById(R.id.zipcode);
        gender = findViewById(R.id.spinner);
        back3 = findViewById(R.id.back3);
        addFav = findViewById(R.id.addFav);

        readFavorites();

        String[] strings = new String[]{"boys", "girls", "all"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, strings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);

        LocalDate d = LocalDate.now();
        //init
        dist.setText("10");
        dist.setInputType(2);
        zipCode.setText("08852");
        int year = d.getYear();
        int monthOfYear = d.getMonthValue();
        int dayOfMonth = d.getDayOfMonth();


        selectedStartDate = year + "-" + f.format(monthOfYear) + "-" + f.format(dayOfMonth);
        selectedEndDate = year + "-" + f.format(monthOfYear + 1) + "-" + f.format(dayOfMonth);

        date2.init(year, monthOfYear-1, dayOfMonth, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedStartDate = year + "-" + f.format(monthOfYear+1) + "-" + f.format(dayOfMonth);
            }
        });

        date1.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedEndDate = year + "-" + f.format(monthOfYear+1) + "-" + f.format(dayOfMonth);
            }
        });

        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callThirdActivity(view);
            }
        });

        back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCurrentSearchToFav();
            }
        });
    }

    public void callThirdActivity(View view) {

        //use zipcode entered in search
        String selectedZipCode = zipCode.getText().toString();
        getLocation(selectedZipCode);
        if (latitude >=0) {
            Intent sendInfo = new Intent(Settings.this, Tournaments.class);
            sendInfo.putExtra("latitude", latitude);
            sendInfo.putExtra("longitude", longitude);
            sendInfo.putExtra("start", selectedStartDate);
            sendInfo.putExtra("end", selectedEndDate);
            sendInfo.putExtra("distance", dist.getText().toString());
            String selectedGender = gender.getSelectedItem().toString();
            Log.i("gender", selectedGender);
            if (!selectedGender.equals("all")) {
                sendInfo.putExtra("gender", selectedGender);
            }

            //send additional params
            startActivityForResult(sendInfo, NUMBER_CODE);
        }else{
            Toast toast3 = Toast.makeText(Settings.this, "INVALID ZIPCODE", Toast.LENGTH_LONG);
            toast3.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NUMBER_CODE && resultCode == RESULT_OK) {
            System.out.println("HERE");
        }
    }

    public void getLocation(String zipCode) {
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL("https://playtennis.usta.com/v0/Geocoding/LocationSearch?q=" + zipCode);
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        int statusCode = urlConnection.getResponseCode();
                        if (statusCode == 200) {
                            InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                            InputStreamReader read = new InputStreamReader(it);
                            BufferedReader buff = new BufferedReader(read);
                            StringBuilder dta = new StringBuilder();
                            String chunks;
                            while ((chunks = buff.readLine()) != null) {
                                dta.append(chunks);
                            }
                            JSONObject result = new JSONObject(dta.toString());
                            JSONArray ja = result.getJSONArray("Places");
                            if (ja.length()>0) {
                                latitude = ja.getJSONObject(0).getDouble("Latitude");
                                longitude = ja.getJSONObject(0).getDouble("Longitude");
                                Log.i("LATITUDE", String.valueOf(latitude));
                                Log.i("LONGITUDE", String.valueOf(longitude));
                            }
                            else{
                                latitude = -1;
                                longitude = -1;
                            }


                        } else {
                            //Handle else
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
            thread.join();
        } catch (Exception ex) {

        }
    }

    public void readFavorites() {
        SharedPreferences sh = getSharedPreferences("MyFavorites", MODE_PRIVATE);

        String s1 = sh.getString("favs", "");
        System.out.println("Read: " + s1);

        Gson gson = new GsonBuilder().create();

        Favorite[] favs = gson.fromJson(s1, Favorite[].class);

        if (favs != null) {
            for (Favorite f : favs) {
                favList.add(f);
            }
        } else {
            System.out.println("Nothing to show");
        }
    }

    public void addCurrentSearchToFav() {
        int day1 = date1.getDayOfMonth();
        int month1 = date1.getMonth();
        int year1 =  date1.getYear();

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(year1, month1, day1);

        int day2 = date2.getDayOfMonth();
        int month2 = date2.getMonth();
        int year2 =  date2.getYear();

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(year2, month2, day2);

        long numDays = TimeUnit.MILLISECONDS.toDays(Math.abs(calendar1.getTimeInMillis() - calendar2.getTimeInMillis()));

        String selectedZipCode = zipCode.getText().toString();
        getLocation(selectedZipCode);

        if (latitude >= 0) {
            Favorite fav = new Favorite(latitude, longitude, gender.getSelectedItem().toString(), Integer.parseInt(dist.getText().toString()), numDays, zipCode.getText().toString());
            favList.add(fav);
            updateFavorites();
        }else{
            Toast toast4 = Toast.makeText(Settings.this, "INVALID ZIPCODE", Toast.LENGTH_LONG);
            toast4.show();
        }
    }

    public void updateFavorites() {
        Gson gson = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(favList).getAsJsonArray();
        System.out.println("Write: " + myCustomArray.toString());
        SharedPreferences sharedPreferences = getSharedPreferences("MyFavorites", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // write all the data entered by the user in SharedPreference and apply
        myEdit.putString("favs", myCustomArray.toString());

        myEdit.apply();
    }
}