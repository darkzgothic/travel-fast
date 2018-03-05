package com.example.zahidhasan.travelfast.passenger.fragments;


import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zahidhasan.travelfast.R;
import com.example.zahidhasan.travelfast.model.Drivers;
import com.example.zahidhasan.travelfast.model.Passenger;
import com.example.zahidhasan.travelfast.model.PassengerInfo;
import com.example.zahidhasan.travelfast.parser.DataParser;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.DemuxOutputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class PassengerRequestFragment extends Fragment {

    private PlaceAutocompleteFragment placeAutocompleteFragment;
    private MapView mapView;
    private GoogleMap googleMap;
    private Button mainBtn;
    private TextView amountText;

    private List<LatLng> polygon;
    private List<MarkerOptions> mOption;
    private List<Marker> markers;

    private DatabaseReference driverRef;
    private DatabaseReference passengerRef;
    private DatabaseReference passInfoRef;

    private LatLng source;
    private LatLng destination;
    private String phn;

    private String parsedDistance;
    private String response;

    Handler h = new Handler();
    int delay = 10000;
    Runnable runnable;

    public PassengerRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        h.postDelayed(new Runnable() {
            public void run() {

                googleMap.clear();

                if (source != null && destination != null){
                    googleMap.addMarker(new MarkerOptions()
                            .position(source)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    googleMap.addMarker(new MarkerOptions()
                            .position(destination)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                    createPathBetweenSourceDes();
                }else {
                    if (source != null){
                        googleMap.addMarker(new MarkerOptions()
                                .position(source)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    }
                    if (destination != null){
                        googleMap.addMarker(new MarkerOptions()
                                .position(destination)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    }
                }

                for (MarkerOptions op : mOption) {
                    markers.add(googleMap.addMarker((MarkerOptions)op));
                }

                runnable = this;

                h.postDelayed(runnable, delay);
            }
        }, delay);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_passenger_request, container, false);

        driverRef = FirebaseDatabase.getInstance().getReference("Drivers");
        passengerRef = FirebaseDatabase.getInstance().getReference("Passengers");
        passInfoRef = FirebaseDatabase.getInstance().getReference("passengerInfo");

        polygon = new ArrayList<>();
        mOption = new ArrayList<>();
        markers = new ArrayList<>();

        mainBtn = (Button) v.findViewById(R.id.main_btn);
        amountText = (TextView) v.findViewById(R.id.amount_txt);
        amountText.setVisibility(View.INVISIBLE);

        mainBtn.setText("Select Pickup Address");

        placeAutocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.pickupaddress_fragment);

        AutocompleteFilter  autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
                .setCountry("BD")
                .build();
        placeAutocompleteFragment.setFilter(autocompleteFilter);
        placeAutocompleteFragment.setHint("Select Pickup Address");

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
                View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
                rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
                rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                rlp.setMargins(0, 0, 0, 30);


                polygon.add(new LatLng(23.897709, 90.377935));
                polygon.add(new LatLng(23.894256,90.361456));
                polygon.add(new LatLng(23.847162, 90.338453));
                polygon.add(new LatLng(23.750096, 90.338110));
                polygon.add(new LatLng(23.709238, 90.364889));
                polygon.add(new LatLng(23.710809, 90.399221));
                polygon.add(new LatLng(23.635660, 90.471319));
                polygon.add(new LatLng(23.626224, 90.455526));
                polygon.add(new LatLng(23.596024, 90.505651));
                polygon.add(new LatLng(23.693520, 90.520071));
                polygon.add(new LatLng(23.762351, 90.482992));
                polygon.add(new LatLng(23.828320, 90.486425));
                polygon.add(new LatLng(23.893000, 90.456556));
                polygon.add(new LatLng(23.900220, 90.437674));
                polygon.add(new LatLng(23.880757, 90.402998));

                googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        LatLng ll = googleMap.getCameraPosition().target;
                        boolean isInside = PolyUtil.containsLocation(ll, polygon, true);

                        if(isInside){
                            mainBtn.setEnabled(true);
                        }if(!isInside){
                            mainBtn.setEnabled(false);
                        }
                    }
                });
                placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 16));
                    }

                    @Override
                    public void onError(Status status) {

                    }
                });

                driverRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mOption.clear();
                        markers.clear();

                        for (DataSnapshot driverSnapshot: dataSnapshot.getChildren()) {
                            Drivers driver = driverSnapshot.getValue(Drivers.class);

                            Double lat = Double.parseDouble(driver.getLat());
                            Double lng = Double.parseDouble(driver.getLng());

                            LatLng ll = new LatLng(lat, lng);

                            MarkerOptions mo = new MarkerOptions()
                                    .position(ll)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                            mOption.add(mo);

//                            googleMap.addMarker(new MarkerOptions()
//                                    .position(ll)
//                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                mainBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ss = mainBtn.getText().toString().trim();
                        Toast.makeText(getContext(), ss, Toast.LENGTH_SHORT).show();

                        if (ss.equals("Select Pickup Address")){
                            source = googleMap.getCameraPosition().target;
                            googleMap.addMarker(new MarkerOptions()
                                    .position(source)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                            mainBtn.setText("Select Destination Address");
                            placeAutocompleteFragment.setHint("Select Destination Address");
                            placeAutocompleteFragment.setText("");

                        }if(ss.equals("Select Destination Address")){
                            destination = googleMap.getCameraPosition().target;
                            googleMap.addMarker(new MarkerOptions()
                                    .position(destination)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                            mainBtn.setText("Confirm Request");
                            amountText.setText("1000 BDT");
                            amountText.setVisibility(View.VISIBLE);

                            createPathBetweenSourceDes();

                        }if (ss.equals("Confirm Request")){

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                            builder1.setMessage("Confirm request?");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
//                                            String snip = marker.getSnippet();
//                                            String[] phnNum = snip.split("@");

                                            FragmentManager fm = getActivity().getSupportFragmentManager();
                                            FragmentTransaction ft = fm.beginTransaction();
                                            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                                            PassengerWaitingFragment fragment = new PassengerWaitingFragment();
                                            ft.replace(R.id.activity_passenger_main, fragment, fragment.getTag());
                                            ft.commit();

                                            createPassengerRequest();

                                        }
                                    });

                            builder1.setNegativeButton(
                                    "No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }
                    }
                });
                passengerRef.orderByChild("phn").equalTo("01673144088").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Passenger pass = dataSnapshot.getValue(Passenger.class);

                        phn = pass.getPhn();
                        LatLng ll = new LatLng(Double.parseDouble(pass.getSourceLat()), Double.parseDouble(pass.getSourceLng()));

                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 16));
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

//                googleMap.addPolygon(new PolygonOptions().add(
//                        new LatLng(23.897709, 90.377935),
//                        new LatLng(23.894256,90.361456),
//                        new LatLng(23.847162, 90.338453),
//                        new LatLng(23.750096, 90.338110),
//                        new LatLng(23.709238, 90.364889),
//                        new LatLng(23.710809, 90.399221),
//                        new LatLng(23.635660, 90.471319),
//                        new LatLng(23.626224, 90.455526),
//                        new LatLng(23.596024, 90.505651),
//                        new LatLng(23.693520, 90.520071),
//                        new LatLng(23.762351, 90.482992),
//                        new LatLng(23.828320, 90.486425),
//                        new LatLng(23.893000, 90.456556),
//                        new LatLng(23.900220, 90.437674),
//                        new LatLng(23.880757, 90.402998)
//                ));

            }
        });
        return v;
    }

    private void createPassengerRequest() {
        double sLat = source.latitude;
        double sLng = source.longitude;
        String sAdd = getCompleteAddressString(sLat, sLng);

        double dLat = destination.latitude;
        double dLng = destination.longitude;
        String dAdd = getCompleteAddressString(dLat, dLng);

        String phn = "01673144088";
        String status = "Not Accepted";

        Passenger passenger = new Passenger(
                dAdd,
                String.valueOf(dLng),
                String.valueOf(dLat),
                sAdd,
                String.valueOf(sLat),
                String.valueOf(sLng),
                phn,
                status
        );
        passengerRef.child(phn).setValue(passenger);

        PassengerInfo passengerInfo = new PassengerInfo(phn, phn, "Null", "Used");

        passInfoRef.child(phn).setValue(passengerInfo);

        Toast.makeText(getContext(), "Request Sent", Toast.LENGTH_SHORT).show();
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
//                Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
//                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
//            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    private void createPathBetweenSourceDes() {
        if ( source != null && destination != null){
            String url = getUrl(source, destination);
//                                Log.d("onMapClick", url.toString());
            FetchUrl FetchUrl = new FetchUrl();
            // Start downloading json data from Google Directions API
            FetchUrl.execute(url);
        }
    }

    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getLocality()).append("\n");
                result.append(address.getCountryName());
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
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

    public String getDistance(final double lat1, final double lon1, final double lat2, final double lon2){

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=metric&mode=bicycle");
                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    response = IOUtils.toString(in, "UTF-8");

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("routes");
                    JSONObject routes = array.getJSONObject(0);
                    JSONArray legs = routes.getJSONArray("legs");
                    JSONObject steps = legs.getJSONObject(0);
                    JSONObject distance = steps.getJSONObject("distance");
                    parsedDistance=distance.getString("text");

                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return parsedDistance;
    }

}
