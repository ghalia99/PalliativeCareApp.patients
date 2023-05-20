package com.example.palliativecareapppatients;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_TEXT = 1;
    private static final int VIEW_TYPE_PHOTO = 2;
    private static final int VIEW_TYPE_FILE = 3;
    private static final int VIEW_TYPE_VIDEO = 4;

    private List<Post> postList;
    private Context context;

    public PostListAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @Override
    public int getItemViewType(int position) {
        Post post = postList.get(position);
        if (post.getVideoUrl() != null && !post.getVideoUrl().isEmpty()) {
            return VIEW_TYPE_VIDEO;
        } else if (post.getFileUrl() != null && !post.getFileUrl().isEmpty()) {
            return VIEW_TYPE_FILE;
        } else if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            return VIEW_TYPE_PHOTO;
        } else {
            return VIEW_TYPE_TEXT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType) {
            case VIEW_TYPE_TEXT:
                View textView = inflater.inflate(R.layout.post_text_item, parent, false);
                return new TextViewHolder(textView);
            case VIEW_TYPE_PHOTO:
                View photoView = inflater.inflate(R.layout.post_photo_item, parent, false);
                return new PhotoViewHolder(photoView);
            case VIEW_TYPE_FILE:
                View fileView = inflater.inflate(R.layout.post_file_item, parent, false);
                return new FileViewHolder(fileView);
            case VIEW_TYPE_VIDEO:
                View videoView = inflater.inflate(R.layout.post_video_item, parent, false);
                return new VideoViewHolder(videoView);
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Post post = postList.get(position);

        switch (viewHolder.getItemViewType()) {
            case VIEW_TYPE_TEXT:
                TextViewHolder textViewHolder = (TextViewHolder) viewHolder;
                textViewHolder.bind(post);
                break;
            case VIEW_TYPE_PHOTO:
                PhotoViewHolder photoViewHolder = (PhotoViewHolder) viewHolder;
                photoViewHolder.bind(post);
                break;
            case VIEW_TYPE_FILE:
                FileViewHolder fileViewHolder = (FileViewHolder) viewHolder;
                fileViewHolder.bind(post);
                break;
            case VIEW_TYPE_VIDEO:
                VideoViewHolder videoViewHolder = (VideoViewHolder) viewHolder;
                videoViewHolder.bind(post);
                break;
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView authorTextView;
        private TextView timestampTextView;

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.post_title);
            descriptionTextView = itemView.findViewById(R.id.post_description);
            authorTextView = itemView.findViewById(R.id.post_author);
            timestampTextView = itemView.findViewById(R.id.post_timestamp);
        }

        public void bind(Post post) {
            titleTextView.setText(post.getTitle());
            descriptionTextView.setText(post.getDescription());
            authorTextView.setText(post.getAuthorId());
            timestampTextView.setText(Utils.formatTimestamp(post.getTimestamp()));
        }
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView descriptionTextView;
        private ImageView photoImageView;
        private TextView authorTextView;
        private TextView timestampTextView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.post_title);
            descriptionTextView = itemView.findViewById(R.id.post_description);
            photoImageView = itemView.findViewById(R.id.post_photo);
            authorTextView = itemView.findViewById(R.id.post_author);
            timestampTextView = itemView.findViewById(R.id.post_timestamp);
        }

        public void bind(Post post) {
            titleTextView.setText(post.getTitle());
            descriptionTextView.setText(post.getDescription());
            authorTextView.setText(post.getAuthorId());
            timestampTextView.setText(Utils.formatTimestamp(post.getTimestamp()));

            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_topic)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(context)
                    .load(post.getImageUrl())
                    .apply(options)
                    .into(photoImageView);
        }
    }

    public class FileViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView fileUrlTextView;
        private TextView authorTextView;
        private TextView timestampTextView;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.post_title);
            descriptionTextView = itemView.findViewById(R.id.post_description);
            fileUrlTextView = itemView.findViewById(R.id.post_file_url);
            authorTextView = itemView.findViewById(R.id.post_author);
            timestampTextView = itemView.findViewById(R.id.post_timestamp);
        }

        public void bind(Post post) {
            titleTextView.setText(post.getTitle());
            descriptionTextView.setText(post.getDescription());
            fileUrlTextView.setText(post.getFileUrl());
            authorTextView.setText(post.getAuthorId());
            timestampTextView.setText(Utils.formatTimestamp(post.getTimestamp()));
        }
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView authorTextView;
        private TextView timestampTextView;
        private VideoView videoView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.post_title);
            descriptionTextView = itemView.findViewById(R.id.post_description);
            authorTextView = itemView.findViewById(R.id.post_author);
            timestampTextView = itemView.findViewById(R.id.post_timestamp);
            videoView = itemView.findViewById(R.id.post_video);
        }

        public void bind(Post post) {
            titleTextView.setText(post.getTitle());
            descriptionTextView.setText(post.getDescription());
            authorTextView.setText(post.getAuthorId());
            timestampTextView.setText(Utils.formatTimestamp(post.getTimestamp()));

            Uri videoUri = Uri.parse(post.getVideoUrl());
            videoView.setVideoURI(videoUri);
            videoView.requestFocus();
            videoView.start();
        }
    }


    public static class Utils {
        public static String formatTimestamp(long timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
            return sdf.format(new Date(timestamp));
        }
    }

}
