package com.dream.dreamtheather.activity;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.dream.dreamtheather.R;
import com.dream.dreamtheather.databinding.ActivityDreamLocationBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;

public class DreamLocation extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "GoogleMap";
    public static final String mapURL = "http://maps.google.com/maps?q=loc:";
    private ActivityDreamLocationBinding binding;
    private GoogleMap mMap;

    private String cinemaName;
    private String cinemaAddress;

    double mLatitude;
    double mLongitude;

    FirebaseFirestore firebaseFirestore;

    private static final String[] permission = new String[]{
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDreamLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();
        initUI();
        getLocation();

    }

    private void initUI(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        //check location permission
        if (!checkPermissionGranted())
            locationPermissionLauncher.launch(permission);
    }

    private void getLocation(){
        Intent getIntent = getIntent();
        cinemaAddress = getIntent.getStringExtra("cinemaAddress");
        cinemaName = getIntent.getStringExtra("cinemaName");
    }

    private void sendDirectionToGoogleMap(double latitude, double longitude) {
        String strUri = mapURL + latitude + "," + longitude + " (" + cinemaName + ")";
        Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
        mapIntent.setPackage("com.google.android.apps.maps");
        mapIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(mapIntent);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocationName(cinemaAddress, 1);

            if (addressList != null) {
                mLatitude = addressList.get(0).getLatitude();
                mLongitude = addressList.get(0).getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(mLatitude, mLongitude);
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions().position(location).title(cinemaName));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15), 2000, null);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap = googleMap;
            mMap.setMyLocationEnabled(true);
        }
        binding.btnDirection.setOnClickListener(v -> sendDirectionToGoogleMap(mLatitude, mLongitude));
    }

    final ActivityResultLauncher<String[]> locationPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            result -> {
                Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);
                if (fineLocationGranted != null && fineLocationGranted) {
                    // Precise location access granted.
                    Log.d(TAG, "register: one of permission missing ");
                } else if (coarseLocationGranted != null && coarseLocationGranted) {
                    // Only approximate location access granted.
                    Log.d(TAG, "register: one of permission missing ");
                } else {
                    // No location access granted.
                    Log.d(TAG, "register: No location permission granted ");
                }
            }
    );

    private boolean checkPermissionGranted() {
        int fine = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
        int coarse = ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION);
        return fine == PackageManager.PERMISSION_GRANTED && coarse == PackageManager.PERMISSION_GRANTED;
    }

    //TODO: implementation get user current location and print route using Google Route and Direction API
}
