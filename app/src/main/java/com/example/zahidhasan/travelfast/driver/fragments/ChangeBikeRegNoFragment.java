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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeBikeRegNoFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView bikeImage;
    private Button button;

    private StorageReference storageReference;
    private FirebaseStorage storage;
    private DatabaseReference driverRef;
    private FirebaseAuth mAuth;
    private FirebaseUser driver;

    private Uri filePath;


    public ChangeBikeRegNoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_change_bike_reg_no, container, false);

        initComponent(v);

        getBikeRegPaperImage();

        uploadImage();

        return v;
    }
    private void uploadImage() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = button.getText().toString().trim();
                if (txt.equals("Browse Image")){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                }
                if (txt.equals("Save Image")){

                    if (filePath != null){
                        final ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setTitle("Uploading");
                        progressDialog.show();

                        String email = driver.getEmail();
                        String[] _folderName = email.split("@");

                        StorageReference bikeImgRef = storageReference.child("Drivers"+"/"+_folderName[0]+"/"
                                + _folderName[0] +"-"+"bikeRegImg"+ "." + getFileExtension(filePath));

                        bikeImgRef.putFile(filePath)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                        driverRef.orderByChild("email").equalTo(driver.getEmail()).addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                String id = dataSnapshot.getKey().toString();
                                                String url = taskSnapshot.getDownloadUrl().toString();
                                                driverRef.child(id).child("bikeRegImg").setValue(url);

                                                button.setText("Browse Image");
                                                progressDialog.dismiss();
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
                                        Toast.makeText(getActivity(), "Bike Reg Paper Image Uploaded ", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(), "Upload Not Successful", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                //displaying the upload progress
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                progressDialog.setMessage("Image Uploaded " + ((int) progress) + "%...");
                            }
                        });
                    }
                }
            }
        });
    }

    private void getBikeRegPaperImage() {
        String _email = driver.getEmail();
        driverRef.orderByChild("email").equalTo(_email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Drivers drivers = dataSnapshot.getValue(Drivers.class);

                StorageReference st = storage.getReferenceFromUrl(drivers.getBikeRegImg());

                Glide.with(getContext())
                        .using(new FirebaseImageLoader())
                        .load(st)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(bikeImage);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                filePath = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                    bikeImage.setImageBitmap(bitmap);
                    button.setText("Save Image");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initComponent(View v) {
        bikeImage = (ImageView) v.findViewById(R.id.bike_img);
        button = (Button) v.findViewById(R.id.bike_img_btn);

        storageReference = FirebaseStorage.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        driverRef = FirebaseDatabase.getInstance().getReference("Drivers");
        mAuth = FirebaseAuth.getInstance();
        driver = mAuth.getCurrentUser();
    }
    public String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}
