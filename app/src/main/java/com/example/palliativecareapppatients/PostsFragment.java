package com.example.palliativecareapppatients;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends Fragment {
    private RecyclerView recyclerView;
    private PostListAdapter postsAdapter;
    private List<Post> postList;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postList = new ArrayList<>();
        postsAdapter = new PostListAdapter( postList,getActivity());
        recyclerView.setAdapter(postsAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();


        String userId = currentUser.getUid();

        DatabaseReference followingTopicsRef = databaseReference.child("users").child(userId).child("followingTopic");
        followingTopicsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> followingTopics = new ArrayList<>();
                for (DataSnapshot topicSnapshot : dataSnapshot.getChildren()) {
                    String topicId = topicSnapshot.getKey();
                    followingTopics.add(topicId);
                    Log.d("followingTopics",followingTopics.size()+" ");
                }

                // قم بتحميل جميع البوستات من قاعدة البيانات
                DatabaseReference postsRef = databaseReference.child("posts");
                postsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        postList.clear();
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Post post = postSnapshot.getValue(Post.class);
                                if (post != null) {
                                    Log.d("postList", "Post retrieved: " + post.getTitle());
                                    Log.d("postList", "Topic ID: " + post.getTopicId());
                                    Log.d("postList", "Following topics: " + followingTopics.toString());

                                    if (followingTopics.contains(postSnapshot.child("topicId").getValue(String.class))) {
                                        postList.add(post);
                                        Log.d("postList", "Post added: " + post.getTitle());
                                    }
                                }
                            }
                            Log.d("postList", "Post list size: " + postList.size());

                            postsAdapter.notifyDataSetChanged();
                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("postList", postList.size() + "....");
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // حدث خطأ في قراءة البيانات
            }
        });

        return view;
    }
}
