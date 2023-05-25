package com.example.palliativecareapppatients;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddPostActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 101;
    private static final int REQUEST_CODE_PICK_VIDEO = 102;
    private static final int REQUEST_CODE_PICK_FILE = 103;

    private EditText etTitle, etDescription;
    private ImageView ivPostImage;
    private Button btnAddImage, btnAddVideo, btnAddFile, btnAddPost;
    private Spinner dropdownList;
    private Uri postImageUri, postVideoUri, postFileUri;
    private VideoView ivPostVideo;
    private StorageReference storageRef;
    private DatabaseReference databaseRef;
    private FirebaseAuth user;
    private String postTopicId="";
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;
    private ProgressDialog progressDialog;
    private long timestamp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        etTitle = findViewById(R.id.et_post_title);
        etDescription = findViewById(R.id.et_post_description);
        ivPostImage = findViewById(R.id.iv_post_image);
        ivPostVideo = findViewById(R.id.iv_post_video);
        btnAddImage = findViewById(R.id.btn_add_image);
        btnAddVideo = findViewById(R.id.btn_add_video);
        btnAddFile = findViewById(R.id.btn_add_file);
        btnAddPost = findViewById(R.id.btn_add_post);
        dropdownList =findViewById(R.id.sp_topic);
        storageRef = FirebaseStorage.getInstance().getReference("uploads");
        databaseRef = FirebaseDatabase.getInstance().getReference("posts");
        user = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");

        btnAddImage.setOnClickListener(v -> openGallery(REQUEST_CODE_PICK_IMAGE));

        btnAddVideo.setOnClickListener(v -> openGallery(REQUEST_CODE_PICK_VIDEO));

        btnAddFile.setOnClickListener(v -> openGallery(REQUEST_CODE_PICK_FILE));

        btnAddPost.setOnClickListener(v -> {
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                addPost();
            }
        });
        DatabaseReference topicsRef = FirebaseDatabase.getInstance().getReference("topics");
        Query query = topicsRef.orderByChild("title");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> topics = new ArrayList<>();
                for (DataSnapshot topicSnapshot : dataSnapshot.getChildren()) {
                    Topic topic = topicSnapshot.getValue(Topic.class);
                    if (topic != null) {
                        topics.add(topic.getTitle());
                        for (int i =0;i <topics.size();i++){
                            System.out.println(topics);
                        }
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddPostActivity.this, android.R.layout.simple_spinner_dropdown_item, topics);
                dropdownList.setAdapter(adapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

// Set a listener to update the topicId field of the post when a topic is selected
        dropdownList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTopicName = (String) parent.getItemAtPosition(position);

                // Query the topics node to get the ID of the selected topic
                Query query = topicsRef.orderByChild("title").equalTo(selectedTopicName);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            DataSnapshot topicSnapshot = dataSnapshot.getChildren().iterator().next();
                            String topicId = topicSnapshot.getKey();

                            // Set the topicId field of the post to the ID of the selected topic
                          postTopicId= topicId;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle error
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

    }

    private void openGallery(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            intent.setType("image/*");
        } else if (requestCode == REQUEST_CODE_PICK_VIDEO) {
            intent.setType("video/*");
        } else if (requestCode == REQUEST_CODE_PICK_FILE) {
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"application/pdf", "application/msword"});
        }
        startActivityForResult(intent, requestCode);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
            if (requestCode == REQUEST_CODE_PICK_IMAGE) {
                ivPostImage.setImageURI(uri);
                postImageUri = uri;
                ivPostImage.setVisibility(View.VISIBLE);
            } else if (requestCode == REQUEST_CODE_PICK_VIDEO) {
                ivPostVideo.setVideoURI(uri);
                postVideoUri = uri;
                ivPostVideo.setVisibility(View.VISIBLE);
            } else if (requestCode == REQUEST_CODE_PICK_FILE) {
                postFileUri = uri;
                Toast.makeText(this, "File selected: " + uri.getLastPathSegment(), Toast.LENGTH_SHORT).show();
            }
            }
        }
    }

    private void addPost() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        if (postImageUri != null) {
            uploadFile(postImageUri, "image");
        } else if (postVideoUri != null) {
            uploadFile(postVideoUri, "video");
        } else {
            uploadPost("", "", "");
        }
    }

    private void uploadFile(Uri fileUri, String fileType) {
        StorageReference fileRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(fileUri));

        uploadTask = fileRef.putFile(fileUri);
        uploadTask.continueWithTask((Continuation<UploadTask.TaskSnapshot, Task<Uri>>) task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return fileRef.getDownloadUrl();
        }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                if (fileType.equals("image")) {
                    uploadPost(downloadUri.toString(), null, null);
                } else if (fileType.equals("video")) {
                    uploadPost(null, downloadUri.toString(), null);
                }
            } else {
                Toast.makeText(AddPostActivity.this, "Upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadPost(String imageUrl, String videoUrl, String fileUrl) {
        String postId = databaseRef.push().getKey();
        String userId = user.getCurrentUser().getUid();
        long currentTime = System.currentTimeMillis();

        HashMap<String, Object> postMap = new HashMap<>();
        postMap.put("postId", postId);
        postMap.put("userId", userId);
        postMap.put("title", etTitle.getText().toString());
        postMap.put("description", etDescription.getText().toString());
        postMap.put("imageUrl", imageUrl);
        postMap.put("videoUrl", videoUrl);
        postMap.put("fileUrl", fileUrl);
        postMap.put("topicId",postTopicId);
        postMap.put("timestamp", currentTime);


        databaseRef.child(postId).setValue(postMap).addOnCompleteListener(task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                Toast.makeText(AddPostActivity.this, "Post added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AddPostActivity.this, "Post upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
