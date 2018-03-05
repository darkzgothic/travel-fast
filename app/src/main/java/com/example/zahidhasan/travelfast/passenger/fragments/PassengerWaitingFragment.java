package com.example.zahidhasan.travelfast.passenger.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.zahidhasan.travelfast.R;
import com.example.zahidhasan.travelfast.model.Passenger;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class PassengerWaitingFragment extends Fragment {

    private TextView status;
    private Button openMap;

    private DatabaseReference passenger;


    public PassengerWaitingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_passenger_waiting, container, false);

        status = (TextView) v.findViewById(R.id.status_txt);
        openMap = (Button) v.findViewById(R.id.open_map_btn);
        openMap.setVisibility(View.INVISIBLE);
        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                PassengerRideFragment fragment = new PassengerRideFragment();
                ft.replace(R.id.activity_passenger_main, fragment, fragment.getTag());
                ft.commit();
            }
        });
        passenger = FirebaseDatabase.getInstance().getReference("Passengers");

        passenger.orderByChild("phn").equalTo("01673144088").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Passenger passenger = dataSnapshot.getValue(Passenger.class);

                String str = passenger.getStatus();

                if (str.equals("Not Accepted")){
                    status.setText("Request not accepted yet");
                    openMap.setVisibility(View.INVISIBLE);
                }
                if (str.equals("Accepted")){
                    status.setText("Request accepted");
                    openMap.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Passenger passenger = dataSnapshot.getValue(Passenger.class);

                String str = passenger.getStatus();

                if (str.equals("Not Accepted")){
                    status.setText("Request not accepted yet");
                    openMap.setVisibility(View.INVISIBLE);
                }
                if (str.equals("Accepted")){
                    status.setText("Request accepted");
                    openMap.setVisibility(View.VISIBLE);
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

}
