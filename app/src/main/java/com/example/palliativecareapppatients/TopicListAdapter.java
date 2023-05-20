package com.example.palliativecareapppatients;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.TopicViewHolder> {

    private List<Topic> mTopics;
    private OnTopicClickListener mListener;

    public interface OnTopicClickListener {
        void onTopicClick(int position);
        void onFollowClick(int position);
    }

    public TopicListAdapter(List<Topic> topics, OnTopicClickListener listener) {
        mTopics = topics;
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
        Topic topic = mTopics.get(position);
        holder.bind(topic);
    }

    @Override
    public int getItemCount() {
        return mTopics.size();
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleTextView;
        private TextView mDescriptionTextView;
        private Button mFollowButton;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitleTextView = itemView.findViewById(R.id.topic_title);
            mDescriptionTextView = itemView.findViewById(R.id.topic_description);
            mFollowButton = itemView.findViewById(R.id.button_follow);

            itemView.setOnClickListener(this);
            mFollowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onFollowClick(position);
                    }
                }
            });
        }

        public void bind(Topic topic) {
            mTitleTextView.setText(topic.getTitle());
            mDescriptionTextView.setText(topic.getDescription());

            // Set the button text based on the follow state
            if (topic.getIs_followed()) {
                mFollowButton.setText("Unfollow");
            } else {
                mFollowButton.setText("Follow");
            }
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                mListener.onTopicClick(position);
            }
        }
    }
}
