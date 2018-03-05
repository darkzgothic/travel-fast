package com.example.zahidhasan.travelfast.driver.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zahidhasan.travelfast.R;
import com.example.zahidhasan.travelfast.model.Drivers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EarningFragment extends Fragment {

    private TextView completedRide, balance;

    private DatabaseReference driverRef;
    private FirebaseAuth mAuth;
    private FirebaseUser driver;

    public EarningFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_earning, container, false);

        completedRide = (TextView) v.findViewById(R.id.completed_ride);
        balance = (TextView) v.findViewById(R.id.balance);

        driverRef = FirebaseDatabase.getInstance().getReference("Drivers");
        mAuth = FirebaseAuth.getInstance();
        driver = mAuth.getCurrentUser();

        getData();

        return v;
    }

    private void getData() {
        String _email = driver.getEmail();

        driverRef.orderByChild("email").equalTo(_email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Drivers drivers = dataSnapshot.getValue(Drivers.class);

                completedRide.setText(drivers.getCompletedRide());
                balance.setText(drivers.getBalance());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Drivers drivers = dataSnapshot.getValue(Drivers.class);

                completedRide.setText(drivers.getCompletedRide());
                balance.setText(drivers.getBalance());
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
