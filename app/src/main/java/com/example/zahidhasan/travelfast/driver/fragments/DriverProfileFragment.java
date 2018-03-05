package com.example.zahidhasan.travelfast.driver.fragments;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.zahidhasan.travelfast.R;
import com.example.zahidhasan.travelfast.model.Drivers;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class DriverProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profileImg, nidImg, bikeRegImg;
    private TextView name, email, phn, bikeRegNum, nidNum, gender, reputationPoint, balance, completedRide, status, address, verification;
    private Spinner spinner;
    private Button proImgUpBtn;

    private DatabaseReference driverRef;
    private FirebaseAuth mAuth;
    private FirebaseUser driver;
    private FirebaseStorage storageReference;
    private StorageReference st;

    private Uri filePath;

    public DriverProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_driver_profile, container, false);

        initComponent(v);

        getDriverData();

        updateProfileImage();

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                profileImg.setImageBitmap(bitmap);
                proImgUpBtn.setText("Save");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getDriverData() {
        String _email = driver.getEmail();

        driverRef.orderByChild("email").equalTo(_email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Drivers driver = dataSnapshot.getValue(Drivers.class);

                name.setText(driver.getName());
                email.setText(driver.getEmail());
                phn.setText(driver.getPhn());
                bikeRegNum.setText(driver.getBikeRegNo());
                nidNum.setText(driver.getNid());
                status.setText(driver.getStatus());
                address.setText(driver.getAddress());
                balance.setText(driver.getBalance());
                completedRide.setText(driver.getCompletedRide());
                reputationPoint.setText(driver.getRepPoint());
                gender.setText(driver.getGender());
                verification.setText(driver.getVerified());

                StorageReference st = storageReference.getReferenceFromUrl(driver.getProImg());
                StorageReference st1 = storageReference.getReferenceFromUrl(driver.getBikeRegImg());
                StorageReference st2 = storageReference.getReferenceFromUrl(driver.getNidImg());

                Glide.with(getContext())
                        .using(new FirebaseImageLoader())
                        .load(st)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(profileImg);

                Glide.with(getContext())
                        .using(new FirebaseImageLoader())
                        .load(st1)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(bikeRegImg);

                Glide.with(getContext())
                        .using(new FirebaseImageLoader())
                        .load(st2)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(nidImg);
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

    private void updateProfileImage() {
        proImgUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = proImgUpBtn.getText().toString();

                if (str.equals("Upload")){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                }
                if (str.equals("Save")){

                    String _email = driver.getEmail();
                    String[] _folderName = _email.split("@");

                    final ProgressDialog progress = new ProgressDialog(getContext());
                    progress.setTitle("Uploading");
                    progress.show();

                    StorageReference storage = st.child("Drivers"+"/"+_folderName[0]+"/"
                            + _folderName[0] +"-"+"proImg"+ "." + getFileExtension(filePath));

                    storage.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            driverRef.orderByChild("email").equalTo(driver.getEmail()).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    String id = dataSnapshot.getKey().toString();
                                    String url = taskSnapshot.getDownloadUrl().toString();
                                    driverRef.child(id).child("proImg").setValue(url);
                                    Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                                    progress.dismiss();
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
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progress.dismiss();
                            Toast.makeText(getContext(), "Upload Not Successful", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double _progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progress.setMessage("Profile Image Uploaded " + ((int) _progress) + "%...");
                        }
                    });
                }


            }
        });
    }

    private void initComponent(View v) {
        driverRef = FirebaseDatabase.getInstance().getReference("Drivers");
        mAuth = FirebaseAuth.getInstance();
        driver = mAuth.getCurrentUser();

        storageReference = FirebaseStorage.getInstance();
        st = FirebaseStorage.getInstance().getReference();

        profileImg = (ImageView) v.findViewById(R.id.profile_image);
        nidImg = (ImageView) v.findViewById(R.id.nid_image);
        bikeRegImg = (ImageView) v.findViewById(R.id.bike_image);

        spinner = (Spinner) v.findViewById(R.id.menu_spinner);
        proImgUpBtn = (Button) v.findViewById(R.id.pro_img_up_btn);

        name = (TextView) v.findViewById(R.id.name_txtview);
        email = (TextView) v.findViewById(R.id.email_txtview);
        phn = (TextView) v.findViewById(R.id.phn_txtvw);
        bikeRegNum = (TextView) v.findViewById(R.id.bike_txtvw);
        nidNum = (TextView) v.findViewById(R.id.nid_txtvw);
        gender = (TextView) v.findViewById(R.id.gender_txtvw);
        reputationPoint = (TextView) v.findViewById(R.id.rep_txtvw);
        balance = (TextView) v.findViewById(R.id.balance_txtvw);
        completedRide = (TextView) v.findViewById(R.id.com_ride_txtvw);
        status = (TextView) v.findViewById(R.id.status_txtvw);
        address = (TextView) v.findViewById(R.id.add_txtvw);
        verification = (TextView) v.findViewById(R.id.verification_txtvw);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = spinner.getSelectedItem().toString();
                if (str.equals("Update Profile")){
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    DriverUpdateProfileFragment fragment = new DriverUpdateProfileFragment();
                    ft.replace(R.id.content_driver_main, fragment, fragment.getTag());
                    ft.commit();
                }
                if (str.equals("Change Password")){
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    ChangePasswordFragment fragment = new ChangePasswordFragment();
                    ft.replace(R.id.content_driver_main, fragment, fragment.getTag());
                    ft.commit();
                }
                if (str.equals("Change National ID")){
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    ChangeNIDFragment fragment = new ChangeNIDFragment();
                    ft.replace(R.id.content_driver_main, fragment, fragment.getTag());
                    ft.commit();
                }
                if (str.equals("Change Bike Reg No")){
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                    ChangeBikeRegNoFragment fragment = new ChangeBikeRegNoFragment();
                    ft.replace(R.id.content_driver_main, fragment, fragment.getTag());
                    ft.commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}
