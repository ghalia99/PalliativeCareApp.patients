import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.palliativecareapppatients.R;

import java.util.ArrayList;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.DoctorViewHolder> {

    private ArrayList<User> mDoctorsList;

    public DoctorListAdapter(ArrayList<User> doctorsList) {
        mDoctorsList = doctorsList;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout., parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        User doctor = mDoctorsList.get(position);
        holder.bind(doctor);
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

