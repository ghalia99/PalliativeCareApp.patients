package com.example.palliativecareapppatients;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity2 extends AppCompatActivity {

    private String mDoctorId;
    private String mDoctorName;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    private ArrayList<ChatMessage> mChatMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        mDoctorId = getIntent().getStringExtra("doctorId");
        mDoctorName = getIntent().getStringExtra("doctorName");

        setTitle(mDoctorName);

        // Initialize Firebase
        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            finish();
            return;
        }
if (firebaseDatabase.getReference().child("chat").child(mFirebaseUser.getUid()).child(mDoctorId) != null){
        DatabaseReference chatRef = firebaseDatabase.getReference().child("chat").child(mFirebaseUser.getUid()).child(mDoctorId);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mDatabaseReference = chatRef;

                    // Initialize RecyclerView
                    mRecyclerView = findViewById(R.id.recyclerView);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity2.this);
                    layoutManager.setStackFromEnd(true);
                    mRecyclerView.setLayoutManager(layoutManager);

                    // Initialize chat messages list
                    mChatMessages = new ArrayList<>();

                    // Initialize adapter
                    mAdapter = new ChatAdapter(mChatMessages, mFirebaseUser.getUid(), mDoctorId);
                    mRecyclerView.setAdapter(mAdapter);

                    // Initialize FAB
                    FloatingActionButton fab = findViewById(R.id.fab);
                    fab.setOnClickListener(view -> {
                        // Create a new chat message and push it to Firebase
                        String messageText = "Hello, how can I help you?";
                        ChatMessage chatMessage = new ChatMessage(messageText, mFirebaseUser.getUid(), mDoctorId);
                        mDatabaseReference.push().setValue(chatMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Do nothing
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Do nothing
                            }
                        });
                    });

                    // Add child event listener to retrieve chat messages from Firebase
                    mChildEventListener = new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                            mChatMessages.add(chatMessage);
                            mAdapter.notifyItemInserted(mChatMessages.size() - 1);
                            mRecyclerView.scrollToPosition(mChatMessages.size() - 1);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            // Do nothing
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            // Do nothing
                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            // Do nothing
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Do nothing
                        }
                    };
                    chatRef.addChildEventListener(mChildEventListener);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Do nothing
            }
        });

    }
    }
}