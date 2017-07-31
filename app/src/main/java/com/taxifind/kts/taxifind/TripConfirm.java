package com.taxifind.kts.taxifind;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class TripConfirm extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private GPSTracker gps;
    private Location mLocation;
    private double longitude, latitude;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_confirm);

        //initToolBar();

        gps = new GPSTracker(TripConfirm.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**public void initToolBar()
     {
     Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
     toolbar.setTitle(R.string.toolbarTitle);

     setSupportActionBar(toolbar);

     toolbar.setNavigationIcon(R.drawable.ic_menu_black_18dp);
     toolbar.setNavigationOnClickListener(
     new View.OnClickListener() {
    @Override
    public void onClick(View v) {
    Toast.makeText(ChooseRank.this, "clicking the toolbar!", Toast.LENGTH_SHORT).show();
    }
    }
     );
     }*/



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

        //mMap.setMinZoomPreference(6.0f);
        //mMap.setMaxZoomPreference(14.0f);

        /**if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
         == PackageManager.PERMISSION_GRANTED) {
         mMap.setMyLocationEnabled(true);
         } else {
         // Show rationale and request permission.
         }*/

        // Add a marker in Sydney and move the camera


        LatLng myLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(myLocation).title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
    }
}