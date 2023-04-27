package com.example.palliativecareapppatients;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEt, passwordEt;
    private Button loginBtn, registerBtn;
    private SharedPreferences mSharedPreferences;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the loggedIn value to true
        SharedPreferences prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("isFirstRun", true);
        if (isFirstRun) {
            // if this is the first run of the app, set the loggedIn value to false
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("loggedIn", false);
            editor.putBoolean("isFirstRun", false);
            editor.apply();

        } else {

            setContentView(R.layout.activity_login);


            emailEt = findViewById(R.id.email_et);
            passwordEt = findViewById(R.id.password_et);
            loginBtn = findViewById(R.id.login_btn);
            registerBtn = findViewById(R.id.register_btn);

            mAuth = FirebaseAuth.getInstance();

            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                String userType = currentUser.getMetadata().getCreationTimestamp() == currentUser.getMetadata().getLastSignInTimestamp()
                        ? currentUser.getDisplayName()
                        : currentUser.getMetadata().getCreationTimestamp() + "";
                if (userType.equals("doctor")) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
                    startActivity(intent);
                    finish();
                } else if (userType.equals("patient")) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

    loginBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = emailEt.getText().toString().trim();
                        String password = passwordEt.getText().toString().trim();

                        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                            Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_LONG).show();
                            return;
                        }

                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
                                            FirebaseUser currentUser = mAuth.getCurrentUser();
                                            if (currentUser != null) {
                                                String uid = currentUser.getUid();
                                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            String userType = dataSnapshot.child("type").getValue(String.class);
                                                            if (userType != null) {
                                                                if (userType.equals("doctor")) {
                                                                    Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
                                                                    startActivity(intent);
                                                                } else if (userType.equals("patient")) {
                                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                                    startActivity(intent);
                                                                }
                                                                finish();
                                                            } else {
                                                                Toast.makeText(getApplicationContext(), "Failed to get user type", Toast.LENGTH_LONG).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "Failed to get user data", Toast.LENGTH_LONG).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        Toast.makeText(getApplicationContext(), "Failed to get user data", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                });




            registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }
}
}
