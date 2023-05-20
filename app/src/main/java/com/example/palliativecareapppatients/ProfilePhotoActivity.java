package com.example.palliativecareapppatients;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfilePhotoActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profileImageView;
    private Button choosePhotoButton, savePhotoButton;
    private String userType;
    private Uri imageUri;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_photo);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        Intent intent = getIntent();
        if (intent.hasExtra("userType")) {
            userType = intent.getStringExtra("userType");
        }

        profileImageView = findViewById(R.id.image_view);
        choosePhotoButton = findViewById(R.id.btn_choose_photo);
        savePhotoButton = findViewById(R.id.btn_upload_photo);

        choosePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        savePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfilePhoto();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void saveProfilePhoto() {
        if (imageUri != null) {
            String uid = mAuth.getCurrentUser().getUid();
            StorageReference fileReference = mStorage.child("profile_photos").child(uid + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get the download URL of the uploaded image
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Save the download URL in the user's profile
                                    mDatabase.child("users").child(uid).child("profilePhotoUrl")
                                            .setValue(uri.toString())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Profile photo saved successfully
                                                    navigateToMainActivity();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    showToast("Failed to save profile photo");
                                                }
                                            });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast("Failed to upload profile photo");
                        }
                    });
        } else {
            showToast("No image selected");
        }
    }

    private void navigateToMainActivity() {
        if (userType != null) {
            Class<?> targetActivity;
            switch (userType) {
                case "patient":
                    targetActivity = MainActivity.class;
                    break;
                case "doctor":
                    targetActivity = MainActivity2.class;
                    break;
                default:
                    showToast("Invalid user type");
                    return;
            }

            Intent intent = new Intent(ProfilePhotoActivity.this, targetActivity);
            startActivity(intent);
            finish();
        } else {
            showToast("User node does not exist in the database");
        }

    }



    private void showToast(String message) {
        Toast.makeText(ProfilePhotoActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
        }
    }
}
