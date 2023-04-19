package com.example.palliativecareapppatients;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends Fragment {

    private String topicId;
    private String topicTitle;
    private List<Post> mPosts = new ArrayList<>();
    private PostsListAdapter mAdapter;

    private FirebaseFirestore mFirestore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference  myRef = database.getReference("posts");
        Post post1 = new Post("1", topicId,"أمراض الكبد وأعراضها", "C:\\Users\\HP\\Documents\\صور المشروع\\أعراض.jpg", "", "", "", "دكتور أحمد", "2023-04-19");
        post1.setType(Post.TYPE_TEXT); // تعيين نوع المنشور إلى نص
          myRef.child(post1.getPostId()).setValue(post1);

        Post post2 = new Post("2",topicId, "", "", "https://www.mayoclinic.org/ar/diseases-conditions/cirrhosis/symptoms-causes/syc-20351487", "", "", "دكتورة سارة", "2023-04-18");
        post2.setType(Post.TYPE_VIDEO); // تعيين نوع المنشور إلى فيديو
        myRef.child(post2.getPostId()).setValue(post2);

        Post post3 = new Post("3",topicId, "", "", "", "https://twitter.com/i/status/1257416051152892065", "", "دكتور محمد", "2023-04-17");
        post3.setType(Post.TYPE_INFOGRAPHIC); // تعيين نوع المنشور إلى إنفوجرافيك
        myRef.child(post3.getPostId()).setValue(post3);



        // Initialize Firestore
        mFirestore = FirebaseFirestore.getInstance();

        // Retrieve topic information from arguments
        if (getArguments() != null) {
            topicId = getArguments().getString("topicId");
            topicTitle = getArguments().getString("topicTitle");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);

        // Set the title of the fragment to the topic title
        if (getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(topicTitle);
        }

        // Initialize RecyclerView
        RecyclerView mRecyclerView = view.findViewById(R.id.posts_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize adapter
        mAdapter = new PostsListAdapter(mPosts,getActivity());

        mRecyclerView.setAdapter(mAdapter);

        // Fetch posts from database
        fetchPostsFromDatabase(topicId);

        return view;
    }

    private void fetchPostsFromDatabase(String topicId) {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("posts");
        Query query = postsRef.orderByChild("topicId").equalTo(topicId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Post> posts = new ArrayList<>();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    posts.add(post);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to fetch posts from database: " + error.getMessage());
            }
        });
    }
}
