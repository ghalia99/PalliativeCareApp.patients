package com.example.palliativecareapppatients;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.TopicViewHolder> {

    private List<Topic> mTopics;

    public TopicListAdapter(List<Topic> topics) {
        mTopics = topics;
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
        Button followButton = holder.itemView.findViewById(R.id.button_follow);
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the topic ID from the currently clicked item
                int position = holder.getAbsoluteAdapterPosition();
                Topic topic = mTopics.get(position);
                String topicId = topic.getId();

                // Update the text of the button
                String buttonText = ((Button) v).getText().toString();
                if (buttonText.equals("Follow")) {
                    ((Button) v).setText("Unfollow");
                    // Save "follow" status as true in the database
                    saveFollowStatusToDatabase(topicId, true);
                } else if (buttonText.equals("Unfollow")) {
                    ((Button) v).setText("Follow");
                    // Save "follow" status as false in the database
                    saveFollowStatusToDatabase(topicId, false);
                }
            }
        });
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
    private void saveFollowStatusToDatabase(String topicId, boolean isFollowed) {
        DatabaseReference topicsRef = FirebaseDatabase.getInstance().getReference().child("topics").child(topicId);

        topicsRef.child("is_followed").setValue(isFollowed)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Update successful
                            Log.d("---", "Follow status updated in database");
                        } else {
                            // Update failed
                            Log.e("TAG", "Failed to update follow status in database: " + task.getException());
                        }
                    }
                });
    }
}
