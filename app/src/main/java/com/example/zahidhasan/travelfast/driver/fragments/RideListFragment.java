package com.example.zahidhasan.travelfast.driver.fragments;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.zahidhasan.travelfast.R;
import com.example.zahidhasan.travelfast.model.RideDetails;
import com.example.zahidhasan.travelfast.model.RideList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class RideListFragment extends Fragment {

    private ListView listViewRides;
    private List<RideDetails> rideList;

    private DatabaseReference rideDetailsRef;
    private FirebaseAuth mAuth;
    private FirebaseUser driver;

    public RideListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ride_list, container, false);

        listViewRides = (ListView) v.findViewById(R.id.listViewItems);
        rideList = new ArrayList<>();

        rideDetailsRef = FirebaseDatabase.getInstance().getReference("rideDetails");
        mAuth = FirebaseAuth.getInstance();
        driver = mAuth.getCurrentUser();

        rideDetailsRef.orderByChild("driverEmail").equalTo(driver.getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rideList.clear();
                for (DataSnapshot rideSnapshot : dataSnapshot.getChildren()) {
                    RideDetails ride = rideSnapshot.getValue(RideDetails.class);
                    rideList.add(ride);
                }
                final RideList rideAdapter = new RideList(getActivity(), rideList);
                listViewRides.setAdapter(rideAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;
    }

}
