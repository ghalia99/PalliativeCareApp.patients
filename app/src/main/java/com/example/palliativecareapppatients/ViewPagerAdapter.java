package com.example.palliativecareapppatients;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    private List<Integer> fragmentLayouts;
    private List<String> fragmentTitles;

    public ViewPagerAdapter() {
        fragmentLayouts = new ArrayList<>();
        fragmentTitles = new ArrayList<>();
    }

    public void addFragment(int fragmentLayout, String fragmentTitle) {
        fragmentLayouts.add(fragmentLayout);
        fragmentTitles.add(fragmentTitle);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // bind data to views
    }

    @Override
    public int getItemCount() {
        return fragmentLayouts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return fragmentLayouts.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // find views by ID
        }
    }
}
