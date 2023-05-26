package com.example.palliativecareapppatients;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreenActivity extends AppCompatActivity {
    private static final long SPLASH_SCREEN_DELAY = 1000; // تأخير سبلاش سكرين بمدة 2 ثانية
    private FirebaseAuth mAuth;
    private SharedPreferences mSharedPreferences;
    private boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                redirectToMainActivity();
            }
        }, SPLASH_SCREEN_DELAY);
        mAuth = FirebaseAuth.getInstance();
        mSharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        isLoggedIn = mSharedPreferences.getBoolean("loggedIn", false);

        if (isLoggedIn) {
            redirectToMainActivity();
            finish();
        }
        if (!isLoggedIn) {
            redirectToLoginActivity();
            finish();
        }

    }

    private void redirectToLoginActivity() {
        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    private void redirectToMainActivity() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String userType = dataSnapshot.child("type").getValue(String.class);
                        Class<?> targetActivity = userType.equals("doctor") ? MainActivity2.class : MainActivity.class;
                        Intent intent = new Intent(SplashScreenActivity.this, targetActivity);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("LoginActivity", "Failed to retrieve user data: " + databaseError.getMessage());

                }
            });
        }
    }

    }

