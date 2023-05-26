package com.example.palliativecareapppatients;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    TextView userProfileName ;
    TextView userStatus ;
    CircularImageView userProfileImage ;
    ImageView userOnlineStatus;
    Button acceptButton ;
    Button cancelButton ;
    VideoView postVideo ;
    ImageView postPhoto ;
    TextView authorName ;
    TextView date ;
    Button commentButton ;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
         userProfileName = findViewById(R.id.users_profile_name);
         userStatus = findViewById(R.id.users_status);
         userProfileImage = findViewById(R.id.users_profile_image);
         userOnlineStatus = findViewById(R.id.users_online_status);
         acceptButton = findViewById(R.id.request_accept_button);
         cancelButton = findViewById(R.id.request_cancel_button);
         postVideo = findViewById(R.id.post_video);
         postPhoto = findViewById(R.id.post_photo);
         authorName = findViewById(R.id.author);
         date = findViewById(R.id.Date);
         commentButton = findViewById(R.id.comment_button);
        Intent intent = getIntent();

        if (intent.hasExtra("postId")) {

            String postId = intent.getStringExtra("postId");
            if (postId!=null) {
                reference = FirebaseDatabase.getInstance().getReference();
                reference.child("posts").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Post post = snapshot.getValue(Post.class);
                            userProfileName.setVisibility(View.VISIBLE);
                            userProfileName.setText(post.getTitle());
                            userStatus.setVisibility(View.VISIBLE);
                            userStatus.setText(post.getDescription());
                            if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {


                                Log.d("DetailActivity", "post.getImageUrl() " + post.getImageUrl());

                                Glide.with(DetailActivity.this)
                                        .load(post.getImageUrl())
                                        .apply(new RequestOptions()
                                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                                        .into(postPhoto);
                                postPhoto.setVisibility(View.VISIBLE);

                            }
                            if (post.getVideoUrl() != null && !post.getVideoUrl().isEmpty()) {
                                Log.d("DetailActivity", "getVideoUrl()  " + post.getVideoUrl());

                                postVideo.setVisibility(View.VISIBLE);
                                Uri videoUri = Uri.parse(post.getVideoUrl());
                                postVideo.setVideoURI(videoUri);
                                postVideo.start();
                            }
                            if (post.getFileUrl() != null && !post.getFileUrl().isEmpty()) {
                                Log.d("DetailActivity", "getFileUrl() " + post.getFileUrl());

                                postPhoto.setVisibility(View.VISIBLE);
                                Glide.with(DetailActivity.this)
                                        .load(post.getImageUrl())
                                        .apply(new RequestOptions()
                                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                                        .into(postPhoto);
                            }
                            date.setText(formatTimestamp(post.getTimestamp()));
                            date.setVisibility(View.VISIBLE);

                            DatabaseReference userRef = reference.child("users").child(post.getUserId());
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        String firstName = dataSnapshot.child("firstName").getValue(String.class);
                                        String familyName = dataSnapshot.child("familyName").getValue(String.class);
                                        if (firstName != null || familyName != null) {
                                            String userName = firstName + " " + familyName;
                                            authorName.setText(userName);
                                            authorName.setVisibility(View.VISIBLE);
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.d("PostListAdapter", "Failed to get user name: " + databaseError.getMessage());
                                }
                            });
                            commentButton.setVisibility(View.VISIBLE);
                            commentButton.setText("تعليق");
                            commentButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                                    builder.setTitle("أضف تعليقًا");

                                    // إنشاء واجهة المستخدم لمربع الحوار
                                    LinearLayout dialogLayout = new LinearLayout(DetailActivity.this);
                                    dialogLayout.setOrientation(LinearLayout.VERTICAL);
                                    dialogLayout.setPadding(16, 16, 16, 16);

                                    // إنشاء EditText لكتابة التعليق
                                    final EditText commentEditText = new EditText(DetailActivity.this);
                                    dialogLayout.addView(commentEditText);

                                    // إنشاء زر لإضافة التعليق
                                    Button addButton = new Button(DetailActivity.this);
                                    addButton.setText("إضافة تعليق");
                                    dialogLayout.addView(addButton);

                                    // إنشاء RecyclerView لعرض التعليقات
                                    RecyclerView commentsRecyclerView = new RecyclerView(DetailActivity.this);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(DetailActivity.this);
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
                                                        Log.d("name", authorName);
                                                        Comment comment = new Comment(userId, authorName, content);
                                                        reference.child("posts").child("comment").push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Log.d("comment", "Comment Add Successful");
                                                            }
                                                        });
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    Log.d("اسم الكاتب", "فشل في الحصول على اسم المستخدم: " + databaseError.getMessage());
                                                }
                                            });


                                        }

                                    });

                                    reference.child("posts").child(post.getPostId()).child("comment").addListenerForSingleValueEvent(new ValueEventListener() {
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
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        } else if (intent.hasExtra("topicId")) {

            String topicId = intent.getStringExtra("topicId");
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String userId = auth.getCurrentUser().getUid();
            reference = FirebaseDatabase.getInstance().getReference();
            reference.child("topics").child(topicId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Topic topic = snapshot.getValue(Topic.class);
                        userProfileName.setVisibility(View.VISIBLE);
                        userProfileName.setText(topic.getTitle());
                        userStatus.setVisibility(View.VISIBLE);
                        userStatus.setText(topic.getDescription());
                        commentButton.setVisibility(View.VISIBLE);
                        commentButton.setText("");
                        commentButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                DatabaseReference followingRef =  reference.child("users").child(userId).child("followingTopic");
                                followingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot followingSnapshot) {
                                        for (DataSnapshot topicSnapshot : followingSnapshot.getChildren()) {

                                            Boolean isFollowed = topicSnapshot.getValue(Boolean.class);
                                            if (isFollowed != null) {
                                                String topicId = topicSnapshot.getKey();
                                                Topic topic = new Topic();
                                                topic.setId(topicId);
                                                topic.setIs_followed(isFollowed);

                                                // Add the topic object to your list or perform any necessary operations
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.d("DoctorTopicList", databaseError.getMessage());
                                    }
                                });

                                boolean isFollowed = topic.getIs_followed();
                                topic.setIs_followed(!isFollowed);

                                if (!isFollowed) {
                                    // Add the topic ID to the user's followingTopic list
                                    followingRef.child(topicId).setValue(true)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                   commentButton.setText("الغاء المتابعة");
                                                    commentButton.setVisibility(View.VISIBLE);

                                                    Log.d("DoctorTopicList", "Topic added to followingTopic");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Update failed
                                                    Log.d("DoctorTopicList", "Failed to add topic to followingTopic: " + e.getMessage());
                                                }
                                            });
                                } else {
                                    // Remove the topic ID from the user's followingTopic list
                                    followingRef.child(topicId).removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    commentButton.setText("متابعة");
                                                    Log.d("DoctorTopicList", "Topic removed from followingTopic");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Update failed
                                                    Log.d("DoctorTopicList", "Failed to remove topic from followingTopic: " + e.getMessage());
                                                }
                                            });
                                }
                            }
                        });
                    }
                }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

         else if (intent.hasExtra("userId")) {

            String userId = intent.getStringExtra("userId");
            reference = FirebaseDatabase.getInstance().getReference();
            reference.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        User user = snapshot.getValue(User.class);
                        userProfileName.setVisibility(View.VISIBLE);
                        userProfileName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String visit_user_id=userId;
                                Intent profileintent=new Intent(DetailActivity.this,ProfileActivity.class);
                                profileintent.putExtra("visit_user_id",visit_user_id);
                                startActivity(profileintent);
                            }
                        });
                        userProfileName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String visit_user_id=userId;
                                Intent profileintent=new Intent(DetailActivity.this,ProfileActivity.class);
                                profileintent.putExtra("visit_user_id",visit_user_id);
                                startActivity(profileintent);
                            }
                        });
                        userProfileName.setText(user.getFirstName() + " " + user.getMiddleName() + " " + user.getFamilyName());
                        userStatus.setVisibility(View.VISIBLE);

                        userStatus.setText(snapshot.child("userState").child("state").getValue(String.class));
                        if (snapshot.child("userState").child("state").getValue(String.class)=="online"){
                        userOnlineStatus.setVisibility(View.VISIBLE);}
                        userProfileImage.setVisibility(View.VISIBLE);
                        Picasso.get().load(snapshot.child("profilePhotoUrl").getValue(String.class)).placeholder(R.drawable.profile_image).into(userProfileImage);
                        userProfileImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String visit_user_id=userId;
                                Intent profileintent=new Intent(DetailActivity.this,ProfileActivity.class);
                                profileintent.putExtra("visit_user_id",visit_user_id);
                                startActivity(profileintent);
                            }
                        });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
                private String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));

}
}