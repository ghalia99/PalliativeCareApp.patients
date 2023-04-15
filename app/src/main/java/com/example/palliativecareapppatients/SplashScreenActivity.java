package com.example.palliativecareapppatients;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Check if user is logged in
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // Retrieve user type
            String userType = sharedPreferences.getString("type", "");

            // Start appropriate activity
            if (userType.equals("patient")) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else if (userType.equals("doctor")) {
                Intent intent = new Intent(this, MainActivity2.class);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        // Finish the splash screen activity
        finish();
    }
}
