package com.example.palliativecareapppatients;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class UserHome extends Fragment implements LocationListener {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private LocationManager locationManager;
    private static final int PERMISSION_REQUEST_CODE = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_user_home, container, false);

        viewPager = root.findViewById(R.id.view_pager);
        tabLayout = root.findViewById(R.id.tab_layout);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity());

        // Set the ViewPagerAdapter as the adapter for the ViewPager2
        viewPager.setAdapter(viewPagerAdapter);

        // Set up the TabLayout with the ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(viewPagerAdapter.getPageTitle(position)))
                .attach();


        // Check location permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, start location updates
            startLocationUpdates();
        } else {
            // Request location permission
            requestLocationPermission();
        }

        return root;
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Show an explanation to the user
            // You can show a dialog or a Snackbar explaining why you need the location permission
        } else {
            // Request the permission
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    private void startLocationUpdates() {
        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, start location updates
                startLocationUpdates();
            } else {
                // Location permission denied
                // You can show a message or handle the denial case
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Track the current location
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        trackLocation(latitude, longitude);

        // Stop location updates
        locationManager.removeUpdates(this);
    }

    public void trackLocation(double latitude, double longitude) {
        // Perform your location tracking logic here
        // You can use the latitude and longitude values
    }

    @Override
    public void onProviderEnabled(String provider) {
        // This method is called when the GPS is enabled
        // You can show a message or perform any actions needed
    }

    @Override
    public void onProviderDisabled(String provider) {
        // This method is called when the GPS is disabled
        // You can show a message or perform any actions needed
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // This method is called when the status of the GPS provider changes
        // You can show a message or perform any actions needed
    }
}
