package com.example.zahidhasan.travelfast.driver.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zahidhasan.travelfast.R;
import com.example.zahidhasan.travelfast.uploadfile.Constants;
import com.example.zahidhasan.travelfast.uploadfile.Upload;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import static com.example.zahidhasan.travelfast.R.id.imageView;

public class VerificationActivity extends AppCompatActivity {

    //constant to track image chooser intent
    private static final int PICK_IMAGE_REQUEST_ONE = 1;
    private static final int PICK_IMAGE_REQUEST_TWO = 2;
    private static final int PICK_IMAGE_REQUEST_THREE = 3;

    //objects
    private Button profileUploadBtn, bikeRegUploadBtn, nidUploadBtn, submitBtn;
    private ImageView profileImageView, bikeRegImageView, nidImageView;
    private EditText bikeEditText, nidEditText;

    //uri to store file
    private Uri filePath1;
    private Uri filePath2;
    private Uri filePath3;


    //firebase
    private StorageReference storageReference;
    private DatabaseReference driverRef;
    private FirebaseAuth mAuth;
    private FirebaseUser driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        driver = mAuth.getCurrentUser();
        driverRef = FirebaseDatabase.getInstance().getReference("Drivers");

        profileUploadBtn = (Button) findViewById(R.id.profile_upld_btn);
        bikeRegUploadBtn = (Button) findViewById(R.id.bike_upld_btn);
        nidUploadBtn = (Button) findViewById(R.id.nid_upld_btn);
        submitBtn = (Button) findViewById(R.id.submit_btn);

        profileImageView = (ImageView) findViewById(R.id.profile_image_view);
        bikeRegImageView = (ImageView) findViewById(R.id.bike_paper_image_view);
        nidImageView = (ImageView) findViewById(R.id.nid_image_view);

        bikeEditText = (EditText) findViewById(R.id.bike_editText);
        nidEditText = (EditText) findViewById(R.id.nid_editText);

//        StorageReference st = storageReference.child("Drivers/anto/anto-proImg");
//        Glide.with(this)
//                .using(new FirebaseImageLoader())
//                .load(st)
//                .into(profileImageView);

        profileUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_ONE);
            }
        });
        bikeRegUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_TWO);
            }
        });
        nidUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_THREE);
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });
    }
    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadFile() {
        if (filePath1 != null && filePath2 != null && filePath3 != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            String email = driver.getEmail();
            String[] _folderName = email.split("@");

            StorageReference proImgRef = storageReference.child("Drivers"+"/"+_folderName[0]+"/"
                    + _folderName[0] +"-"+"proImg"+ "." + getFileExtension(filePath1));
            StorageReference bikeRegImgRef = storageReference.child("Drivers"+"/"+_folderName[0]+"/"
                    + _folderName[0] +"-"+"bikeRegImg"+ "." + getFileExtension(filePath1));
            StorageReference nidImgRef = storageReference.child("Drivers"+"/"+_folderName[0]+"/"
                    + _folderName[0] +"-"+"nidImg"+ "." + getFileExtension(filePath1));

            proImgRef.putFile(filePath1)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            driverRef.orderByChild("email").equalTo(driver.getEmail()).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    String id = dataSnapshot.getKey().toString();
                                    String url = taskSnapshot.getDownloadUrl().toString();
                                    driverRef.child(id).child("proImg").setValue(url);
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
                            Toast.makeText(getApplicationContext(), "Profile Image Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Upload Not Successful", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Profile Image Uploaded " + ((int) progress) + "%...");
                        }
                    });
            bikeRegImgRef.putFile(filePath2)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            driverRef.orderByChild("email").equalTo(driver.getEmail()).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    String id = dataSnapshot.getKey().toString();
                                    String url = taskSnapshot.getDownloadUrl().toString();

                                    driverRef.child(id).child("bikeRegImg").setValue(url);
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
                            Toast.makeText(getApplicationContext(), "Bike Registration Image Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Upload Not Successful", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Bike Registration Image Uploaded " + ((int) progress) + "%...");
                        }
                    });
            nidImgRef.putFile(filePath3)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            driverRef.orderByChild("email").equalTo(driver.getEmail()).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    String id = dataSnapshot.getKey().toString();
                                    String url = taskSnapshot.getDownloadUrl().toString();

                                    driverRef.child(id).child("nidImg").setValue(url);
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
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "NID Image Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Upload Not Successful", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("NID Image Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (requestCode == PICK_IMAGE_REQUEST_ONE){
                filePath1 = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath1);
                    profileImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == PICK_IMAGE_REQUEST_TWO){
                filePath2 = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath2);
                    bikeRegImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == PICK_IMAGE_REQUEST_THREE){
                filePath3 = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath3);
                    nidImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
