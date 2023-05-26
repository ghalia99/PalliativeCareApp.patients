package com.example.palliativecareapppatients;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
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

public class ChatActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager myviewPager;
    private TabsAccessorAdapter mytabsAccessorAdapter;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String currentUserId;
    private TabLayout mytabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();



        myviewPager=findViewById(R.id.main_tabs_pager);
        mytabsAccessorAdapter=new TabsAccessorAdapter(getSupportFragmentManager());
        myviewPager.setAdapter(mytabsAccessorAdapter);

        mytabLayout=findViewById(R.id.main_tabs);
        mytabLayout.setupWithViewPager(myviewPager);
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=firebaseAuth.getCurrentUser();
        if(currentUser==null)
        {
            sendUserToLoginActivity();
        }
        else
        {
         updateUserStatus("online");
            VerifyUserexistance();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser currentUser=firebaseAuth.getCurrentUser();
        if(currentUser!=null)
        {
          updateUserStatus("offline");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseUser currentUser=firebaseAuth.getCurrentUser();
        if(currentUser!=null)
        {
          updateUserStatus("offline");
        }
    }

    private void VerifyUserexistance() {
        String currentUserID=firebaseAuth.getCurrentUser().getUid();
        databaseReference.child("users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("firstName") && dataSnapshot.hasChild("middleName") && dataSnapshot.hasChild("familyName")) {

                    Toast.makeText(ChatActivity.this,"Welcome",Toast.LENGTH_SHORT).show();
                }
                else
                {
              sendUserToSettingsActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendUserToLoginActivity() {
        Intent loginintent=new Intent(ChatActivity.this,LoginActivity.class);
        loginintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginintent);
        finish();
    }

    private void sendUserToSettingsActivity() {
        Intent settingsintent=new Intent(ChatActivity.this,SettingsActivity.class);
        startActivity(settingsintent);
        finish();
    }

    private void sendUserToFindFriendsActivity() {
        Intent friendsintent=new Intent(ChatActivity.this,FindFriendsActivity.class);
        startActivity(friendsintent);
        finish();
    }

   private void updateUserStatus(String state)
    {
        String savecurrentTime,savecurrentDate;
        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("dd/MM/yyyy");
        savecurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("hh:mm a");
        savecurrentTime=currentTime.format(calendar.getTime());

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("time",savecurrentTime);
        hashMap.put("date",savecurrentDate);
        hashMap.put("state",state);

        currentUserId=firebaseAuth.getCurrentUser().getUid();
        databaseReference.child("users").child(currentUserId).child("userState").updateChildren(hashMap);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.options_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();
        if(id==R.id.main_find_friends_menu)
        {
            sendUserToFindFriendsActivity();
        }
        else if(id==R.id.main_settings_menu)
        {
            sendUserToSettingsActivity();
        }
        else if(id==R.id.main_logout_menu)
        {
            updateUserStatus("offline");
            SharedPreferences prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("loggedIn", false);
            editor.apply();
            firebaseAuth.signOut();
            Intent intent = new Intent(ChatActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(ChatActivity.this, "User logged out successfully...", Toast.LENGTH_SHORT).show();
            return true; }

        return true;
    }

}
