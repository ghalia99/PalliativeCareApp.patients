package com.example.palliativecareapppatients;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity2 extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

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
            startActivity(new Intent(MainActivity2.this,SearchActivity.class));
            return true;
        }
        if (id == R.id.action_chat) {
            startActivity(new Intent(MainActivity2.this,ChatActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}