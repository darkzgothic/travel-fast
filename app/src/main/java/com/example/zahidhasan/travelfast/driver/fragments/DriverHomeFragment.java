package com.example.zahidhasan.travelfast.driver.fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverHomeFragment extends Fragment {

    private double driverLat;
    private double driverLng;

    private MapView mapView;
    private GoogleMap googleMap;

    private List<Marker> markers = new ArrayList<>();
    private List<MarkerOptions> mOption = new ArrayList<>();
    private List<Marker> passengerMarkers = new ArrayList<>();
    private List<MarkerOptions> pmOption = new ArrayList<>();

    private List<String> location = new ArrayList<>();
    private List<String> userLocation = new ArrayList<>();

    DatabaseReference refDriver;
    DatabaseReference refPassenger;
    FirebaseAuth mAuth;
    FirebaseUser driver;

    DataSnapshot passengerDataSnapshot;

    Handler h = new Handler();
    int delay = 10000;
    Runnable runnable;
    ProgressDialog progress;

    public DriverHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        h.postDelayed(new Runnable() {
            public void run() {

                googleMap.clear();

                for (MarkerOptions op : mOption) {
                    markers.add(googleMap.addMarker((MarkerOptions)op));
                }

                for (MarkerOptions op : pmOption) {
                    markers.add(googleMap.addMarker((MarkerOptions) op));
                }

                runnable = this;

                h.postDelayed(runnable, delay);
            }
        }, delay);
    }

    @Override
    public void onPause() {
        super.onPause();
        h.removeCallbacks(runnable);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_driver_home, container, false);

        location = new ArrayList<>();

        refDriver = FirebaseDatabase.getInstance().getReference("Drivers");
        refPassenger = FirebaseDatabase.getInstance().getReference("Passengers");
        mAuth = FirebaseAuth.getInstance();
        driver = mAuth.getCurrentUser();

        getDriverLocation();

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

                setMap();
            }
        });

        return v;
    }

    private void setMap() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
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

//        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
//        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
//        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//        rlp.setMargins(0, 0, 30, 30);

        LatLng dhaka = new LatLng(driverLat, driverLng);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dhaka, 15));

        //Set Marker Info Window
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            View view;
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                if(marker.getTitle().equals("Driver")){
                    view = getActivity().getLayoutInflater().inflate(R.layout.info_window_driver, null);


                    TextView textView2 = (TextView) view.findViewById(R.id.textView_driver);
                    TextView textView3 = (TextView) view.findViewById(R.id.textView_driver_online);

                    textView2.setText(marker.getTitle());
                    textView3.setText(marker.getSnippet());
                }
                else {
                    view = getActivity().getLayoutInflater().inflate(R.layout.info_window, null);

                    String sni = marker.getSnippet().toString();
                    String[] str = sni.split("@");

                    TextView textView2 = (TextView) view.findViewById(R.id.textView2);
                    TextView textView3 = (TextView) view.findViewById(R.id.textView3);
                    TextView textView4 = (TextView) view.findViewById(R.id.textView4);

                    textView2.setText(marker.getTitle());
                    textView3.setText(str[0]);
                    textView4.setText(str[1]);
                }
                return view;
            }
        });
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {
                if(marker.getTitle().equals("Driver")){

                    Toast.makeText(getContext(), "Driver online", Toast.LENGTH_SHORT).show();
                }
                if(marker.getTitle().equals("Passenger")){

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage("Do you want to accept this ride request?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String snip = marker.getSnippet();
                                    String[] phnNum = snip.split("@");

                                    FragmentManager fm = getActivity().getSupportFragmentManager();
                                    FragmentTransaction ft = fm.beginTransaction();
                                    ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                                    DriverRideFragment fragment = new DriverRideFragment();
                                    ft.replace(R.id.content_driver_main, fragment, fragment.getTag());
                                    ft.commit();

                                    marker.remove();
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

        //Add Driver Marker
        refDriver.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                markers.clear();
                mOption.clear();
                for (DataSnapshot driverSnapshot : dataSnapshot.getChildren()) {
                    String lat = driverSnapshot.child("lat").getValue().toString();
                    String lng = driverSnapshot.child("lng").getValue().toString();

                    LatLng ll = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
                    MarkerOptions op = new MarkerOptions()
                            .position(ll)
                            .title("Driver")
                            .snippet("Online")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    mOption.add(op);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Add Passenger Marker
        refPassenger.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                passengerMarkers.clear();
                pmOption.clear();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String lat = userSnapshot.child("sourceLat").getValue().toString();
                    String lng = userSnapshot.child("sourceLng").getValue().toString();
                    String phn = userSnapshot.child("phn").getValue().toString();
                    String des_add = userSnapshot.child("destinationAdd").getValue().toString();

                    LatLng ll = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));

                    MarkerOptions op = new MarkerOptions()
                            .position(ll)
                            .title("Passenger")
                            .snippet(phn + " @ " + des_add)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    pmOption.add(op);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getDriverLocation() {
        String email = driver.getEmail();

        refDriver.orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Drivers driver = dataSnapshot.getValue(Drivers.class);

                driverLat = Double.parseDouble(driver.getLat());
                driverLng = Double.parseDouble(driver.getLng());
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
    }

}
