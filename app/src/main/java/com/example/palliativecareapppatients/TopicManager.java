package com.example.palliativecareapppatients;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TopicManager {
    private static final String TAG = TopicManager.class.getSimpleName();

    private FirebaseFirestore db;
    private String usersId;

    public interface TopicListCallback {
        void onCallback(List<Topic> topics);

        void onTopicListReceived(List<Topic> topics);

        void onFailure(@NonNull Exception e);
    }

    public interface TopicCallback {
        void onCallback(Topic topic);

        void onResult(boolean isFollowing);

        void onTopicReceived(Topic topic);

        void onFailure(@NonNull Exception e);
    }

    public TopicManager(String patientId) {
        this.db = FirebaseFirestore.getInstance();
        this.usersId = patientId;
    }

    public void followTopic(Topic topic) {
        db.collection("users").document(usersId)
                .collection("topics").document(topic.getId())
                .set(topic)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Topic followed successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error following topic", e);
                    }
                });
    }

    public void unfollowTopic(String topicId) {
        db.collection("users").document(usersId)
                .collection("topics").document(topicId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Topic unfollowed successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error unfollowing topic", e);
                    }
                });
    }

    public void isFollowingTopic(String topicId, TopicCallback callback) {
        db.collection("users").document(usersId)
                .collection("topics").document(topicId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Topic topic = documentSnapshot.toObject(Topic.class);
                     /*   if (topic != null && topic.isFollowed()) {
                            callback.onCallback(topic);
                        } else {
                            callback.onCallback(null);
                        }*/
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error getting topic", e);
                        callback.onCallback(null);
                    }
                });
    }

    public void getTopics(TopicListCallback callback) {
        db.collection("topics")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        List<Topic> topics = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                            Topic topic = documentSnapshot.toObject(Topic.class);
                            if (topic != null) {
                                topics.add(topic);
                            }
                        }
                        callback.onCallback(topics);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error getting topics", e);
                        callback.onCallback(null);
                    }
                });
    }

  /*  This code retrieves the "topics" node that is nested under the "patient" node, and then adds a ValueEventListener to retrieve the topic data.

    Also, make sure to modify the Topic class to include the fields that you want to display in the listTopic fragment, and modify the TopicAdapter and list_item_topic layout accordingly as mentioned in the previous answer.


*/




}
