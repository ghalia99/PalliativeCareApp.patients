package com.example.palliativecareapppatients;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // Constants for view types
    String authorName="";
    private static final int VIEW_TYPE_TEXT = 1;
    private static final int VIEW_TYPE_PHOTO = 2;
    private static final int VIEW_TYPE_FILE = 3;
    private static final int VIEW_TYPE_VIDEO = 4;
    private FirebaseAnalytics mFirebaseAnalytics;
    private DatabaseReference databaseReference;
    private String userName="";
    private List<Post> postList;
    private Context context;
    private String AuthorName="";

    public PostListAdapter(List<Post> postList, Context context) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        this.postList = postList;
        this.context = context;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

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

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("PostListAdapter", "Before btnEvent");
                btnEvent("post_id", "Post Clicked", "Button");
                Log.d("PostListAdapter", "After btnEvent");

            }
        });
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
        private Button comment;
        private  DatabaseReference userRef;
        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.post_title);
            descriptionTextView = itemView.findViewById(R.id.post_description);
            authorTextView = itemView.findViewById(R.id.post_author);
            timestampTextView = itemView.findViewById(R.id.post_timestamp);
            comment=itemView.findViewById(R.id.button);

        }

        public void bind(Post post) {
            titleTextView.setText(post.getTitle());
            descriptionTextView.setText(post.getDescription());

            // استعراض قاعدة البيانات للحصول على اسم المستخدم وتعيينه في الـ userName
            DatabaseReference userRef = databaseReference.child("users").child(post.getUserId());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        String familyName = dataSnapshot.child("familyName").getValue(String.class);
                        userName = firstName + " " + familyName;
                        authorTextView.setText(userName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("PostListAdapter", "Failed to get user name: " + databaseError.getMessage());
                }
            });

            timestampTextView.setText(formatTimestamp(post.getTimestamp()));
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCommentDialog(post);
                }
            });
        }

    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView photoImageView;
        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView authorTextView;
        private TextView timestampTextView;
        private Button comment;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.post_photo);
            titleTextView = itemView.findViewById(R.id.post_title);
            descriptionTextView = itemView.findViewById(R.id.post_description);
            authorTextView = itemView.findViewById(R.id.post_author);
            timestampTextView = itemView.findViewById(R.id.post_timestamp);
            comment=itemView.findViewById(R.id.button);

        }

        public void bind(Post post) {
            titleTextView.setText(post.getTitle());
            descriptionTextView.setText(post.getDescription());
            // استعراض قاعدة البيانات للحصول على اسم المستخدم وتعيينه في الـ userName
            DatabaseReference userRef = databaseReference.child("users").child(post.getUserId());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        String familyName = dataSnapshot.child("familyName").getValue(String.class);
                        userName = firstName + " " + familyName;
                        authorTextView.setText(userName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("PostListAdapter", "Failed to get user name: " + databaseError.getMessage());
                }
            });

            timestampTextView.setText(formatTimestamp(post.getTimestamp()));

            // Load photo using Glide
            Glide.with(context)
                    .load(post.getImageUrl())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .into(photoImageView);
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCommentDialog(post);
                }
            });

        }
    }

    public class FileViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView authorTextView;
        private TextView timestampTextView;
        private Button comment;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.post_title);
            descriptionTextView = itemView.findViewById(R.id.post_description);
            authorTextView = itemView.findViewById(R.id.post_author);
            timestampTextView = itemView.findViewById(R.id.post_timestamp);
            comment=itemView.findViewById(R.id.button);

        }

        public void bind(Post post) {
            titleTextView.setText(post.getTitle());
            descriptionTextView.setText(post.getDescription());
            // استعراض قاعدة البيانات للحصول على اسم المستخدم وتعيينه في الـ userName
            DatabaseReference userRef = databaseReference.child("users").child(post.getUserId());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        String familyName = dataSnapshot.child("familyName").getValue(String.class);
                        userName = firstName + " " + familyName;
                        authorTextView.setText(userName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("PostListAdapter", "Failed to get user name: " + databaseError.getMessage());
                }
            });

            timestampTextView.setText(formatTimestamp(post.getTimestamp()));
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCommentDialog(post);
                }
            });
            // Track file post viewed
        }
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        private VideoView videoView;
        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView authorTextView;
        private TextView timestampTextView;
        private Button comment;


        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.post_video);
            titleTextView = itemView.findViewById(R.id.post_title);
            descriptionTextView = itemView.findViewById(R.id.post_description);
            authorTextView = itemView.findViewById(R.id.post_author);
            timestampTextView = itemView.findViewById(R.id.post_timestamp);
            comment=itemView.findViewById(R.id.button);
        }

        public void bind(Post post) {
            titleTextView.setText(post.getTitle());
            descriptionTextView.setText(post.getDescription());
            // استعراض قاعدة البيانات للحصول على اسم المستخدم وتعيينه في الـ userName
            DatabaseReference userRef = databaseReference.child("users").child(post.getUserId());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                        String familyName = dataSnapshot.child("familyName").getValue(String.class);
                        userName = firstName + " " + familyName;
                        authorTextView.setText(userName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("PostListAdapter", "Failed to get user name: " + databaseError.getMessage());
                }
            });

            timestampTextView.setText(formatTimestamp(post.getTimestamp()));

            Uri videoUri = Uri.parse(post.getVideoUrl());
            videoView.setVideoURI(videoUri);
            videoView.start();
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCommentDialog(post);
                }
            });


        }
    }

    private String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    public void btnEvent(String id, String name, String contentType) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, contentType);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
    private void showCommentDialog(final Post post) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("أضف تعليقًا");

        // إنشاء واجهة المستخدم لمربع الحوار
        LinearLayout dialogLayout = new LinearLayout(context);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setPadding(16, 16, 16, 16);

        // إنشاء EditText لكتابة التعليق
        final EditText commentEditText = new EditText(context);
        dialogLayout.addView(commentEditText);

        // إنشاء زر لإضافة التعليق
        Button addButton = new Button(context);
        addButton.setText("إضافة تعليق");
        dialogLayout.addView(addButton);

        // إنشاء RecyclerView لعرض التعليقات
        RecyclerView commentsRecyclerView = new RecyclerView(context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        commentsRecyclerView.setLayoutManager(layoutManager);
        List<Comment> comments = new ArrayList<>();
        CommentAdapter commentAdapter = new CommentAdapter(comments);
        commentsRecyclerView.setAdapter(commentAdapter);

        // إضافة واجهة المستخدم لمربع الحوار
        builder.setView(dialogLayout);

        // إعداد الزر لإضافة التعليق
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String userId = auth.getCurrentUser().getUid();
                String content = commentEditText.getText().toString();

                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String firstName = dataSnapshot.child("firstName").getValue(String.class);
                            String familyName = dataSnapshot.child("familyName").getValue(String.class);
                            String authorName = firstName + " " + familyName;
                            Log.d("name",authorName);
                            Comment comment = new Comment(userId,authorName,content);
                            databaseReference.child("posts").child(post.getPostId()).child("comment").push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d("comment","Comment Add Successful");
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("اسم الكاتب", "فشل في الحصول على اسم المستخدم: " + databaseError.getMessage());
                    }
                });

                Log.d("name",authorName);


            }

        });

        databaseReference.child("posts").child(post.getPostId()).child("comment").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    comments.clear(); // قم بمسح القائمة قبل إضافة التعليقات الجديدة
                    for (DataSnapshot commentSnapshot : snapshot.getChildren()) {
                        Comment comment = commentSnapshot.getValue(Comment.class);
                        comments.add(comment);
                    }
                    commentEditText.setText("");
                    commentAdapter.notifyDataSetChanged(); // قم بتحديث RecyclerView

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // معالجة الخطأ
            }
        });


        builder.setNegativeButton("خروج", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialogLayout.removeView(commentsRecyclerView); // إزالة `commentsRecyclerView` من `dialogLayout`
        dialogLayout.addView(commentsRecyclerView); // إضافة `commentsRecyclerView` بعد إزالته

        builder.create().show();
    }

}
