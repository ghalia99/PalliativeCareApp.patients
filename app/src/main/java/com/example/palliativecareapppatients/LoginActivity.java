package com.example.palliativecareapppatients;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
    private FirebaseAuth mAuth;
    private SharedPreferences mSharedPreferences;
    private boolean isLoggedIn;

    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEt = findViewById(R.id.email_et);
        passwordEt = findViewById(R.id.password_et);
        loginBtn = findViewById(R.id.login_btn);
        registerBtn = findViewById(R.id.register_btn);

        mAuth = FirebaseAuth.getInstance();
        mSharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        isLoggedIn = mSharedPreferences.getBoolean("loggedIn", false);

        if (isLoggedIn) {
            redirectToMainActivity();
            finish();
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                                    editor.putBoolean("loggedIn", true);
                                    editor.apply();

                                    redirectToMainActivity();
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Failed to log in. Please check your credentials and try again.", Toast.LENGTH_SHORT).show();
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
    private boolean isDataLoaded = false;

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
                        Intent intent = new Intent(LoginActivity.this, targetActivity);
                        startActivity(intent);
                    }
                    isDataLoaded = true; // تم تحميل البيانات بنجاح
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("LoginActivity", "Failed to retrieve user data: " + databaseError.getMessage());
                    isDataLoaded = true; // حدث خطأ في تحميل البيانات
                }
            });
        } else {
            isDataLoaded = true; // لا يوجد مستخدم حالي
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isDataLoaded) {
            // تعيين شاشة تحميل أو إجراء آخر هنا حتى يتم تحميل البيانات
        }
    }



}
