package com.example.myusta;

//MUST be able to save data from a previous run of an application

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myusta.databinding.ActivityFavoritesBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity{
    Button tournament, favorites;
    ConstraintLayout background;
    ImageView imageView;
    Button about;

    static final int NUMBER_CODE = 1234;
    static final int CODE_NUMBER = 44568;
    static final int ABOUT_CODE = 44569;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        tournament = findViewById(R.id.tournaments);
        favorites = findViewById(R.id.favorites);

        background = findViewById(R.id.background);
        imageView = findViewById(R.id.imageView);
        about = findViewById(R.id.about);

        //background.setBackgroundColor(Color.rgb(4, 7, 32));
        background.setBackgroundColor(Color.rgb(0, 1, 8));//0.1.10

        AnimatedGifImageView gifImageView = (AnimatedGifImageView) findViewById(R.id.animatedGifImageView2);
        gifImageView.setGifImageResource(R.drawable.tennis_hit);
        gifImageView.setScaleX(2.25f);
        gifImageView.setScaleY(3);


        tournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callSecondActivity(view);

            }
        });

        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAnotherActivity(view);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAboutActivity(v);
            }
        });






/*
        //Set Preference
        SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
        SharedPreferences.Editor prefsEditor;
        prefsEditor = myPrefs.edit();
        prefsEditor.putString("REFKEY", valuetobestored);
        prefsEditor.commit();

        //Get Preferenece
        SharedPreferences myPrefs;
        myPrefs = getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
        String output=myPrefs.getString("REFKEY", "");
    */


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String readAllLines(BufferedReader reader) throws IOException {
        StringBuilder content = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            content.append(line);
            content.append(System.lineSeparator());
        }

        return content.toString();
    }

    public void callSecondActivity(View view){
        Intent sendInfo = new Intent(MainActivity.this, Settings.class);

        startActivityForResult(sendInfo, NUMBER_CODE);
    }
    public void callAnotherActivity(View view){
        Intent sendInfo = new Intent(MainActivity.this, Favorites.class);

        startActivityForResult(sendInfo, CODE_NUMBER);
    }

    public void callAboutActivity(View view){
        Intent sendInfo = new Intent(MainActivity.this, AboutActivity.class);

        startActivityForResult(sendInfo, ABOUT_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NUMBER_CODE && resultCode == RESULT_OK){

        }
    }

}