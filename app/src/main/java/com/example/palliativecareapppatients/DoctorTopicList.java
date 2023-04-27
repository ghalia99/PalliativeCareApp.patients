package com.example.palliativecareapppatients;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DoctorTopicList extends Fragment  {

    private TopicListAdapter mAdapter;
    private List<Topic> mTopics = new ArrayList<>();

    private FirebaseFirestore mFirestore;
    DatabaseReference myRef;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("topics");
        // Topic topic1 = new Topic("-NRwYqhCaVja7rxLjDAx", "قلب", "....",true);
        //Topic topic2 = new Topic("-NRwYqhCaVja7rxLjDBx", "رئة", "....",true);
        // Topic topic3 = new Topic("-NRwYqhCaVja7rxLjDCx", "كبد", "....",false);
        //   myRef.child(topic1.getId()).setValue(topic1);
        //  myRef.child(topic2.getId()).setValue(topic2);
        //  myRef.child(topic3.getId()).setValue(topic3);

        // Initialize Firestore
        mFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic_list, container, false);

        // Initialize RecyclerView
        RecyclerView mRecyclerView = view.findViewById(R.id.topic_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize adapter
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
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemViewCacheSize(10);
        if (myRef != null) {
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mTopics.clear(); // Clear the existing topics list before adding new data
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        // Fetch data from childSnapshot and create a Topic object

                        String id = childSnapshot.getKey();
                        String   title = childSnapshot.child("title").getValue(String.class);
                        String    description = childSnapshot.child("description").getValue(String.class);
                        boolean   is_followed = childSnapshot.child("is_followed").getValue(Boolean.class);

                        Topic topic = new Topic(id, title, description, is_followed);
                        mTopics.add(topic);
                        mAdapter.notifyDataSetChanged();

                    }

                    mAdapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });}




        return view;
    }

}
