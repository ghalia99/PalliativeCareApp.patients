package com.example.palliativecareapppatients;

/*
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

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private ArrayList<ChatMessage> mChatMessages;
    private String mCurrentUserId;
    private String mDoctorId;

    public ChatAdapter(ArrayList<ChatMessage> chatMessages, String currentUserId, String doctorId) {
        mChatMessages = chatMessages;
        mCurrentUserId = currentUserId;
        mDoctorId = doctorId;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = mChatMessages.get(position);

        holder.mMessageTextView.setText(message.getMessage());

        if (message.getSenderId().equals(mCurrentUserId)) {
            holder.mMessageTextView.setBackgroundResource(R.drawable.sent_message_background);
            holder.mMessageTextView.setGravity(Gravity.END);
        } else if (message.getSenderId().equals(mDoctorId)) {
            holder.mMessageTextView.setBackgroundResource(R.drawable.received_message_background);
            holder.mMessageTextView.setGravity(Gravity.START);
        }
    }

    @Override
    public int getItemCount() {
        return mChatMessages.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        public TextView mMessageTextView;

        public ChatViewHolder(View itemView) {
            super(itemView);
            mMessageTextView = itemView.findViewById(R.id.message_text_view);
        }
    }
}
*/
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

public class ChatAdapter extends FirebaseRecyclerAdapter<ChatMessage, ChatAdapter.ChatViewHolder> {

    private String mCurrentUserId;
    private String mDoctorId;

    public ChatAdapter(@NonNull FirebaseRecyclerOptions<ChatMessage> options, String currentUserId, String doctorId) {
        super(options);
        mCurrentUserId = currentUserId;
        mDoctorId = doctorId;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull ChatMessage message) {
        holder.bind(message, mCurrentUserId, mDoctorId);
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        public TextView mMessageTextView;

        public ChatViewHolder(View itemView) {
            super(itemView);
            mMessageTextView = itemView.findViewById(R.id.message_text_view);
        }

        public void bind(ChatMessage message, String currentUserId, String doctorId) {
            mMessageTextView.setText(message.getMessageText());

            if (message.getSenderId().equals(currentUserId)) {
                mMessageTextView.setBackgroundResource(R.drawable.sent_message_background);
                mMessageTextView.setGravity(Gravity.END);
            } else if (message.getSenderId().equals(doctorId)) {
                mMessageTextView.setBackgroundResource(R.drawable.received_message_background);
                mMessageTextView.setGravity(Gravity.START);
            }
        }
    }
}
