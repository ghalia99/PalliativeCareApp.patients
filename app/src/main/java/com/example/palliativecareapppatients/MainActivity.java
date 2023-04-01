package com.example.palliativecareapppatients;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.palliativecareapppatients.ChatActivity;
import com.example.palliativecareapppatients.Profile;
import com.example.palliativecareapppatients.R;
import com.example.palliativecareapppatients.SearchActivity;
import com.example.palliativecareapppatients.Setting;
import com.example.palliativecareapppatients.TopicList;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set the initial fragment
        TopicList topicListFragment = new TopicList();
        setFragment(topicListFragment);

        // Set the listeners for the bottom navigation bar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.navigation_topic:
                        fragment = new TopicList();
                        setFragment(fragment);
                        return true;
                    case R.id.navigation_profile:
                        fragment = new Profile();
                        setFragment(fragment);
                        return true;
                    case R.id.navigation_settings:
                        fragment = new Setting();
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
        fragmentTransaction.replace(R.id.content_frame, fragment);
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
startActivity(new Intent(MainActivity.this,SearchActivity.class));
return true;
        }
        if (id == R.id.action_chat) {
            startActivity(new Intent(MainActivity.this,ChatActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
