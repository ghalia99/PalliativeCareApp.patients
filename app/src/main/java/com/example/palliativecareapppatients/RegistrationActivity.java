package com.example.palliativecareapppatients;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {
    private EditText firstNameEt, middleNameEt, familyNameEt, birthDateEt, addressEt, emailEt, mobileNoEt, passwordEt, confirmPasswordEt;
    private Button registerBtn;
    private CheckBox patientCheckBox, doctorCheckBox;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button loginBtn ;
    private String userType ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 /*       SharedPreferences prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        boolean loggedIn = prefs.getBoolean("loggedIn", false);
        if (loggedIn) {
            // if the user is already logged in, start the home activity
            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
*/

        setContentView(R.layout.activity_registration);
        firstNameEt = findViewById(R.id.first_name_et);
        middleNameEt = findViewById(R.id.middle_name_et);
        familyNameEt = findViewById(R.id.family_name_et);
        birthDateEt = findViewById(R.id.birth_date_et);
        addressEt = findViewById(R.id.address_et);
        emailEt = findViewById(R.id.email_et);
        mobileNoEt = findViewById(R.id.mobile_no_et);
        passwordEt = findViewById(R.id.password_et);
        confirmPasswordEt = findViewById(R.id.confirm_password_et);
        registerBtn = findViewById(R.id.register_btn);
        loginBtn = findViewById(R.id.sign_in_btn);

        patientCheckBox = findViewById(R.id.patient_checkbox);
        doctorCheckBox = findViewById(R.id.doctor_checkbox);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String firstName = firstNameEt.getText().toString().trim();
                final String middleName = middleNameEt.getText().toString().trim();
                final String familyName = familyNameEt.getText().toString().trim();
                final String birthDate = birthDateEt.getText().toString().trim();
                final String address = addressEt.getText().toString().trim();
                final String email = emailEt.getText().toString().trim();
                final String mobileNo = mobileNoEt.getText().toString().trim();
                final String password = passwordEt.getText().toString().trim();
                String confirmPassword = confirmPasswordEt.getText().toString().trim();
                if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(middleName) || TextUtils.isEmpty(familyName)
                        || TextUtils.isEmpty(birthDate) || TextUtils.isEmpty(address) || TextUtils.isEmpty(email)
                        || TextUtils.isEmpty(mobileNo) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
                    return;
                }
                if (patientCheckBox.isChecked() && doctorCheckBox.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Please select only one user type", Toast.LENGTH_LONG).show();
                    return;
                }

                if (patientCheckBox.isChecked()) {
                    userType = "patient";
                } else if (doctorCheckBox.isChecked()) {
                    userType = "doctor";
                } else {
                    Toast.makeText(getApplicationContext(), "Please select user type", Toast.LENGTH_LONG).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String uid = mAuth.getCurrentUser().getUid();

                                    HashMap<String, String> userMap = new HashMap<>();
                                    userMap.put("firstName", firstName);
                                    userMap.put("middleName", middleName);
                                    userMap.put("familyName", familyName);
                                    userMap.put("birthDate", birthDate);
                                    userMap.put("address", address);
                                    userMap.put("email", email);
                                    userMap.put("mobileNo", mobileNo);
                                    userMap.put("type", userType);


                                    mDatabase.child("users").child(uid).setValue(userMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();
                                                        Intent intent = new Intent(RegistrationActivity.this, ProfilePhotoActivity.class);
                                                        intent.putExtra("userType", userType);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                }
            });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }
}
