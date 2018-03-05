package com.example.zahidhasan.travelfast;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.zahidhasan.travelfast.driver.activity.VerificationActivity;

public class MainActivity extends AppCompatActivity {

    private Button driverBtn, passengerBtn;
    LocationManager lm;
    boolean gps_enabled = false;
    boolean network_enabled = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        driverBtn = (Button) findViewById(R.id.driver_button);
        passengerBtn = (Button) findViewById(R.id.passenger_button);

        lm = (LocationManager)getBaseContext().getSystemService(Context.LOCATION_SERVICE);

        driverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternetConnectivity() && checkLocationEnabled()){
                    startActivity(new Intent(MainActivity.this, BaseActivity.class));
                }
                else {
                    if (!checkInternetConnectivity()){
                        Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }if (!checkLocationEnabled()){
                        checkLocationService();
                    }
                }
            }
        });
        passengerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternetConnectivity() && checkLocationEnabled()){
                    startActivity(new Intent(MainActivity.this, PassengerBaseActivity.class));
                }
                else {
                    if (!checkInternetConnectivity()){
                        Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }if (!checkLocationEnabled()){
                        checkLocationService();
                    }
                }
            }
        });
    }

    private boolean checkLocationEnabled(){
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled){

            return false;
        }
        return true;
    }

    private void checkLocationService() {
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
            builder1.setMessage("Want to enable location service?");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Toast.makeText(MainActivity.this, "Turn On Location Service", Toast.LENGTH_LONG).show();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    public boolean checkInternetConnectivity() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
