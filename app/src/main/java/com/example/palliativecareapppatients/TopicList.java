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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */





public class TopicList extends Fragment {

    private RecyclerView mRecyclerView;
    private TopicsAdapter mTopicsAdapter;
    private DatabaseReference mDatabaseRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic_list, container, false);

        // Initialize Firebase Database reference
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("topics");

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
    private static class TopicsAdapter extends RecyclerView.Adapter<TopicViewHolder> {
        private List<String> mTopicsList = new ArrayList<>();
        private OnItemClickListener mListener;

        // Interface for item click listener
        public interface OnItemClickListener {
            void onItemClick(String topicId);
        }

        // Method to set the item click listener
        public void setOnItemClickListener(OnItemClickListener listener) {
            mListener = listener;
        }

        @NonNull
        @Override
        public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_topic, parent, false);
            return new TopicViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
            final String topic = mTopicsList.get(position);
            holder.bind(topic);

            // Set click listener for item view
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(topic);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mTopicsList.size();
        }

        // Method to set the topics list and notify data set changed
        public void setTopicsList(List<String> topicsList) {
            mTopicsList = topicsList;
            notifyDataSetChanged();
        }
    }
}

