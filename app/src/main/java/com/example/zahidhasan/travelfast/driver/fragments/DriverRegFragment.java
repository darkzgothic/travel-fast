package com.example.zahidhasan.travelfast.driver.fragments;


import android.app.ProgressDialog;
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
import com.example.zahidhasan.travelfast.model.Drivers;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DriverRegFragment extends Fragment {

    private Button backButton, regButton;
    private TextView loginTxtView;
    private EditText nameEdtTxt, emailEdtTxt, passwordEdtTxt, cpasswordEdtTxt;
    private ProgressDialog signUpProgress;

    private DatabaseReference driverRef;
    private FirebaseAuth mAuth;

    public DriverRegFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_driver_reg, container, false);

        InitComponent(v);

        PerformClickEvent();

        return v;
    }

    private void PerformClickEvent() {
        loginTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                DriverLoginFragment fragment = new DriverLoginFragment();
                ft.replace(android.R.id.content, fragment, fragment.getTag());
                ft.commit();
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DriverRegistration();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                DriverLoginFragment fragment = new DriverLoginFragment();
                ft.replace(android.R.id.content, fragment, fragment.getTag());
                ft.commit();
            }
        });
    }

    private void DriverRegistration() {
        String _name = nameEdtTxt.getText().toString().trim();
        String _email = emailEdtTxt.getText().toString().trim();
        String _password = passwordEdtTxt.getText().toString().trim();
        String _cpassword = cpasswordEdtTxt.getText().toString().trim();

        if (TextUtils.isEmpty(_name)){
            nameEdtTxt.setError("Name can't be empty");
            return;
        }
        if (TextUtils.isEmpty(_email)){
            emailEdtTxt.setError("Email can't be empty");
            return;
        }
        if (TextUtils.isEmpty(_password)){
            passwordEdtTxt.setError("Password can't be empty");
            return;
        }
        if (TextUtils.isEmpty(_cpassword)){
            cpasswordEdtTxt.setError("Confirm password can't be empty");
            return;
        }
        if (!_email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
            emailEdtTxt.setError("Enter valid email");
            return;
        }
        if (!_password.matches("^.{8,}$")){
            passwordEdtTxt.setError("Password must contains 8 characters");
            return;
        }
        if(!_password.equals(_cpassword)){
            cpasswordEdtTxt.setError("Password not matched");
            return;
        }
        signUpProgress.setMessage("Please Wait");
        signUpProgress.show();
        mAuth.createUserWithEmailAndPassword(_email, _password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String __name = nameEdtTxt.getText().toString().trim();
                        String __email = emailEdtTxt.getText().toString().trim();
                        String __password = passwordEdtTxt.getText().toString().trim();

                        if(!task.isSuccessful()){
                            signUpProgress.dismiss();
                            Toast.makeText(getContext(), "Sign Up Error", Toast.LENGTH_LONG).show();
                            emailEdtTxt.setError("Email already registered OR Wrong password");
                        }
                        else {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String id = user.getUid();

                            Drivers driver = new Drivers(id, __name, __email, "null", "null", "null", "null", "0", "0", "null",
                                    "1", "1", "null", "10", "false", "null", "null", "null");
                            driverRef.child(id).setValue(driver);

                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences.Editor ed = sp.edit();
                            ed.putString("email", __email);
                            ed.putString("password", __password);
                            ed.commit();

                            signUpProgress.dismiss();

                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                            DriverLoginFragment fragment = new DriverLoginFragment();
                            ft.replace(android.R.id.content, fragment, fragment.getTag());
                            ft.commit();

                            Toast.makeText(getContext(), "Sign Up Successful", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void InitComponent(View v) {
        driverRef = FirebaseDatabase.getInstance().getReference("Drivers");
        mAuth = FirebaseAuth.getInstance();

        backButton = (Button) v.findViewById(R.id.back_btn);
        regButton = (Button) v.findViewById(R.id.reg_btn);
        loginTxtView = (TextView) v.findViewById(R.id.login_txtview);
        nameEdtTxt = (EditText) v.findViewById(R.id.name_edttxt);
        emailEdtTxt = (EditText) v.findViewById(R.id.email_edttxt);
        passwordEdtTxt = (EditText) v.findViewById(R.id.password_edttxt);
        cpasswordEdtTxt = (EditText) v.findViewById(R.id.cpassword_edttxt);

        signUpProgress = new ProgressDialog(getContext());
    }

}
