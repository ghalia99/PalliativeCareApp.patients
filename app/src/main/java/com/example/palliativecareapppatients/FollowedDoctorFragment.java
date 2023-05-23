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

public class FollowedDoctorFragment extends Fragment {
    private RecyclerView recyclerView;
    private PostListAdapter postsAdapter;
    private List<Post> postList;

    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followed_doctor, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postList = new ArrayList<>();
        postsAdapter = new PostListAdapter(postList);
        recyclerView.setAdapter(postsAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference contactsRef = databaseReference.child("Contacts").child(userId);
            contactsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("contactSnapshot", "Contacts snapshot: " + dataSnapshot);

                    List<String> userContacts = new ArrayList<>();

                    for (DataSnapshot contactSnapshot : dataSnapshot.getChildren()) {
                        String contactUserId = contactSnapshot.getKey();
                        userContacts.add(contactUserId);
                    }

                    retrieveMatchingPosts(userContacts);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // حدث خطأ في قراءة جهات الاتصال
                }
            });
        }

        return view;
    }

    private void retrieveMatchingPosts(List<String> userContacts) {
        DatabaseReference postsRef = databaseReference.child("posts");

        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.d("postSnapshot", "Post snapshot: " + postSnapshot);
                    Post post = postSnapshot.getValue(Post.class);
                    if (post != null) {
                        if (userContacts.contains(postSnapshot.child("userId").getValue(String.class))) {
                            postList.add(post);
                            Log.d("postList", "Post added: " + post.getTitle());
                        }
                    }
                }

                postsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // حدث خطأ في قراءة البوستات
            }
        });
    }
}
