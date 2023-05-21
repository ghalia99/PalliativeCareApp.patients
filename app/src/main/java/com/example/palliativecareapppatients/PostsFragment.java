package com.example.palliativecareapppatients;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostListAdapter adapter;
    private List<Post> postList;

    private DatabaseReference postsRef;
    private ChildEventListener postsListener;

    private DatabaseReference topicsRef;
    private ChildEventListener topicsListener;
    private List<String> followedTopicIds = new ArrayList<>();

    private TextView title;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postList = new ArrayList<>();
        adapter = new PostListAdapter(postList);
        recyclerView.setAdapter(adapter);
        title=view.findViewById(R.id.post_title);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        postsRef = database.getReference("posts");
        topicsRef = database.getReference("topics");


        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        loadFollowedPosts(currentUserID);
        Toast.makeText(getActivity(), currentUserID, Toast.LENGTH_SHORT).show();
        return view;
    }
    private void loadFollowedPosts(String userID) {
        Log.d("Load Followed Posts", "User ID: " + userID);

        FirebaseDatabase.getInstance().getReference("users")
                .child(userID)
                .child("followingTopic")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot topicSnapshot : dataSnapshot.getChildren()) {
                            String topicId = topicSnapshot.getKey();
                            followedTopicIds.add(topicId);
                        }
                        Log.d("Load Followed Posts", "Followed Topic IDs: " + followedTopicIds.toString());

                        fetchPostsForFollowedTopics();
                        Toast.makeText(getContext(), "Loading posts...", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Load Followed Posts", "Failed to load followed topics: " + databaseError.getMessage());
                    }
                });
    }
    private void fetchPostsForFollowedTopics() {
        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserID).child("followingTopic");
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("posts");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> followedTopicIds = new ArrayList<>();
                for (DataSnapshot topicSnapshot : dataSnapshot.getChildren()) {
                    if (topicSnapshot.getValue(Boolean.class)) {
                        followedTopicIds.add(topicSnapshot.getKey());
                    }
                }

                Query query = postsRef.orderByChild("topicId");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Post> filteredPosts = new ArrayList<>();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Post post = postSnapshot.getValue(Post.class);
                            if (post != null && followedTopicIds.contains(post.getTopic())) {
                                filteredPosts.add(post);
                            }
                        }

                        Log.d("Fetch Posts", "Filtered Posts: " + filteredPosts.size());

                        // Display the filtered posts (e.g., set them in RecyclerView adapter)


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Fetch Posts", "Failed to fetch posts: " + databaseError.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Fetch Posts", "Failed to load followed topics: " + databaseError.getMessage());
            }
        });
    }


}
