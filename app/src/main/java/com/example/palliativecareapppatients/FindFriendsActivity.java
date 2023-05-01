package com.example.palliativecareapppatients;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class FindFriendsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference Userref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        Userref= FirebaseDatabase.getInstance().getReference().child("users");

        recyclerView=findViewById(R.id.find_friends_recyclerlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Find friends");



    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Contacts> options=new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(Userref,Contacts.class)
                .build();


        FirebaseRecyclerAdapter<Contacts,FindFriendsViewHolder> adapter=
                new FirebaseRecyclerAdapter<Contacts, FindFriendsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FindFriendsViewHolder holder, final int position, @NonNull Contacts model) {
                        holder.username.setText(model.getName());
                        holder.userstatus.setText(model.getStatus());
                        Picasso.get().load(model.getImage()).placeholder(R.drawable.profile_image).into(holder.profile);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String visit_user_id = getRef(holder.getAdapterPosition()).getKey();
                                Intent profileintent = new Intent(FindFriendsActivity.this, ProfileActivity.class);
                                profileintent.putExtra("visit_user_id", visit_user_id);
                                startActivity(profileintent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public FindFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.users_display_layout,parent,false);
                        FindFriendsViewHolder viewHolder=new FindFriendsViewHolder(view);
                        return viewHolder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder {
        TextView username,userstatus;
        CircularImageView profile;
        public FindFriendsViewHolder(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.users_profile_name);
            userstatus=itemView.findViewById(R.id.users_status);
            profile=itemView.findViewById(R.id.users_profile_image);
        }
    }
}
