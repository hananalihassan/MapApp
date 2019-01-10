package hanan.hanan_ali.mappapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    Button find_btn, offline_btn;
    ProgressBar progress;
    LocationRequest locationRequest;
    LocationCallback locationCallback = new LocationCallback() {

    };
    FusedLocationProviderClient fusedLocationProviderClient;
    Double lat, lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
        }


        RequestLocation();
        RequestCallBack();


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        progress = findViewById(R.id.progress);
        find_btn = findViewById(R.id.find_btn);
        offline_btn = findViewById(R.id.offline_btn);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        find_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    return;
                }

                progress.setVisibility(View.VISIBLE);
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

            }
        });
        offline_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                progress.setVisibility(View.INVISIBLE);
                mMap.clear();

            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }
    public void RequestLocation()
    {
        locationRequest=new LocationRequest();
        locationRequest.setInterval(20000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    public void RequestCallBack()
    {
        locationCallback=new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                if (locationResult==null)
                {
                    return;
                }
                mMap.clear();
                for (Location location:locationResult.getLocations())
                {
                    lat=location.getLatitude();
                    lng=location.getLongitude();
                    LatLng home=new LatLng(lat,lng);
                    CameraPosition cameraPosition=CameraPosition
                            .builder()
                            .target(home)
                            .zoom(16)
                            .build();
                    mMap.addMarker(new MarkerOptions().position(home).title("my home"));
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),6000,null);
                    progress.setVisibility(View.INVISIBLE);

                }

            }
        };

    }
}
