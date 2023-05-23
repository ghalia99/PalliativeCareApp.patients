package com.example.palliativecareapppatients;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Profile extends Fragment {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    private ImageView profileImage;
    private TextView profileFirstName;
    private TextView profileMiddleName;
    private TextView profileFamilyName;
    private TextView profileEmail;
    private Button editProfileButton;
    private AlertDialog alertDialog; // Declare the variable here
    private Uri selectedImageUri;

    // ...

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        profileImage = view.findViewById(R.id.profileImage);
        profileFirstName = view.findViewById(R.id.profileFirstName);
        profileEmail = view.findViewById(R.id.profileEmail);
        editProfileButton = view.findViewById(R.id.editProfileButton);

        // Set click listener for edit profile button
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });

        // Load user data from the database
        loadUserData();

        return view;
    }

    private void loadUserData() {
        // Get the current user ID
        String userId = mAuth.getCurrentUser().getUid();

        // Get the user data from the database
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get user data
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    String middleName = dataSnapshot.child("middleName").getValue(String.class);
                    String familyName = dataSnapshot.child("familyName").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String profilePhotoUrl = dataSnapshot.child("profilePhotoUrl").getValue(String.class);

                    // Update the UI with user data
                    profileFirstName.setText(firstName+" "+middleName+" "+familyName);

                    profileEmail.setText(email);

                    // Load profile photo using an image loading library like Glide or Picasso
                    Glide.with(requireContext())
                            .load(profilePhotoUrl)
                            .placeholder(R.drawable.profile_image)
                            .into(profileImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
                Log.e("TAG", "Failed to load user data.", databaseError.toException());
            }
        });
    }
    private void showEditProfileDialog() {
        // Create a dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_profile, null);
        dialogBuilder.setView(dialogView);

        // Initialize dialog views
        ImageView dialogProfileImage = dialogView.findViewById(R.id.dialogProfileImage);
        EditText dialogFirstName = dialogView.findViewById(R.id.dialogFirstName);
        EditText dialogMiddleName = dialogView.findViewById(R.id.dialogMiddleName);
        EditText dialogFamilyName = dialogView.findViewById(R.id.dialogFamilyName);
        EditText dialogEmail = dialogView.findViewById(R.id.dialogEmail);
        Button saveButton = dialogView.findViewById(R.id.saveButton);
        String[] nameParts = profileFirstName.getText().toString().split(" ");

        String firstName = "";
        String middleName = "";
        String familyName = "";

        if (nameParts.length > 0) {
            firstName = nameParts[0];
            dialogFirstName.setText(firstName);

        }

        if (nameParts.length > 1) {
            middleName = nameParts[1];
            dialogMiddleName.setText(middleName);

        }

        if (nameParts.length > 2) {
            familyName = nameParts[2];
            dialogFamilyName.setText(familyName);

        }
        String currentEmail = profileEmail.getText().toString();
        dialogEmail.setText(currentEmail);

        // Set click listener for profile image to open image picker
        dialogProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        // Set click listener for save button to update user data
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newFirstName = dialogFirstName.getText().toString();
                String newMiddleName = dialogMiddleName.getText().toString();
                String newFamilyName = dialogFamilyName.getText().toString();
                String newEmail = dialogEmail.getText().toString();

                // Update user data in the database
                updateUserData(newFirstName, newMiddleName, newFamilyName, newEmail);

                // Update the profile image if a new image is selected
                if (selectedImageUri != null) {
                    uploadProfileImage(selectedImageUri);
                }

                // Dismiss the dialog
                alertDialog.dismiss();
            }
        });

        // Show the dialog
        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void updateUserData(String firstName, String middleName, String familyName, String email) {
        // Get the current user ID
        String userId = mAuth.getCurrentUser().getUid();

        // Update user data in the database
        mDatabase.child("users").child(userId).child("firstName").setValue(firstName);
        mDatabase.child("users").child(userId).child("middleName").setValue(middleName);
        mDatabase.child("users").child(userId).child("familyName").setValue(familyName);
        mDatabase.child("users").child(userId).child("email").setValue(email);

        // Update the UI with the new user data
        profileFirstName.setText(firstName+" "+middleName+" "+familyName);

        profileEmail.setText(email);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Get the image URI from the intent data
            selectedImageUri = data.getData();

            // Load the selected image into the profile image view
            Glide.with(requireContext())
                    .load(selectedImageUri)
                    .placeholder(R.drawable.profile_image)
                    .into(profileImage);
        }
    }
    private void uploadProfileImage(Uri imageUri) {
        // Get the current user ID
        String userId = mAuth.getCurrentUser().getUid();

        // Create a storage reference for the user's profile image
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Image Files").child(userId + ".jpg");

        // Upload the image to Firebase Storage
        storageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get the download URL of the uploaded image
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                // Update the user's profile image URL in the database
                                mDatabase.child("users").child(userId).child("profilePhotoUrl").setValue(downloadUri.toString());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the upload failure
                        Toast.makeText(requireContext(), "Failed to upload profile image", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
