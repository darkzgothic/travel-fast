package com.example.zahidhasan.travelfast.driver.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zahidhasan.travelfast.R;
import com.example.zahidhasan.travelfast.driver.activity.DriverMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DriverLoginFragment extends Fragment {

    private Button loginButton, backButton;
    private TextView regTxtView;
    private EditText emailEdtTxt, passwordEditTxt;
    private ProgressDialog loginProgress;

    private FirebaseAuth mAuth;

    public DriverLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_driver_login, container, false);

        mAuth = FirebaseAuth.getInstance();
        loginProgress = new ProgressDialog(getContext());

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String email = sp.getString("email", null);
        String password = sp.getString("password", null);

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        Toast.makeText(getContext(), "Please Login", Toast.LENGTH_SHORT).show();
                    }else {
                        startActivity(new Intent(getActivity(), DriverMainActivity.class));
                        getActivity().finish();
                    }
                }
            });
        }

        InitComponent(v);

        return v;
    }

    private void InitComponent(View v) {
        loginButton = (Button) v.findViewById(R.id.login_btn);
        backButton = (Button) v.findViewById(R.id.back_btn);

        regTxtView = (TextView) v.findViewById(R.id.reg_txtview);

        emailEdtTxt = (EditText) v.findViewById(R.id.email_edttxt2);
        passwordEditTxt = (EditText) v.findViewById(R.id.password_edttxt2);



        regTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                DriverRegFragment fragment = new DriverRegFragment();
                ft.replace(android.R.id.content, fragment, fragment.getTag());
                ft.commit();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _email = emailEdtTxt.getText().toString().trim();
                String _password = passwordEditTxt.getText().toString();

                if (TextUtils.isEmpty(_email)){
                    emailEdtTxt.setError("Email can't be empty");
                    return;
                }
                if (TextUtils.isEmpty(_password)){
                    passwordEditTxt.setError("Password can't be empty");
                    return;
                }
                if (!_email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
                    emailEdtTxt.setError("Enter valid email");
                    return;
                }
                if (!_password.matches("^.{8,}$")){
                    passwordEditTxt.setError("Password must contains 8 characters");
                    return;
                }
                loginProgress.setMessage("Please Wait");
                loginProgress.show();

                mAuth.signInWithEmailAndPassword(_email, _password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()){
                            loginProgress.dismiss();
                            Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_LONG).show();
                        }
                        else {
                            String email = emailEdtTxt.getText().toString().trim();
                            String password = passwordEditTxt.getText().toString().trim();

                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences.Editor ed = sp.edit();
                            ed.putString("email", email);
                            ed.putString("password", password);
                            ed.commit();

                            loginProgress.dismiss();

                            Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(getActivity(), DriverMainActivity.class));
                            getActivity().finish();
                        }
                    }
                });


            }
        });
    }

}
