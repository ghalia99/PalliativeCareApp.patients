package com.example.palliativecareapppatients;

/*
public class PostsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Post> postsList;
    private Context context;

    public PostsListAdapter(List<Post> postsList, Context context) {
        this.postsList = postsList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate different layouts based on the type of post
        switch (viewType) {
            case Post.TYPE_TEXT:
                view = inflater.inflate(R.layout.item_post_text, parent, false);
                viewHolder = new TextPostViewHolder(view);
                break;
            case Post.TYPE_VIDEO:
                view = inflater.inflate(R.layout.item_post_video, parent, false);
                viewHolder = new VideoPostViewHolder(view);
                break;
            case Post.TYPE_INFOGRAPHIC:
                view = inflater.inflate(R.layout.item_post_infographic, parent, false);
                viewHolder = new InfographicPostViewHolder(view);
                break;
            case Post.TYPE_PDF:
                view = inflater.inflate(R.layout.item_post_pdf, parent, false);
                viewHolder = new PdfPostViewHolder(view);
                break;
            default:
                view = inflater.inflate(R.layout.item_post_default, parent, false);
                viewHolder = new DefaultPostViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Post post = postsList.get(position);

        // Bind data to appropriate view holders based on the type of post
        if (holder instanceof TextPostViewHolder) {
            ((TextPostViewHolder) holder).bind(post);
        } else if (holder instanceof VideoPostViewHolder) {
            ((VideoPostViewHolder) holder).bind(post);
        } else if (holder instanceof InfographicPostViewHolder) {
            ((InfographicPostViewHolder) holder).bind(post);
        } else if (holder instanceof PdfPostViewHolder) {
            ((PdfPostViewHolder) holder).bind(post);
        } else {
            ((DefaultPostViewHolder) holder).bind(post);
        }
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return postsList.get(position);
    }

    // View holder for text type post
    static class TextPostViewHolder extends RecyclerView.ViewHolder {
        private TextView doctorNameTextView;
        private TextView postTitleTextView;
        private TextView postContentTextView;

        public TextPostViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorNameTextView = itemView.findViewById(R.id.doctor_Name);
            postTitleTextView = itemView.findViewById(R.id.post_title);
            postContentTextView = itemView.findViewById(R.id.post_content);
        }

        public void bind(Post post) {
            doctorNameTextView.setText(post.getAuthorId());
            postTitleTextView.setText(post.getTitle());
            postContentTextView.setText(post.getDescription());
        }
    }



        // View holder for video type post
    static class VideoPostViewHolder extends RecyclerView.ViewHolder {
        private TextView doctorNameTextView;
        private TextView postTitleTextView;
        private VideoView postVideoPlayerView;

        public VideoPostViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorNameTextView = itemView.findViewById(R.id.doctor_Name);
            postTitleTextView = itemView.findViewById(R.id.post_title);
            postVideoPlayerView = itemView.findViewById(R.id.post_video_player);
        }

        public void bind(Post post) {
            doctorNameTextView.setText(post.getTitle());
            postTitleTextView.setText(post.getDescription());
            // Set video URI to the VideoView
            postVideoPlayerView.setVideoURI(Uri.parse(post.getVideoUrl()));
            // Start video playback
            postVideoPlayerView.start();
        }
    }

    // View holder for infographic type post
    static class InfographicPostViewHolder extends RecyclerView.ViewHolder {
        private TextView doctorNameTextView;
        private TextView postTitleTextView;
        private ImageView postImageView;

        public InfographicPostViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorNameTextView = itemView.findViewById(R.id.doctor_Name);
            postTitleTextView = itemView.findViewById(R.id.post_title);
            postImageView = itemView.findViewById(R.id.post_infographic_image);
        }

        public void bind(Post post) {
            doctorNameTextView.setText(post.getAuthorId());
            postTitleTextView.setText(post.getTitle());
            // Load infographic image using image loading library like Glide or Picasso
            // For example:
            Glide.with(itemView.getContext())
                    .load(post.getImageUrl())
                    .into(postImageView);
        }
    }

    // View holder for PDF type post
    static class PdfPostViewHolder extends RecyclerView.ViewHolder {
        private TextView doctorNameTextView;
        private TextView postTitleTextView;
        private ImageView postPdfImageView;

        public PdfPostViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorNameTextView = itemView.findViewById(R.id.doctor_Name);
            postTitleTextView = itemView.findViewById(R.id.post_title);
            postPdfImageView = itemView.findViewById(R.id.post_pdf_image);
        }

        public void bind(Post post) {
            doctorNameTextView.setText(post.getAuthorId());
            postTitleTextView.setText(post.getTitle());
            // Load PDF image using image loading library like Glide or Picasso
            // For example:
            Glide.with(itemView.getContext())
                    .load(post.getFileUrl())
                    .into(postPdfImageView);
        }
    }

    // View holder for default type post
    static class DefaultPostViewHolder extends RecyclerView.ViewHolder {
        private TextView doctorNameTextView;
        private TextView postTitleTextView;

        public DefaultPostViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorNameTextView = itemView.findViewById(R.id.doctor_Name);
            postTitleTextView = itemView.findViewById(R.id.post_title);
        }

        public void bind(Post post) {
            doctorNameTextView.setText(post.getAuthorId());
            postTitleTextView.setText(post.getDescription());
        }
    }
}*/
