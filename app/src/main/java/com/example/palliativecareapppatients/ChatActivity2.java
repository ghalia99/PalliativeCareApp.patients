package com.example.palliativecareapppatients;
/*
import android.os.Bundle;
import android.widget.Toast;

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
        try {
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

                            try {
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
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(ChatActivity2.this, "Error 1: Failed to initialize. Please try again later.", Toast.LENGTH_SHORT).show();
                                // Handle exception here, e.g. log error, show error message to user, etc.
                            }
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


        }catch (Exception e) {
            // Handle any exceptions that may occur during initialization
            e.printStackTrace();
            Toast.makeText(this, "Error: Failed to initialize. Please try again later.", Toast.LENGTH_SHORT).show();
            // Show an error message or take appropriate action
        }
    }
}

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity2 extends AppCompatActivity {
    private String mDoctorId;
    private String mDoctorName;
    private EditText mMessageEditText;
    private Button mSendButton;
    private RecyclerView mRecyclerView;
    private ChatAdapter mChatAdapter;
    private ArrayList<ChatMessage> mChatMessages;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        mDoctorId = getIntent().getStringExtra("doctorId");
        mDoctorName = getIntent().getStringExtra("doctorName");

        setTitle(mDoctorName);

        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        if (mCurrentUser == null) {
            // Not signed in, launch the Sign In activity
            finish();
            return;
        }

        DatabaseReference chatRef = firebaseDatabase.getReference().child("chat").child(mCurrentUser.getUid()).child(mDoctorId);
        chatRef.keepSynced(true);

        mMessageEditText = findViewById(R.id.edit_text_message);
        mSendButton = findViewById(R.id.button_send);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatMessages = new ArrayList<>();
        mChatAdapter = new ChatAdapter(mChatMessages);
        mRecyclerView.setAdapter(mChatAdapter);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = mMessageEditText.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    ChatMessage chatMessage = new ChatMessage(messageText, mCurrentUser.getUid(), mDoctorId,1);
                    mDatabaseReference = chatRef.push();
                    mDatabaseReference.setValue(chatMessage);
                    mMessageEditText.setText("");
                }
            }
        });

        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @NonNull String previousChildName) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                mChatMessages.add(chatMessage);
                mChatAdapter.notifyDataSetChanged();
                mRecyclerView.scrollToPosition(mChatMessages.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @NonNull String previousChildName) {
                // Do nothing
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // Do nothing
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @NonNull String previousChildName) {
                // Do nothing
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Do nothing
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDatabaseReference != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);
        }
    }

    private static class ChatViewHolder extends RecyclerView.ViewHolder {
        public ChatViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {
        private ArrayList<ChatMessage> mChatMessages;

        public ChatAdapter(ArrayList<ChatMessage> chatMessages) {
            mChatMessages = chatMessages;
        }

        @NonNull
        @Override
        public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
            return new ChatViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
            ChatMessage chatMessage = mChatMessages.get(position);
            // Bind chat message to ViewHolder
        }

        @Override
        public int getItemCount() {
            return mChatMessages.size();
        }
    }
    private ChildEventListener mChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            // Handle child added event
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            // Handle child changed event
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            // Handle child removed event
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            // Handle child moved event
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Handle cancellation event
        }
    };

}*/
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class ChatActivity2 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mMessagesDatabase;
    private EditText mMessageEditText;
    private Button mSendButton;
    private RecyclerView mChatRecyclerView;
    private FirebaseRecyclerAdapter<ChatMessage, ChatAdapter.ChatViewHolder> mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private String mCurrentUserId;
    private String mDoctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();
        mDoctorId = getIntent().getStringExtra("doctorId");

        mMessagesDatabase = FirebaseDatabase.getInstance().getReference().child("messages");

        mMessageEditText = findViewById(R.id.edit_text_message);
        mSendButton = findViewById(R.id.button_send);
        mChatRecyclerView = findViewById(R.id.recycler_view_chat);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mChatRecyclerView.setLayoutManager(mLinearLayoutManager);



         FirebaseRecyclerOptions<ChatMessage> options = new FirebaseRecyclerOptions.Builder<ChatMessage>()
                .setQuery(mMessagesDatabase, ChatMessage.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<ChatMessage, ChatAdapter.ChatViewHolder>(options) {
            @NonNull
            @Override
            public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
                return new ChatAdapter.ChatViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position, @NonNull ChatMessage message) {
                holder.bind(message, mCurrentUserId, mDoctorId);
            }
        };

        mChatRecyclerView.setAdapter(mAdapter);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = mMessageEditText.getText().toString();
                if (!messageText.isEmpty()) {
                    String messageId = mMessagesDatabase.push().getKey();
                    ChatMessage chatMessage = new ChatMessage( messageText, mCurrentUserId,new Date().getTime());
                    mMessagesDatabase.child(messageId).setValue(chatMessage);
                    mMessageEditText.setText("");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}

