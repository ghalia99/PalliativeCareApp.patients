package com.example.palliativecareapppatients;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class DoctorHome extends Fragment {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_doctor_home, container, false);

        viewPager = root.findViewById(R.id.view_pager);
        tabLayout = root.findViewById(R.id.tab_layout);

        FragmentManager fragmentManager = getChildFragmentManager();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter();
        viewPagerAdapter.addFragment( R.layout.fragment_followed_doctor, "الاطباء");
        viewPagerAdapter.addFragment(R.layout.fragment_posts, "المواضيع");

        viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("الاطباء");
                    break;
                case 1:
                    tab.setText("المواضيع");
                    break;
            }
        }).attach();

        return root;
    }
}