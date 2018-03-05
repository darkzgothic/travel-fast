package com.example.zahidhasan.travelfast.passenger.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.zahidhasan.travelfast.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PassengerHomeFragment extends Fragment {

    private Button reqButton;

    public PassengerHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_passenger_home, container, false);

        reqButton = (Button) v.findViewById(R.id.req_btn);

        reqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                PassengerRequestFragment fragment = new PassengerRequestFragment();
                ft.replace(R.id.activity_passenger_main, fragment, fragment.getTag());
                ft.commit();
            }
        });

        return v;

    }

}
