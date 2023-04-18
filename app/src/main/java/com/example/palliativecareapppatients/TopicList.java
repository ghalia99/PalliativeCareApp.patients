package com.example.palliativecareapppatients;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the factory method to
 * create an instance of this fragment.
 */

public class TopicList extends Fragment {

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabaseRef;
    private TopicListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic_list, container, false);

        mRecyclerView = view.findViewById(R.id.topic_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new TopicListAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("topics");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Update the adapter with the new data
                List<Topic> topics = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Topic topic = dataSnapshot.getValue(Topic.class);
                    topics.add(topic);
                }
                mAdapter.setTopics(topics);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TopicList", "onCancelled: " + error.getMessage());
            }
        });

        return view;
    }

    // Define the TopicsAdapter

    private class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.TopicViewHolder> {

        private List<Topic> mTopics;

        public TopicListAdapter() {
            mTopics = new ArrayList<>();
        }

        public void setTopics(List<Topic> topics) {
            mTopics = topics;
            notifyDataSetChanged();
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

        public class TopicViewHolder extends RecyclerView.ViewHolder {

            private TextView mTitleTextView;
            private TextView mDescriptionTextView;

            public TopicViewHolder(@NonNull View itemView) {
                super(itemView);
                mTitleTextView = itemView.findViewById(R.id.topic_title);
                mDescriptionTextView = itemView.findViewById(R.id.topic_description);
            }

            public void bind(Topic topic) {
                mTitleTextView.setText(topic.getTitle());
                mDescriptionTextView.setText(topic.getDescription());
            }
        }
    }

}
