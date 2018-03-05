package com.example.zahidhasan.travelfast.driver.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zahidhasan.travelfast.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordFragment extends Fragment {


    private EditText email;
    private Button sendBtn;
    private TextView status;

    private FirebaseAuth mAuth;
    private FirebaseUser driver;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_change_password, container, false);

        email = (EditText) v.findViewById(R.id.change_pass_edittxt);
        sendBtn = (Button) v.findViewById(R.id.pass_chng_btn);
        status = (TextView) v.findViewById(R.id.status_txtvw);

        mAuth = FirebaseAuth.getInstance();
        driver = mAuth.getCurrentUser();

        email.setText(driver.getEmail());
        email.setEnabled(false);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _email = driver.getEmail();
                mAuth.sendPasswordResetEmail("zahidhasan065@gmail.com");
                Toast.makeText(getContext(), "Request Send", Toast.LENGTH_SHORT).show();
                status.setText("Please Check Email For Next Step");
            }
        });
        return v;
    }

}
