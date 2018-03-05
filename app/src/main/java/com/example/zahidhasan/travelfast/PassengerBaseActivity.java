package com.example.zahidhasan.travelfast;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.zahidhasan.travelfast.passenger.fragments.PassengerPhnFragment;

public class PassengerBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_base);

        PassengerPhnFragment fragment = new PassengerPhnFragment();
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(android.R.id.content, fragment, fragment.getTag());
        tx.commit();
    }
}
