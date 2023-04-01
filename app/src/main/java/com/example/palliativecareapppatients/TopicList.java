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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopicList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopicList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TopicList() {
        // Required empty public constructor
    }




        private RecyclerView mTopicRecyclerView;
        private TopicAdapter mAdapter;

        private TopicManager mTopicManager;



        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mTopicManager = new TopicManager(PatientInfo.getInstance().getId());
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_topic_list, container, false);

            mTopicRecyclerView = view.findViewById(R.id.topic_list);
            mTopicRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            updateUI();

            return view;
        }

        private void updateUI() {
            mTopicManager.getTopicByName(new TopicManager.TopicListCallback() {
                @Override
                public void onTopicListReceived(List<Topic> topics) {
                    if (mAdapter == null) {
                        mAdapter = new TopicAdapter(topics);
                        mTopicRecyclerView.setAdapter(mAdapter);
                    } else {
                        mAdapter.setTopics(topics);
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Error getting topics", e);
                }
            });
        }

        private class TopicHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private Topic mTopic;

            private TextView mTitleTextView;
            private TextView mDescriptionTextView;
            private Button mFollowButton;

            public TopicHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.list_item_topic, parent, false));
                itemView.setOnClickListener(this);

                mTitleTextView = itemView.findViewById(R.id.topic_title);
                mDescriptionTextView = itemView.findViewById(R.id.topic_description);
                mFollowButton = itemView.findViewById(R.id.follow_button);
            }

            public void bind(Topic topic) {
                mTopic = topic;
                mTitleTextView.setText(mTopic.getTitle());
                mDescriptionTextView.setText(mTopic.getDescription());

                mTopicManager.isFollowingTopic(mTopic.getId(), new TopicManager.TopicCallback() {
                    @Override
                    public void onResult(boolean isFollowing) {

                    }

                    @Override
                    public void onTopicReceived(Topic topic) {
                        if (topic != null) {
                            mFollowButton.setText("Un Follow");
                        } else {
                            mFollowButton.setText("Follow");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error checking if following topic", e);
                    }
                });

                mFollowButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mFollowButton.getText().equals("Follow")) {
                            mTopicManager.followTopic(mTopic);
                            mFollowButton.setText("UN follow");
                        } else {
                            mTopicManager.unfollowTopic(mTopic.getId());
                            mFollowButton.setText("Follow");
                        }
                    }
                });
            }

            @Override
            public void onClick(View view) {
                // Handle click on the topic
            }
        }


    }
