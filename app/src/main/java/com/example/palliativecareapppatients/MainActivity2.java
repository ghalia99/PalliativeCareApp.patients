package com.example.palliativecareapppatients;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        // Set the initial fragment
       DoctorTopicList topicListFragment = new DoctorTopicList();
        setFragment(topicListFragment);

        // Set the listeners for the bottom navigation bar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.navigation_Doctor_topic:
                        fragment = new DoctorTopicList();
                        setFragment(fragment);
                        return true;
                    case R.id.navigation_home:
                        fragment = new DoctorHome();
                        setFragment(fragment);
                        return true;
                    case R.id.navigation_profile:
                        fragment = new DoctorProfile();
                        setFragment(fragment);
                        return true;
                    default:
                        return false;
                }
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            updateUserStatus("online");
            VerifyUserexistance();

        }

    }

    private void updateUserStatus(String state) {
        String savecurrentTime, savecurrentDate;
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        savecurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        savecurrentTime = currentTime.format(calendar.getTime());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("time", savecurrentTime);
        hashMap.put("date", savecurrentDate);
        hashMap.put("state", state);

        String    currentUserId = firebaseAuth.getCurrentUser().getUid();
        databaseReference.child("users").child(currentUserId).child("userState").updateChildren(hashMap);

    }
    private void VerifyUserexistance() {
        String currentUserID=firebaseAuth.getCurrentUser().getUid();
        databaseReference.child("users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("firstName") && dataSnapshot.hasChild("middleName") && dataSnapshot.hasChild("familyName")) {

                    Toast.makeText(MainActivity2.this,"Welcome",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    startActivity(new Intent(MainActivity2.this, SettingsActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            startActivity(new Intent(MainActivity2.this, SearchActivity.class));
            return true;
        } else if (id == R.id.action_chat) {
            startActivity(new Intent(MainActivity2.this, ChatActivity.class));
            return true;
        } else if (id == R.id.main_find_friends_menu) {
            startActivity(new Intent(MainActivity2.this, FindFriendsActivity.class));
            return true;
        } else if (id == R.id.main_settings_menu) {
            startActivity(new Intent(MainActivity2.this, SettingsActivity.class));
            return true;
        } else if (id == R.id.main_logout_menu) {
            updateUserStatus("offline");
            firebaseAuth.signOut();
            startActivity(new Intent(MainActivity2.this, LoginActivity.class));
            Toast.makeText(MainActivity2.this, "User logged out successfully...", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}