package com.example.zahidhasan.travelfast.passenger.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zahidhasan.travelfast.R;
import com.example.zahidhasan.travelfast.passenger.activity.PassengerMainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PassengerPhnFragment extends Fragment {

    private EditText phnEditText;
    private Button loginButton, backButton;

    LocationManager lm;
    boolean gps_enabled = false;
    boolean network_enabled = false;

    public PassengerPhnFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_passenger_phn, container, false);

        initComponent(v);

        loginPassenger();

        return v;
    }

    private void initComponent(View v) {
        phnEditText = (EditText) v.findViewById(R.id.phn_edit_text);
        loginButton = (Button) v.findViewById(R.id.passenger_login);
        backButton = (Button) v.findViewById(R.id.back_button);

        lm = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
    }

    private void loginPassenger() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String phn = phnEditText.getText().toString().trim();
//                if (!phn.matches("^(?:\\+?88)?01[15-9]\\d{8}$")){
//                    phnEditText.setError("Invalid Mobile Number");
//                }
//                else {
//                    if (checkInternetConnectivity() && checkLocationEnabled()){
//
//                        Intent intent = new Intent(getActivity(), PassengerMainActivity.class);
//                        intent.putExtra("phn", phn);
//                        startActivity(intent);
//
//                        getActivity().finish();
//                    }
//                    else {
//                        if (!checkInternetConnectivity()){
//                            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
//                        }if (!checkLocationEnabled()){
//                            checkLocationService();
//                        }
//                    }
//                }
                startActivity(new Intent(getActivity(), PassengerMainActivity.class));
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

            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
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
                            Toast.makeText(getContext(), "Turn On Location Service", Toast.LENGTH_LONG).show();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    public boolean checkInternetConnectivity() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
