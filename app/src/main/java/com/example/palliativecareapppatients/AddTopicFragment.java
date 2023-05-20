package com.example.palliativecareapppatients;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTopicFragment extends Fragment {

    private EditText mTitleEditText, mDescriptionEditText;
    private Button mAddButton;

    private DatabaseReference mDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference("topics");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_topic, container, false);

        mTitleEditText = view.findViewById(R.id.title_edit_text);
        mDescriptionEditText = view.findViewById(R.id.description_edit_text);
        mAddButton = view.findViewById(R.id.add_button);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitleEditText.getText().toString().trim();
                String description = mDescriptionEditText.getText().toString().trim();

                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description)) {
                    addTopic(title, description);
                } else {
                    Toast.makeText(getActivity(), "Please enter title and description", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void addTopic(String title, String description) {
        String topicId = mDatabase.push().getKey();
        Topic topic = new Topic(topicId, title, description, false);
        mDatabase.child(topicId).setValue(topic);

        Toast.makeText(getActivity(), "Topic added", Toast.LENGTH_SHORT).show();
        // إعادة التوجيه إلى قائمة التوبيكات بعد إضافة التوبيك
        navigateToTopicListFragment();
    }

    private void navigateToTopicListFragment() {
        // استدعاء الفراغمنت الحالي بوصفه الفراغمنت الأساسي
        Fragment currentFragment = getParentFragmentManager().findFragmentById(R.id.fragment_container);

        // إنشاء فراغمنت جديد لقائمة التوبيكات
        TopicList topicListFragment = new TopicList();

        // استبدال الفراغمنت الحالي بفراغمنت قائمة التوبيكات
        getParentFragmentManager().beginTransaction()
                .remove(currentFragment)
                .add(R.id.fragment_container, topicListFragment)
                .commit();
    }
}
