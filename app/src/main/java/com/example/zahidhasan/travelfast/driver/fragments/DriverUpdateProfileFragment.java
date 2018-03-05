package com.example.zahidhasan.travelfast.driver.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.zahidhasan.travelfast.R;
import com.example.zahidhasan.travelfast.model.Drivers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverUpdateProfileFragment extends Fragment {

    private EditText name, email, phn, bikeReg, nid, address;
    private Spinner gender;
    private Button updateBtn, backBtn;

    private DatabaseReference driverRef;
    private FirebaseAuth mAuth;
    private FirebaseUser driver;


    public DriverUpdateProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_driver_update_profile, container, false);

        initComponent(v);

        getDriverData();

        updateProfile();

        return v;
    }

    private void getDriverData() {
        String _email = driver.getEmail();

        driverRef.orderByChild("email").equalTo(_email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Drivers drivers = dataSnapshot.getValue(Drivers.class);

                email.setText(drivers.getEmail());
                name.setText(drivers.getName());
                phn.setText(drivers.getPhn());
                bikeReg.setText(drivers.getBikeRegNo());
                nid.setText(drivers.getNid());
                address.setText(drivers.getAddress());

                String gen = drivers.getGender();

                if (gen.equals("Male")){
                    gender.setSelection(0);
                }
                else if (gen.equals("Female")){
                    gender.setSelection(1);
                }
                else if (gen.equals("Others")){
                    gender.setSelection(2);
                }
                else {
                    gender.setSelection(0);
                }

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

    private void updateProfile() {
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String _name = name.getText().toString().trim();
                String _email = driver.getEmail();
                final String _phn = phn.getText().toString().trim();
                final String _bikeReg = bikeReg.getText().toString().trim();
                final String _nid = nid.getText().toString().trim();
                final String _address = address.getText().toString().trim();
                final String _gender = gender.getSelectedItem().toString().trim();

                driverRef.orderByChild("email").equalTo(_email).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        Drivers drivers = dataSnapshot.getValue(Drivers.class);
//                        String id = drivers.getId();
                        String id = dataSnapshot.getKey();

                        driverRef.child(id).child("name").setValue(_name);
                        driverRef.child(id).child("phn").setValue(_phn);
                        driverRef.child(id).child("bikeRegNo").setValue(_bikeReg);
                        driverRef.child(id).child("nid").setValue(_nid);
                        driverRef.child(id).child("address").setValue(_address);
                        driverRef.child(id).child("gender").setValue(_gender);


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
        });
    }

    private void initComponent(View v) {
        driverRef = FirebaseDatabase.getInstance().getReference("Drivers");
        mAuth = FirebaseAuth.getInstance();
        driver = mAuth.getCurrentUser();

        name = (EditText) v.findViewById(R.id.name_edit);
        email = (EditText) v.findViewById(R.id.email_edit);
        phn = (EditText) v.findViewById(R.id.phn_edit);
        bikeReg = (EditText) v.findViewById(R.id.bike_reg_edit);
        nid = (EditText) v.findViewById(R.id.nid_edit);
        address = (EditText) v.findViewById(R.id.address_edit);

        gender = (Spinner) v.findViewById(R.id.gender_spinner);

        updateBtn = (Button) v.findViewById(R.id.update_btn);
        backBtn =(Button) v.findViewById(R.id.back_btn);

        email.setEnabled(false);
    }

}
