package com.example.myusta;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Tournaments extends AppCompatActivity {

    ListView listView;
    List<Tournament> tournamentArrayList;
    CustomAdapter adapter;
    static int NUMBER_CODE = 63475;
    String selectedTournamentId, selectedUrlSegment;
    Button details, back7;

    double latitude;
    double longitude;
    String start;
    String end;
    String distance;
    String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournaments);


        Bundle extras = getIntent().getExtras();
        latitude = extras.getDouble("latitude");
        longitude = extras.getDouble("longitude");
        start = extras.getString("start");
        end = extras.getString("end");
        distance = extras.getString("distance");
        gender = extras.getString("gender");

        listView = findViewById(R.id.listview);
        details = findViewById(R.id.details);
        back7 = findViewById(R.id.back7);

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFourthActivity(v);
            }
        });
        back7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        if(savedInstanceState!= null){

        }else{
            tournamentArrayList = new ArrayList<Tournament>();
            //to use parameters from intent bundle

            getTournaments();
            if (tournamentArrayList.size() > 0) {
                selectedTournamentId = tournamentArrayList.get(0).getTournamentId().toString();
                selectedUrlSegment = tournamentArrayList.get(0).getUrlSegment().toString();
            }

        }
        adapter = new CustomAdapter(this, R.layout.adapter_layout, tournamentArrayList);
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
                selectedTournamentId = adapter.list.get(i).getTournamentId().toString();
                selectedUrlSegment = adapter.list.get(i).getUrlSegment().toString();
                Log.i("tournament",selectedTournamentId);

            }
        });
    }
    public void callFourthActivity(View view){
        Intent sendInfo = new Intent(Tournaments.this, Registration.class);
        //put selected tournamentId
        sendInfo.putExtra("tournamentId",selectedTournamentId);
        sendInfo.putExtra("urlSegment",selectedUrlSegment);
        startActivityForResult(sendInfo, NUMBER_CODE);
    }

    public void setBackColor(AdapterView<?> adapterView, int selectedIndex){
        for (int i=0; i<adapterView.getCount();i++){
            int sel = adapterView.getFirstVisiblePosition();
            if (adapterView.getChildAt(i-sel) != null)
                adapterView.getChildAt(i-sel).setBackgroundColor(i==selectedIndex ? Color.LTGRAY: Color.WHITE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NUMBER_CODE && resultCode == RESULT_OK){

        }
    }

    public void getTournaments() {
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL("https://prd-usta-kube.clubspark.pro/unified-search-api/api/Search/tournaments/Query?indexSchema=tournament");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                        conn.setRequestProperty("Accept","application/json");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        String genderFilter = "";

                        if (gender != null){
                            genderFilter = ",{\"key\":\"event-division-gender\",\"items\":[{\"value\":\""+
                                    gender + "\"},{\"value\":\"" +
                                    gender + "\"},{\"value\":\"" +
                                    gender + "\"}";
                        }
                        String jsonString = "{\"options\":{\"size\":20,\"from\":0,\"sortKey\":\"date\",\"latitude\":"+
                                latitude +",\"longitude\":"+
                                longitude +"},\"filters\":[{\"key\":\"organisation-id\",\"items\":[]},{\"key\":\"location-id\",\"items\":[]},{\"key\":\"region-id\",\"items\":[]},{\"key\":\"publish-target\",\"items\":[{\"value\":1}]},{\"key\":\"level-category\",\"items\":[{\"value\":\"junior\"}],\"operator\":\"Or\"},{\"key\":\"organisation-group\",\"items\":[],\"operator\":\"Or\"},{\"key\":\"date-range\",\"items\":[{\"minDate\":\"" +
                                start + "T00:00:00.000Z\",\"maxDate\":\"" +
                                end + "T03:59:59.999Z\"}],\"operator\":\"Or\"},{\"key\":\"distance\",\"items\":[{\"value\":" +
                                distance + "}],\"operator\":\"Or\"},{\"key\":\"tournament-level\",\"items\":[],\"operator\":\"Or\"}" + genderFilter + "],\"operator\":\"Or\"},{\"key\":\"event-ntrp-rating-level\",\"items\":[],\"operator\":\"Or\"},{\"key\":\"event-division-age-category\",\"items\":[],\"operator\":\"Or\"},{\"key\":\"event-division-event-type\",\"items\":[{\"value\":\"singles\"}],\"operator\":\"Or\"},{\"key\":\"event-court-location\",\"items\":[],\"operator\":\"Or\"}]}";

                        DataOutputStream os = new DataOutputStream(conn.getOutputStream());

                        os.writeBytes(jsonString);

                        os.flush();
                        os.close();

                        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                        if (conn.getResponseCode() ==  200) {
                            InputStream it = new BufferedInputStream(conn.getInputStream());
                            InputStreamReader read = new InputStreamReader(it);
                            BufferedReader buff = new BufferedReader(read);
                            StringBuilder dta = new StringBuilder();
                            String chunks ;
                            while((chunks = buff.readLine()) != null)
                            {
                                dta.append(chunks);
                            }
                            Log.i("TOURNAMENTS RESULT",dta.toString());

                            //get list of tournaments
                            JSONObject result = new JSONObject(dta.toString());
                            JSONArray ja = result.getJSONArray("searchResults");
                            for (int i=0;i<ja.length(); i++){
                                Tournament t = new Tournament();
                                t.setDistance(ja.getJSONObject(i).getDouble("distance"));
                                t.setTournamentId(ja.getJSONObject(i).getJSONObject("item").getString("id"));
                                t.setTournamentName(ja.getJSONObject(i).getJSONObject("item").getString("name"));
                                t.setUrlSegment(ja.getJSONObject(i).getJSONObject("item").getJSONObject("organization").getString("urlSegment"));
                                Log.i("id",t.getTournamentId().toString());
                                Log.i("id",t.getUrlSegment().toString());
                                tournamentArrayList.add(t);
                            }
                            Log.i("num tournaments",String.valueOf(tournamentArrayList.size()));
                        }


                        conn.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
            thread.join();
        }catch(Exception ex){

        }
    }
}