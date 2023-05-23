package com.example.palliativecareapppatients;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TopicList extends Fragment {

    private TopicListAdapter mAdapter;
    private List<Topic> mTopics = new ArrayList<>();
    private EditText mTitleEditText, mDescriptionEditText;
    private DatabaseReference mTopicsRef;
    private DatabaseReference mUserRef;
    private String mCurrentUserId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mTopicsRef = FirebaseDatabase.getInstance().getReference().child("topics");
        mUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentUserId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic_list, container, false);

        RecyclerView mRecyclerView = view.findViewById(R.id.topic_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new TopicListAdapter(mTopics, new TopicListAdapter.OnTopicClickListener() {
            @Override
            public void onTopicClick(int position) {
                Topic selectedTopic = mTopics.get(position);

                // Start the PostsActivity and pass the selected topic ID as an extra
                Intent intent = new Intent(getContext(), topicsPostsActivity.class);
                intent.putExtra("topicId", selectedTopic.getId());
                startActivity(intent);
            }

            @Override
            public void onFollowClick(int position) {
                Topic topic = mTopics.get(position);
                String topicId = topic.getId();

                // Toggle the follow state
                boolean isFollowed = topic.getIs_followed();
                topic.setIs_followed(!isFollowed);
                mAdapter.notifyItemChanged(position);

                // Update user's followingTopic list
                DatabaseReference followingRef = mUserRef.child("followingTopic");

                if (!isFollowed) {
                    // Add the topic ID to the user's followingTopic list
                    followingRef.child(topicId).setValue(true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Update successful
                                    Log.d("DoctorTopicList", "Topic added to followingTopic");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Update failed
                                    Log.d("DoctorTopicList", "Failed to add topic to followingTopic: " + e.getMessage());
                                }
                            });
                } else {
                    // Remove the topic ID from the user's followingTopic list
                    followingRef.child(topicId).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Update successful
                                    Log.d("DoctorTopicList", "Topic removed from followingTopic");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Update failed
                                    Log.d("DoctorTopicList", "Failed to remove topic from followingTopic: " + e.getMessage());
                                }
                            });
                }
            }

        });

        mRecyclerView.setAdapter(mAdapter);


        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

        mTopicsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mTopics.clear();

                DatabaseReference followingRef = mUserRef.child("followingTopic");
                followingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot followingSnapshot) {
                        for (DataSnapshot topicSnapshot : dataSnapshot.getChildren()) {
                            Topic topic = topicSnapshot.getValue(Topic.class);
                            if (topic != null) {
                                String topicId = topic.getId();
                                boolean isFollowed = followingSnapshot.hasChild(topicId);
                                topic.setIs_followed(isFollowed);
                                mTopics.add(topic);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("DoctorTopicList", databaseError.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DoctorTopicList", databaseError.getMessage());
            }
        });
    }



}
