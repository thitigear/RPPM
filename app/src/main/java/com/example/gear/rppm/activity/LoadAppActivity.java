package com.example.gear.rppm.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.gear.rppm.R;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

public class LoadAppActivity extends AppCompatActivity {//implements BootstrapNotifier, BeaconConsumer {

    private Button load_app_button;
    private boolean isClickButton = false;

    private BeaconManager myBeaconManager;
    private RegionBootstrap regionBootstrap;

    private String TAG = "APPLICATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_app);

        load_app_button = (Button) findViewById(R.id.load_app_button1);
        load_app_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClickButton = true;

            }
        });

        /**myBeaconManager = BeaconManager.getInstanceForApplication(this);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        myBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        myBeaconManager.bind(this);

        // wake up the app when any beacon is seen (you can specify specific id filers in the parameters below)
        Region region = new Region("com.example.rppm.activity.boostrapRegion", null, null, null);
        regionBootstrap = new RegionBootstrap(this, region);*/


        Thread loadAppThread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    //sleep(5000);  //Delay of 10 seconds
                    while(!isClickButton){sleep(10);}

                } catch (Exception e) {

                } finally {
                    //Start MainActivity
                    Intent i = new Intent(getApplicationContext() ,MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        loadAppThread.start();



        Log.e(TAG, "HEREEEEEEEEEEEEEEEEEEEEEE");

    }
/**
    @Override
    public void didEnterRegion(Region region) {
        Log.e(TAG, "Got a didEnterRegion call!!!" + region);
        // This call to disable will make it so the activity below only gets launched the first time a beacon is seen (until the next time the app is launched)
        // if you want the Activity to launch every single time beacons come into view, remove this call.
        regionBootstrap.disable();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        // IMPORTANT: in the AndroidManifest.xml definition of this activity, you must set android:launchMode="singleInstance" or you will get two instances
        // created when a user launches the activity manually and it gets launched from here.
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    @Override
    public void didExitRegion(Region region) {

    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {

    }

    @Override
    public void onBeaconServiceConnect() {

    }*/
}
