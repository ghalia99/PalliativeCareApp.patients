package com.example.palliativecareapppatients;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView searchIcon, chatIcon;
    private BottomNavigationView bottomNavigationView;
    private Fragment currentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        searchIcon = findViewById(R.id.search_icon);
        chatIcon = findViewById(R.id.chat_icon);
        bottomNavigationView = findViewById(R.id.bottom_navigation);





        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new TopicList()).commit();
        currentFragment = new TopicList();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_topic:
                    selectedFragment = new TopicList();
                    break;
                case R.id.navigation_profile:
                    selectedFragment = new Profile();
                    break;
                case R.id.navigation_settings:
                    selectedFragment = new Setting();
                    break;
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, selectedFragment).commit();
                currentFragment = selectedFragment;
            }

            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        } else if (id == R.id.action_chat) {
            startActivity(new Intent(this, ChatActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
