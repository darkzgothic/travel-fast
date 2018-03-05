package com.example.zahidhasan.travelfast.passenger.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.zahidhasan.travelfast.R;
import com.example.zahidhasan.travelfast.passenger.fragments.PassengerHomeFragment;
import com.example.zahidhasan.travelfast.passenger.services.PassengerLocationService;

public class PassengerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_main);

        startService(new Intent(this, PassengerLocationService.class));

        PassengerHomeFragment fragment = new PassengerHomeFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.activity_passenger_main, fragment, fragment.getTag());
        ft.commit();
    }
}
