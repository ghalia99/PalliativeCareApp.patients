package com.example.palliativecareapppatients;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends FirebaseRecyclerAdapter<ChatMessage, ChatAdapter.ViewHolder> {

    private FirebaseAuth firebaseAuth;

    public ChatAdapter(@NonNull FirebaseRecyclerOptions<ChatMessage> options) {
        super(options);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ChatMessage model) {
        String message = model.getMessage();
        String sender = model.getSenderId();
        String receiver = model.getReceiverId();

        holder.messageTextView.setText(message);

        if (sender.equals(firebaseAuth.getCurrentUser().getDisplayName())) {
            holder.messageTextView.setBackgroundResource(R.drawable.sent_message_background);
            holder.messageTextView.setGravity(Gravity.END);
        } else {
            holder.messageTextView.setBackgroundResource(R.drawable.received_message_background);
            holder.messageTextView.setGravity(Gravity.START);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView messageTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.message_text_view);
        }
    }
}

