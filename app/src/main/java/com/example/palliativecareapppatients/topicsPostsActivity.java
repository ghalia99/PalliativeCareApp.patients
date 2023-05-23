package com.example.palliativecareapppatients;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class topicsPostsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PostListAdapter adapter;
    private DatabaseReference postsRef;
    private List<Post> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics_posts);

        // Retrieve the selected topic ID from the intent
        String topicId = getIntent().getStringExtra("topicId");

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the list and adapter
        postList = new ArrayList<>();
        adapter = new PostListAdapter(postList);
        recyclerView.setAdapter(adapter);

        // Get the reference to the "posts" node in the Firebase Realtime Database
        postsRef = FirebaseDatabase.getInstance().getReference("posts");

        // Add a child event listener to fetch posts for the specified topic
        postsRef.orderByChild("topicId").equalTo(topicId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // A new post has been added
                Post post = dataSnapshot.getValue(Post.class);
                if (post != null) {
                    post.setPostId(dataSnapshot.getKey());
                    postList.add(post);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // A post has been updated
                Post updatedPost = dataSnapshot.getValue(Post.class);
                if (updatedPost != null) {
                    updatedPost.setPostId(dataSnapshot.getKey());
                    for (int i = 0; i < postList.size(); i++) {
                        if (postList.get(i).getPostId().equals(updatedPost.getPostId())) {
                            postList.set(i, updatedPost);
                            adapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // A post has been removed
                String removedPostId = dataSnapshot.getKey();
                for (int i = 0; i < postList.size(); i++) {
                    if (postList.get(i).getPostId().equals(removedPostId)) {
                        postList.remove(i);
                        adapter.notifyItemRemoved(i);
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // A post has been moved
                // Not handling this event in the current implementation
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // An error occurred while fetching posts
                Toast.makeText(topicsPostsActivity.this, "Failed to load posts: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
