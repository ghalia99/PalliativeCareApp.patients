package com.example.palliativecareapppatients;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatActivity2 extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText editText;
    private Button sendButton;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String senderUserName, receiverUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);



        recyclerView = findViewById(R.id.recyclerView);
        editText = findViewById(R.id.edit_text_message);
        sendButton = findViewById(R.id.button_send);

        Intent intent = getIntent();
        receiverUserName = intent.getStringExtra("doctorName");

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        senderUserName = firebaseAuth.getCurrentUser().getDisplayName();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editText.getText().toString();
                if (!message.isEmpty()) {
                    sendMessage(message);
                    editText.setText("");
                }
            }
        });

        displayMessages();
    }

    private void sendMessage(String message) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", senderUserName);
        hashMap.put("receiver", receiverUserName);
        hashMap.put("message", message);

        databaseReference.child("Chats").push().setValue(hashMap);
    }

    private void displayMessages() {
        Query query = databaseReference.child("Chats")
                .orderByChild("sender_receiver")
                .equalTo(senderUserName + "_" + receiverUserName);

        FirebaseRecyclerOptions<ChatMessage> options = new FirebaseRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .build();

        ChatAdapter chatAdapter = new ChatAdapter(options);
        recyclerView.setAdapter(chatAdapter);
        chatAdapter.startListening();
    }
}
