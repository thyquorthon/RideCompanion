package com.qthlabs.ride.ridecompanion;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity {

    boolean tracking = false;
    final Activity _self = this;
    private EditText editTrackLog;
    private Button btnTrack;
    private GPSTracker gps;
    public float[] gVector = new float[3];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTrack = (Button) findViewById(R.id.btnTrack);
        editTrackLog = (EditText) findViewById(R.id.editTrackLog);

        // **********************************************************************
        // BUTTON START / STOP RECORDING
        btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!tracking){
                    tracking = true;
                    startService(new Intent(_self, LocationListening_service.class));
                    LocalBroadcastManager.getInstance(_self).registerReceiver(mLocationReceiver, new IntentFilter("location"));
                } else {
                    tracking = false;
                    LocalBroadcastManager.getInstance(_self).unregisterReceiver(mLocationReceiver);
                    stopService(new Intent(_self, LocationListening_service.class));
                }
            }
        });
    }


    // handler for received Intents for the "my-event" event
    private BroadcastReceiver mLocationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            Location location = intent.getExtras().getParcelable("location");
            Log.i("receiver", "Got position: " + location.getLatitude() + " : " + location.getLongitude() );
            editTrackLog.append("\nYour Location is - \nLat: " + location.getLatitude() + "\nLong: " + location.getLongitude());
            //float[] a = gVector;
        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

