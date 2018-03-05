package com.example.zahidhasan.travelfast.passenger.fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zahidhasan.travelfast.R;
import com.example.zahidhasan.travelfast.model.RideDetails;
import com.example.zahidhasan.travelfast.parser.DataParser;
import com.example.zahidhasan.travelfast.passenger.services.PassengerRideService;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PassengerRideFragment extends Fragment {

    private Button msgBtn, callBtn, cancelBtn;
    private TextView status;

    private DatabaseReference rideDetRef;

    private MapView mapView;
    private GoogleMap googleMap;

    private LatLng driverPos;
    private LatLng passengerPos;
    private LatLng source;
    private LatLng destination;

    public PassengerRideFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_passenger_ride, container, false);

        Context context = getContext();
        Intent intent = new Intent(context, PassengerRideService.class);
        intent.putExtra("id", "01673144088_anto@gmail");
        context.startService(intent);

        msgBtn = (Button) v.findViewById(R.id.message_btn);
        callBtn = (Button) v.findViewById(R.id.call_btn);
        cancelBtn = (Button) v.findViewById(R.id.cancel_btn);
        status = (TextView) v.findViewById(R.id.on_way_txt);

        status.setText("Driver is on the way");

        mapView = (MapView) v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);
            }
        });

        rideDetRef = FirebaseDatabase.getInstance().getReference("rideDetails");

        rideDetRef.orderByChild("id").equalTo("01673144088_anto@gmail").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                RideDetails rideDetails = dataSnapshot.getValue(RideDetails.class);

                driverPos = new LatLng(Double.parseDouble(rideDetails.getDriverLat()),
                        Double.parseDouble(rideDetails.getDriverLng()));

                passengerPos = new LatLng(Double.parseDouble(rideDetails.getPassengerSourceLat()),
                        Double.parseDouble(rideDetails.getPassengerSourceLng()));

                googleMap.addMarker(new MarkerOptions()
                        .position(driverPos)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                googleMap.addMarker(new MarkerOptions()
                        .position(passengerPos)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                createPathBetweenSourceDes(driverPos, passengerPos);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                googleMap.clear();

                RideDetails rideDetails = dataSnapshot.getValue(RideDetails.class);

                if (rideDetails.getRideStatus().equals("Driver is on the way")){
                    status.setText("Driver is on the way");

                    driverPos = new LatLng(Double.parseDouble(rideDetails.getDriverLat()),
                            Double.parseDouble(rideDetails.getDriverLng()));

                    passengerPos = new LatLng(Double.parseDouble(rideDetails.getPassengerSourceLat()),
                            Double.parseDouble(rideDetails.getPassengerSourceLng()));

                    googleMap.addMarker(new MarkerOptions()
                            .position(driverPos)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    googleMap.addMarker(new MarkerOptions()
                            .position(passengerPos)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                    createPathBetweenSourceDes(driverPos, passengerPos);
                }
                if (rideDetails.getRideStatus().equals("Driver arrived")){
                    status.setText("Trip Started");

                    source = new LatLng(Double.parseDouble(rideDetails.getPassengerSourceLat()),
                            Double.parseDouble(rideDetails.getPassengerSourceLng()));
                    destination = new LatLng(Double.parseDouble(rideDetails.getPassengerDesLat()),
                            Double.parseDouble(rideDetails.getPassengerDesLng()));

                    createPathBetweenSourceDes(source, destination);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;
    }

    private void createPathBetweenSourceDes(LatLng source, LatLng destination) {
        if ( source != null && destination != null){
            String url = getUrl(source, destination);
//                                Log.d("onMapClick", url.toString());
            FetchUrl FetchUrl = new FetchUrl();
            // Start downloading json data from Google Directions API
            FetchUrl.execute(url);
        }
    }

    private String getUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false&units=metric&mode=driving";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DataParser parser = new DataParser();
                routes = parser.parse(jObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.RED);
            }
            if(lineOptions != null) {
                googleMap.addPolyline(lineOptions);
            }
            else {
            }
        }
    }

}
