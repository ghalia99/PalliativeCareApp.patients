package com.example.palliativecareapppatients;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class ChatsFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private DatabaseReference chatRef, userRef;
    private FirebaseAuth mAuth;
    private String userID;

    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chats, container, false);
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        chatRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(userID);
        userRef = FirebaseDatabase.getInstance().getReference().child("users");

        recyclerView = view.findViewById(R.id.chats_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Contacts> options = new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(chatRef, Contacts.class)
                .build();

        FirebaseRecyclerAdapter<Contacts, ChatViewHolder> adapter = new FirebaseRecyclerAdapter<Contacts, ChatViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ChatViewHolder holder, int position, @NonNull Contacts model) {
                final String[] username = {""};
                final String userid = getRef(position).getKey();
                final String[] image = {"default_image"};
                userRef.child(userid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (dataSnapshot.hasChild("profilePhotoUrl")) {
                                image[0] = dataSnapshot.child("profilePhotoUrl").getValue().toString();
                                Picasso.get().load(image[0]).placeholder(R.drawable.profile_image).into(holder.profile_image);
                            }

                            if (dataSnapshot.hasChild("firstName") && dataSnapshot.hasChild("middleName") && dataSnapshot.hasChild("familyName")) {
                                username[0] = dataSnapshot.child("firstName").getValue(String.class) +
                                        dataSnapshot.child("middleName").getValue(String.class) +
                                        dataSnapshot.child("familyName").getValue(String.class);
                            }

                            holder.username.setText(username[0]);
                            holder.userstatus.setText("Last seen:\nDate Time");

                            if (dataSnapshot.child("userState").hasChild("state")) {
                                String state = dataSnapshot.child("userState").child("state").getValue().toString();
                                String date = dataSnapshot.child("userState").child("date").getValue().toString();
                                String time = dataSnapshot.child("userState").child("time").getValue().toString();

                                if (state.equals("online")) {
                                    holder.userstatus.setText("online");
                                } else if (state.equals("offline")) {
                                    holder.userstatus.setText("Last seen:\n" + date + " " + time);
                                }
                            } else {
                                holder.userstatus.setText("offline");
                            }

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent chatIntent = new Intent(getContext(), ChatActivity2.class);
                                    chatIntent.putExtra("visit_user_id", userid);
                                    chatIntent.putExtra("visit_user_name", username[0]);
                                    chatIntent.putExtra("visit_image", image[0]);
                                    startActivity(chatIntent);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.users_display_layout, parent, false);
                return new ChatViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        CircularImageView profile_image;
        TextView username, userstatus;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.users_profile_image);
            username = itemView.findViewById(R.id.users_profile_name);
            userstatus = itemView.findViewById(R.id.users_status);
        }
    }
}
