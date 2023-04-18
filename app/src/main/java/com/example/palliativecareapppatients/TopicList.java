package com.example.palliativecareapppatients;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class TopicList extends Fragment {

    private TopicListAdapter mAdapter;
    private List<Topic> mTopics = new ArrayList<>();

    private FirebaseFirestore mFirestore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("topics");
        Topic topic1 = new Topic("-NRwYqhCaVja7rxLjDAx", "قلب", "....");
        Topic topic2 = new Topic("-NRwYqhCaVja7rxLjDBx", "رئة", "....");
        Topic topic3 = new Topic("-NRwYqhCaVja7rxLjDCx", "كبد", "....");
        myRef.child(topic1.getId()).setValue(topic1);
        myRef.child(topic2.getId()).setValue(topic2);
        myRef.child(topic3.getId()).setValue(topic2);

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
        mAdapter = new TopicListAdapter(mTopics);
        mRecyclerView.setAdapter(mAdapter);

        // Load topics from Firestore
        mFirestore.collection("topics").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mTopics.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Topic topic = document.toObject(Topic.class);
                    mTopics.add(topic);
                }
                mAdapter.notifyDataSetChanged();
            } else {
                // Handle error
            }
        });

        return view;
    }
}
