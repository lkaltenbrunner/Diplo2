package com.example.kalti.diplo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
//import com.hitomi.cmlibrary.CircleMenu;
//import com.hitomi.cmlibrary.OnMenuSelectedListener;

import java.util.ArrayList;
import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "GoogleMapsActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(isServiceVersionOk()){
            init();
        }

    }


    private void init(){
        Button btnWeather = (Button) findViewById(R.id.btnWeather);
        btnWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessageWeather();
            }
        });


        Button btnMap = (Button) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendMessageGoogleMaps();
        }
        });
    }



/*
        //CircleMenu erstellen
        CircleMenu circleMenu = (CircleMenu)findViewById(R.id.circle_menu);
        //Mainmenu (Hauptmenu)
        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"),R.drawable.touchicon,R.drawable.deleteicon)
                .addSubMenu(Color.parseColor("#E3D823"),R.drawable.profileicon)
                .addSubMenu(Color.parseColor("#B11B16"),R.drawable.weathericon)
                .addSubMenu(Color.parseColor("#005A20"),R.drawable.mapsicon)
                .addSubMenu(Color.parseColor("#A1AB9F"),R.drawable.settingsicon)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int i) {
                        if(menuItems[i].equals("Profile")){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    sendMessageProfile();
                                }
                            }, 1000);
                        }else if(menuItems[i].equals("Settings")){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    sendMessageSettings();
                                }
                            }, 1000);

                        }else if(menuItems[i].equals("GoogleMaps")){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    if(isServiceVersionOk()){
                                        sendMessageGoogleMaps();
                                    }

                                }
                            }, 1000);

                        }else if(menuItems[i].equals("Weather")){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    sendMessageWeather();
                                }
                            }, 1000);

                        }
                    }
                });
*/


    public void sendMessageSettings(){

        Intent switchActivityToSettings = new Intent(this, SettingsActivity.class);
        startActivity(switchActivityToSettings);
    }
    public void sendMessageProfile(){

        Intent switchActivityToProfile = new Intent(this, ProfileActivity.class);
        startActivity(switchActivityToProfile);
    }
    public void sendMessageWeather(){

        Intent switchActivityToWeather = new Intent(this, WeatherActivity.class);
        startActivity(switchActivityToWeather);
    }
    public void sendMessageGoogleMaps(){

        Intent switchActivityToGoogleMaps = new Intent(this, GoogleMapsActivity.class);
        startActivity(switchActivityToGoogleMaps);
    }

    public boolean isServiceVersionOk(){
        Log.d(TAG,"isServiceVersionOk: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //Richtige Version installiert
            Log.d(TAG, "isServiceVersionOk: google-play service is working!");
            return true;

        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //Falsche Version vorhanden
            Log.d(TAG,"isServiceVersionOk: wrong version of the google-play service installed!");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();

        }else{
            Toast.makeText(this, "Map request is not available",Toast.LENGTH_SHORT).show();
        }
        return false;
    }


}
