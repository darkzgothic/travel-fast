package com.example.zahidhasan.travelfast;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.zahidhasan.travelfast.driver.fragments.DriverLoginFragment;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        DriverLoginFragment fragment = new DriverLoginFragment();
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(android.R.id.content, fragment, fragment.getTag());
        tx.commit();
    }
}
