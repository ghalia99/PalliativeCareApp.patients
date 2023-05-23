package com.example.palliativecareapppatients;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;


public class RequestsFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private DatabaseReference chatrequestRef, userref, contactref;
    private FirebaseAuth mauth;
    private String currentUserId;

    public RequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_requests, container, false);
        recyclerView = view.findViewById(R.id.chat_request_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userref = FirebaseDatabase.getInstance().getReference().child("users");
        chatrequestRef = FirebaseDatabase.getInstance().getReference().child("Chat Requests");
        contactref = FirebaseDatabase.getInstance().getReference().child("Contacts");
        mauth = FirebaseAuth.getInstance();
        currentUserId = mauth.getCurrentUser().getUid();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contacts> options = new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(chatrequestRef.child(currentUserId), Contacts.class)
                .build();

        FirebaseRecyclerAdapter<Contacts, RequestViewHolder> adapter = new FirebaseRecyclerAdapter<Contacts, RequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final RequestViewHolder holder, int position, @NonNull Contacts model) {
                holder.itemView.findViewById(R.id.request_accept_button).setVisibility(View.VISIBLE);
                holder.itemView.findViewById(R.id.request_cancel_button).setVisibility(View.VISIBLE);
                final String userId = getRef(position).getKey();

                DatabaseReference getTypeRef = getRef(position).child("request_type").getRef();
                getTypeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String type = dataSnapshot.getValue().toString();

                            if (type.equals("received")) {
                                userref.child(userId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        holder.imageView.setVisibility(View.INVISIBLE);
                                        if (dataSnapshot.hasChild("profilePhotoUrl")) {
                                            final String requestImage = dataSnapshot.child("profilePhotoUrl").getValue().toString();
                                            Picasso.get().load(requestImage).placeholder(R.drawable.profile_image).into(holder.profilePicture);
                                        }

                                        final String firstName = dataSnapshot.child("firstName").getValue().toString();
                                        final String middleName = dataSnapshot.child("middleName").getValue().toString();
                                        final String familyName = dataSnapshot.child("familyName").getValue().toString();
                                        final String requestUsername = firstName + " " + middleName + " " + familyName;
                                        String requestStatus="";
                                        if (dataSnapshot.hasChild("status")) {

                                            requestStatus = dataSnapshot.child("status").getValue().toString();
                                        }

                                        holder.username.setText(requestUsername);
                                        holder.userStatus.setText("Want to connect with you");
                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                CharSequence options[] = new CharSequence[]{
                                                        "Accept", "Cancel"
                                                };
                                                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                builder.setTitle(requestUsername + " Chat Request");
                                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if (which == 0) {
                                                            contactref.child(currentUserId).child(userId).child("Contacts")
                                                                    .setValue("Saved")
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                contactref.child(userId).child(currentUserId).child("Contacts")
                                                                                        .setValue("Saved")
                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if (task.isSuccessful()) {
                                                                                                    chatrequestRef.child(currentUserId).child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                            if (task.isSuccessful()) {
                                                                                                                chatrequestRef.child(userId).child(currentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                                                        if (task.isSuccessful()) {
                                                                                                                            Toast.makeText(getContext(), "New Contact Saved", Toast.LENGTH_SHORT).show();
                                                                                                                        }
                                                                                                                    }
                                                                                                                });
                                                                                                            }
                                                                                                        }
                                                                                                    });
                                                                                                }
                                                                                            }
                                                                                        });
                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                        if (which == 1) {
                                                            chatrequestRef.child(currentUserId).child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        chatrequestRef.child(userId).child(currentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    Toast.makeText(getContext(), "Request Deleted", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            });
                                                        }

                                                    }
                                                });
                                                builder.show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            } else if (type.equals("sent")) {
                                Button requestSentBtn = holder.itemView.findViewById(R.id.request_accept_button);
                                requestSentBtn.setText("Cancel Req");
                                holder.itemView.findViewById(R.id.request_cancel_button).setVisibility(View.INVISIBLE);

                                userref.child(userId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild("profilePhotoUrl")) {
                                            final String requestImage = dataSnapshot.child("profilePhotoUrl").getValue().toString();
                                            Picasso.get().load(requestImage).placeholder(R.drawable.profile_image).into(holder.profilePicture);
                                        }

                                        final String firstName = dataSnapshot.child("firstName").getValue().toString();
                                        final String middleName = dataSnapshot.child("middleName").getValue().toString();
                                        final String familyName = dataSnapshot.child("familyName").getValue().toString();
                                        final String requestUsername = firstName + " " + middleName + " " + familyName;
                                        String requestStatus="";
                                        if (dataSnapshot.hasChild("status")) {

                                            requestStatus = dataSnapshot.child("status").getValue().toString();
                                        }
                                        holder.username.setText(requestUsername);
                                        holder.userStatus.setText("You have sent a request to " + requestUsername);
                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                CharSequence options[] = new CharSequence[]{
                                                        "Cancel chat request"
                                                };
                                                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                builder.setTitle("Already sent Request");
                                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        if (which == 0) {
                                                            chatrequestRef.child(currentUserId).child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        chatrequestRef.child(userId).child(currentUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    Toast.makeText(getContext(), "You have cancelled the chat request.", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            });
                                                        }

                                                    }
                                                });
                                                builder.show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_display_layout, parent, false);
                RequestViewHolder viewHolder = new RequestViewHolder(view);
                return viewHolder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView username, userStatus;
        CircularImageView profilePicture;
        Button acceptButton, rejectButton;
        ImageView imageView;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.users_profile_name);
            userStatus = itemView.findViewById(R.id.users_status);
            profilePicture = itemView.findViewById(R.id.users_profile_image);
            acceptButton = itemView.findViewById(R.id.request_accept_button);
            rejectButton = itemView.findViewById(R.id.request_cancel_button);
            imageView = itemView.findViewById(R.id.users_online_status);
        }
    }
}
