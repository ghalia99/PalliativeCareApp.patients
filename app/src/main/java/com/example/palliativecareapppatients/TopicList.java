package com.example.palliativecareapppatients;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */





public class TopicList extends Fragment {

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabaseRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic_list, container, false);

        // Initialize Firebase Database reference
    /*    mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("topics");

        // Initialize RecyclerView
        mRecyclerView = view.findViewById(R.id.topic_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTopicsAdapter = new TopicsAdapter();
        mRecyclerView.setAdapter(mTopicsAdapter);

        // Set click listener for adapter items
        mTopicsAdapter.setOnItemClickListener(new TopicsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String topicId) {
                // Handle item click, e.g., navigate to PostsFragment with selected topicId
                PostsFragment postsFragment = new PostsFragment();
                Bundle args = new Bundle();
                args.putString("topicId", topicId);
                postsFragment.setArguments(args);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, postsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }*/
        mRecyclerView = view.findViewById(R.id.topic_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        TopicListAdapter mAdapter = new TopicListAdapter();
        mRecyclerView.setAdapter(mAdapter);

        // Initialize Firebase Realtime Database reference
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("topics");

        // Add ValueEventListener to fetch data from Firebase
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear existing data

                mAdapter.clearData();
                // Iterate through the data from Firebase
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Topic topic = dataSnapshot.getValue(Topic.class);
                    mAdapter.addTopic(topic);
                }
                // Notify adapter that data has changed
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " + error.getMessage());
            }
        });

        return view;
    }


    // Define the ViewHolder for the TopicsAdapter
    private static class TopicViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewTopic;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTopic = itemView.findViewById(R.id.topic_title);
        }

        public void bind(String topic) {
            mTextViewTopic.setText(topic);
        }
    }

    // Define the TopicsAdapter

    private class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.TopicViewHolder> {

        private List<Topic> mTopics;

        public TopicListAdapter() {
            mTopics = new ArrayList<>();
        }

        public void addTopic(Topic topic) {
            mTopics.add(topic);
        }

        public void clearData() {
            mTopics.clear();
        }

        @NonNull
        @Override
        public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.list_item_topic, parent, false);
            return new TopicViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
            Topic topic = mTopics.get(position);
            holder.bind(topic);
        }

        @Override
        public int getItemCount() {
            return mTopics.size();
        }

        public  class TopicViewHolder extends RecyclerView.ViewHolder {

            private TextView mTitleTextView;

            public TopicViewHolder(@NonNull View itemView) {
                super(itemView);
                mTitleTextView = itemView.findViewById(R.id.topic_title);
            }

            public void bind(Topic topic) {
                mTitleTextView.setText(topic.getTitle());
            }
        }
    }
    }



