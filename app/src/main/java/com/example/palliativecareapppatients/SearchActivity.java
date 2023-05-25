package com.example.palliativecareapppatients;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private List<String> posts;
    private List<String> topics;
    private List<String> users;

    private RecyclerView recyclerView;
    private EditText searchEditText;
    private ListAdapter listAdapter;

    private DataSnapshot postsSnapshot;
    private DataSnapshot topicsSnapshot;
    private DataSnapshot usersSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize data lists
        posts = new ArrayList<>();
        topics = new ArrayList<>();
        users = new ArrayList<>();

        // Set up RecyclerView
        recyclerView = findViewById(R.id.searchRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new ListAdapter();
        recyclerView.setAdapter(listAdapter);

        // Set up search functionality
        searchEditText = findViewById(R.id.searchEditText);

        // Get data from Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Store the dataSnapshot for posts
                postsSnapshot = dataSnapshot;

                // Clear existing data
                posts.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String title = snapshot.child("title").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);

                    posts.add(title);
                    posts.add(description);
                }

                // Notify the adapter of the data change
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors
            }
        });

        database.getReference("topics").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Store the dataSnapshot for topics
                topicsSnapshot = dataSnapshot;

                // Clear existing data
                topics.clear();

                // Iterate through the data snapshot and add topics to the list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String topic = snapshot.child("title").getValue(String.class);
                    topics.add(topic);
                }

                // Notify the adapter of the data change
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors
            }
        });

        database.getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Store the dataSnapshot for users
                usersSnapshot = dataSnapshot;

                // Clear existing data
                users.clear();

                // Iterate through the data snapshot and add users to the list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String user = snapshot.child("firstName").getValue(String.class) + " " +
                            snapshot.child("middleName").getValue(String.class) + " " +
                            snapshot.child("familyName").getValue(String.class);
                    users.add(user);
                }

                // Notify the adapter of the data change
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }

    public void search(View view) {
        String query = searchEditText.getText().toString().toLowerCase();
        List<String> searchResults = new ArrayList<>();

        // Perform search based on query
        for (String post : posts) {
            if (query != null && post != null && post.toLowerCase().contains(query)) {
                searchResults.add(post);
            }
        }
        for (String topic : topics) {
            if (query != null && topic != null && topic.toLowerCase().contains(query)) {
                searchResults.add(topic);
            }
        }
        for (String user : users) {
            if (query != null && user != null && user.toLowerCase().contains(query)) {
                searchResults.add(user);
            }
        }

        // Update RecyclerView with search results
        listAdapter.setData(searchResults);
        listAdapter.notifyDataSetChanged();
    }

    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

        private List<String> data;

        public void setData(List<String> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final String item = data.get(position);
            holder.titleTextView.setText(item);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigate to detail page with the selected item
                    Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                    //int index = posts.indexOf(item);
                    if (posts.contains(item)) {

                        for (DataSnapshot snapshot : postsSnapshot.getChildren()) {
                            String title = snapshot.child("title").getValue(String.class);
                            String d =snapshot.child("description").getValue(String.class);
                            if (item.equalsIgnoreCase(title)) {
                                String postId = snapshot.getKey(); // الحصول على معرف البوست
                                intent.putExtra("postId", postId);

                            } else if (item.equalsIgnoreCase(d)) {
                                String postId = snapshot.getKey(); // الحصول على معرف البوست
                                intent.putExtra("postId", postId);

                            }
                        }


                    } else if (topics.contains(item)) {
                        for (DataSnapshot snapshot : topicsSnapshot.getChildren()) {
                            String title = snapshot.child("title").getValue(String.class);
                            if (item.equalsIgnoreCase(title)) {
                                String topicId = snapshot.getKey(); // الحصول على معرف البوست
                                intent.putExtra("topicId", topicId);
                                Toast.makeText(SearchActivity.this,topicId ,Toast.LENGTH_SHORT).show();

                            }
                        }
                    } else if (users.contains(item)) {
                        String[] name=     item.split(" ");
                        for (DataSnapshot snapshot : usersSnapshot.getChildren()) {
                             String user1 = snapshot.child("firstName").getValue(String.class);
                             String user2=snapshot.child("middleName").getValue(String.class);
                             String user3=snapshot.child("familyName").getValue(String.class);

                            if (name[0].equalsIgnoreCase(user1) &&name[1].equalsIgnoreCase(user2) &&name[2].equalsIgnoreCase(user3) ) {
                                String userId = snapshot.getKey(); // الحصول على معرف البوست
                                intent.putExtra("userId", userId);

                            }
                        }
                    }

                    startActivity(intent);

                }
            });

        }

        @Override
        public int getItemCount() {
            return data != null ? data.size() : 0;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView titleTextView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                titleTextView = itemView.findViewById(R.id.titleTextView);
            }
        }
    }
}
