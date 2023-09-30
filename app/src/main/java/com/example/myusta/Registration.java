package com.example.myusta;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Registration extends AppCompatActivity {

    //add widgets for other details
        //name of tournament
        //contact email address, phone
        //address
        //registration closing date
        //list of events with a register button next to each event in this tournament

    Button back;
    Button register;
    static final int NUMBER_CODE = 37462;
    String tournamentId, urlSegment;
    EventCustomAdapter adapter;
    ListView spinner2;
    ArrayList<Event> eventArrayList;
    TextView tourName, director, email, phone, address, closeDate, startDate, endDate;
    String tName, tDirector, tEmail, tPhone, tAddress, tCloseDate, tStartDate, tEndDate;
    DecimalFormat f = new DecimalFormat("###.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Bundle extras = getIntent().getExtras();
        tournamentId = extras.getString("tournamentId");
        urlSegment = extras.getString("urlSegment");

        eventArrayList = new ArrayList<>();

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
        register = findViewById(R.id.register2);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage("https://playtennis.usta.com/Competitions/" + urlSegment + "/Tournaments/Overview/"+tournamentId);
                setResult(RESULT_OK);
                finish();
            }
        });
        spinner2 = findViewById(R.id.spinner2);
        tourName = findViewById(R.id.tourName2);
        director = findViewById(R.id.director2);
        address = findViewById(R.id.address2);
        closeDate = findViewById(R.id.closeDate2);
        phone = findViewById(R.id.phone2);
        email = findViewById(R.id.email2);
        startDate = findViewById(R.id.closeDate4);
        endDate = findViewById(R.id.closeDate6);
        //get tournamentId from intent bundle and use in below
        getTournamentDetails();
        //display details of the tournament
        tourName.setText(tName);
        director.setText(tDirector);
        address.setText(tAddress);
        closeDate.setText(tCloseDate);
        email.setText(tEmail);
        phone.setText(tPhone);
        startDate.setText(tStartDate);
        endDate.setText(tEndDate);

        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate today = LocalDate.now();
            LocalDate close = LocalDate.parse(tCloseDate, formatter);
            if (close.isBefore(today)){
                register.setEnabled(false);
            }
        }
        adapter = new EventCustomAdapter(this, R.layout.adapter_layout2, eventArrayList);
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {

                super.onChanged();
            }
        });
        spinner2.setAdapter(adapter);


    }

    public void callThirdActivity(View view){
        Intent sendInfo = new Intent(Registration.this, MainActivity.class);

        startActivityForResult(sendInfo, NUMBER_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NUMBER_CODE && resultCode == RESULT_OK){

        }
    }

    public void getTournamentDetails() {
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL("https://prd-usta-kube-tournaments.clubspark.pro/");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                        conn.setRequestProperty("Accept", "application/json");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);

                        String jsonString = "{\"operationName\":\"GetTournament\",\"variables\":{\"id\":\"" +
                                tournamentId +
                                "\",\"previewMode\":false},\"query\":\"query GetTournament($id: UUID!, $previewMode: Boolean) {  publishedTournament(id: $id, previewMode: $previewMode) {    id    featureSettings {      enabled      featureId      overridable      __typename    }    commsSettings {      emailReplyTo      phoneNumber      __typename    }    sanctionStatus    name    isPublished    selectionsPublished    tournamentFee    identificationCode    finalisedAndCharged    tournamentFeePayment {      status      tournamentFee      latestChargeTimestamp      __typename    }    organisation {      id      name      __typename    }    registrationRestrictions {      entriesCloseDate      entriesOpenDate      entriesCloseTime      entriesOpenTime      timeZone      entriesCloseDateTime      entriesOpenDateTime      secondsUntilEntriesClose      secondsUntilEntriesOpen      maxEventEntriesPerUser      maxSinglesEntriesPerUser      maxDoublesEntriesPerUser      singleAgeGroupPerPlayer      __typename    }    director {      id      firstName      lastName      __typename    }    websiteContent {      logoPath      photoPath      tournamentDetails      aboutTheOrganiser      entryInformation      hideDraws      hidePlayerList      __typename    }    lastSanctionStatusChange(sanctionStatus: SUBMITTED) {      createdAt      createdByFirstName      createdByLastName      __typename    }    primaryLocation {      id      name      address1      address2      address3      country      county      latitude      longitude      postcode      town      __typename    }    timings {      startDate      endDate      timeZone      startDateTime      __typename    }    level {      __typename      id      name      category      orderIndex      shortName    }    onlineRegistrationEnabled    publishedEvents(previewMode: $previewMode) {      id      sanctionStatus      courtLocation      sanctionStatus      isPublished      formatConfiguration {        entriesLimit        ballColour        drawSize        eventFormat        scoreFormat        selectionProcess        __typename      }      level {        __typename        id        name        category        orderIndex        shortName      }      division {        __typename        ballColour        ageCategory {          __typename          minimumAge          maximumAge          todsCode          type        }        eventType        gender        wheelchairRating        familyType        ratingCategory {          __typename          ratingCategoryType          ratingType          value          minimumValue          maximumValue        }      }      pricing {        entryFee {          amount          currency          __typename        }        __typename      }      registrations {        id        player {          customId {            key            value            __typename          }          firstName          lastName          name          gender          __typename        }        registrationDate        selectionIndex        selectionStatus        __typename      }      surface      timings {        startDateTime        __typename      }      tournament {        id        registrationRestrictions {          entriesCloseDateTime          __typename        }        levelConfiguration {          skillLevel          __typename        }        __typename      }      withdrawals {        player {          customId {            key            value            __typename          }          customIds {            key            value            __typename          }          firstName          lastName          name          gender          __typename        }        registrationDate        __typename      }      teamEventConfiguration {        isDominantDuo        __typename      }      __typename    }    __typename  }}\"}";

                        DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                        os.writeBytes(jsonString);

                        os.flush();
                        os.close();

                        Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                        if (conn.getResponseCode() == 200) {
                            InputStream it = new BufferedInputStream(conn.getInputStream());
                            InputStreamReader read = new InputStreamReader(it);
                            BufferedReader buff = new BufferedReader(read);
                            StringBuilder dta = new StringBuilder();
                            String chunks;
                            while ((chunks = buff.readLine()) != null) {
                                dta.append(chunks);
                            }
                            //get tournament details
                            JSONObject result = new JSONObject(dta.toString());
                            JSONObject tourn = result.getJSONObject("data").getJSONObject("publishedTournament");
                            tName = tourn.getString("name");
                            tDirector = tourn.getJSONObject("director").getString("firstName") + " " + tourn.getJSONObject("director").getString("lastName");
                            tAddress = tourn.getJSONObject("primaryLocation").getString("address1") +
                                    ", \n" + tourn.getJSONObject("primaryLocation").getString("town") +
                                    ", " + tourn.getJSONObject("primaryLocation").getString("county") +
                                    " " + tourn.getJSONObject("primaryLocation").getString("postcode");
                            tCloseDate = tourn.getJSONObject("registrationRestrictions").getString("entriesCloseDate");
                            tEmail = tourn.getJSONObject("commsSettings").getString("emailReplyTo");
                            tPhone = tourn.getJSONObject("commsSettings").getString("phoneNumber");
                            tStartDate = tourn.getJSONObject("timings").getString("startDate");
                            tEndDate = tourn.getJSONObject("timings").getString("endDate");


                            JSONArray ja = tourn.getJSONArray("publishedEvents");
                            for (int i=0;i<ja.length(); i++){
                                Event t = new Event();
                                t.seteventType(ja.getJSONObject(i).getJSONObject("division").getString("eventType"));
                                t.setgender(ja.getJSONObject(i).getJSONObject("division").getString("gender"));
                                double price =ja.getJSONObject(i).getJSONObject("pricing").getJSONObject("entryFee").getInt("amount")/100.0;
                                t.setprice("$" +f.format(price));
                                t.setAge(String.valueOf(ja.getJSONObject(i).getJSONObject("division").getJSONObject("ageCategory").getInt("maximumAge")));
                                eventArrayList.add(t);
                            }

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

    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}