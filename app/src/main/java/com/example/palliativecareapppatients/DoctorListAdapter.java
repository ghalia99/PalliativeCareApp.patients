package com.example.palliativecareapppatients;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.DoctorViewHolder> {

    private ArrayList<User> mDoctorsList;
    private Context mContext;

    public DoctorListAdapter(ArrayList<User> doctorsList , Context context) {
        mDoctorsList = doctorsList;
        mContext = context;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_list_item, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        User doctor = mDoctorsList.get(position);
        holder.bind(doctor);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity2.class);
                intent.putExtra("doctorId", doctor.getId()); // Pass the doctor's ID to the ChatActivity
                intent.putExtra("doctorName", holder.mNameTextView.getText().toString()); // Pass the doctor's name to the ChatActivity
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDoctorsList.size();
    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameTextView;


        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);

            mNameTextView = itemView.findViewById(R.id.nameTextView);

        }

        public void bind(User doctor) {
            String name = doctor.getFirstName() + " " + doctor.getMiddleName() + " " + doctor.getFamilyName();
            mNameTextView.setText(name);

        }
    }
}

