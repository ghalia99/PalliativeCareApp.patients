package com.example.palliativecareapppatients;

import androidx.fragment.app.Fragment;

public class PostsFragment extends Fragment {

  /*  private String topicId;
    private String topicTitle;
    private List<Post> mPosts = new ArrayList<>();
    private PostsListAdapter mAdapter;

    private FirebaseFirestore mFirestore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference  myRef = database.getReference("posts");




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
    }*/
}
