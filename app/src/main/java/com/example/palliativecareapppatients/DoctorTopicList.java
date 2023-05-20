package com.example.palliativecareapppatients;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DoctorTopicList extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_doctor_topic_list, container, false);

        RecyclerView mRecyclerView = view.findViewById(R.id.topic_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new TopicListAdapter(mTopics, new TopicListAdapter.OnTopicClickListener() {
            @Override
            public void onTopicClick(int position) {
                Topic topic = mTopics.get(position);

                // Pass the topic information to the PostsFragment
                PostsFragment postsFragment = new PostsFragment();
                Bundle args = new Bundle();
                args.putString("topicId", topic.getId());
                args.putString("topicTitle", topic.getTitle());
                postsFragment.setArguments(args);

                // Replace the current fragment with PostsFragment
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, postsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
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

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTopicDialog();
            }
        });

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


    private void showAddTopicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Topic");

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_topic, null);
        mTitleEditText = view.findViewById(R.id.title_edit_text);
        mDescriptionEditText = view.findViewById(R.id.description_edit_text);

        builder.setView(view);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = mTitleEditText.getText().toString().trim();
                String description = mDescriptionEditText.getText().toString().trim();

                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description)) {
                    addTopic(title, description);
                } else {
                    Toast.makeText(getActivity(), "Please enter title and description", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void addTopic(String title, String description) {
        String id = mTopicsRef.push().getKey();
        Topic topic = new Topic(id, title, description, false);
        mTopicsRef.child(id).setValue(topic);
        Toast.makeText(getActivity(), "Topic added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        for (int i = 0; i < mTopics.size(); i++) {
            Topic topic = mTopics.get(i);
            outState.putBoolean("is_followed_" + i, topic.getIs_followed());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            for (int i = 0; i < mTopics.size(); i++) {
                boolean isFollowed = savedInstanceState.getBoolean("is_followed_" + i);
                mTopics.get(i).setIs_followed(isFollowed);
            }
        }
    }
}
