package com.example.palliativecareapppatients;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private List<Topic> mTopics;
    private Context mContext;
    private TopicManager mTopicManager;

    public TopicAdapter(List<Topic> topics, Context context, TopicManager topicManager) {
        mTopics = topics;
        mContext = context;
        mTopicManager = topicManager;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_topic, parent, false);
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

    public void setTopics(List<Topic> topics) {
        mTopics = topics;
        notifyDataSetChanged();
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Topic mTopic;

        private TextView mTitleTextView;
        private TextView mDescriptionTextView;
        private Button mFollowButton;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = itemView.findViewById(R.id.topic_title);
            mDescriptionTextView = itemView.findViewById(R.id.topic_description);
     /*       mFollowButton = itemView.findViewById(R.id.follow_button);

            mFollowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTopic != null) {
                        if (mTopic.isFollowed()) {
                            mTopicManager.unfollowTopic(mTopic.getId());
                            mTopic.setFollowed(false);
                            mFollowButton.setText("Follow");
                        } else {
                            mTopicManager.followTopic(mTopic);
                            mTopic.setFollowed(true);
                            mFollowButton.setText("Un follow");
                        }
                    }
                }
            });*/
        }

        public void bind(Topic topic) {
            mTopic = topic;
            mTitleTextView.setText(mTopic.getTitle());
            mDescriptionTextView.setText(mTopic.getDescription());

            if (mTopic.isFollowed()) {
           //     mFollowButton.setText("Un follow");
            } else {
           //     mFollowButton.setText("Follow");
            }
        }

        @Override
        public void onClick(View v) {
            // Handle item click here
        }
    }
}

