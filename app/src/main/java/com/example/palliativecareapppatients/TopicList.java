package com.example.palliativecareapppatients;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
   // private EditText mTitleEditText, mDescriptionEditText;
    private DatabaseReference mTopicsRef;
    private DatabaseReference mUserRef;
    private String mCurrentUserId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mCurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mTopicsRef = FirebaseDatabase.getInstance().getReference().child("topics");
        mUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(mCurrentUserId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic_list, container, false);


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
                if (!isFollowed) {
                    mUserRef.child("followingTopic").child(topicId).setValue(true);
                } else {
                    mUserRef.child("followingTopic").child(topicId).removeValue();
                }
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  showAddTopicDialog();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Retrieve the list of topics
        mTopicsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mTopics.clear();
                for (DataSnapshot topicSnapshot : dataSnapshot.getChildren()) {
                    Topic topic = topicSnapshot.getValue(Topic.class);
                    if (topic != null) {
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

}
