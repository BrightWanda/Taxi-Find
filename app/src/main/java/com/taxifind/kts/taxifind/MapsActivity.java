package com.taxifind.kts.taxifind;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private GPSTracker gps;
    private Location mLocation;
    private double longitude, latitude;
    private List<Address> addresses;
    private List<Distance> distance;
    private ApiInterface apiInterface;
    Geocoder geocoder;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initToolBar();

        gps = new GPSTracker(MapsActivity.this);
        geocoder = new Geocoder(this, Locale.getDefault());


        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            try{
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            }catch(IOException ex) {
                //Do something with the exception
            }

            // \n is for new line
            Toast.makeText(getApplicationContext(), addresses.get(0).getAddressLine(2), Toast.LENGTH_LONG).show();
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

        final Button button = (Button) findViewById(R.id.findBtn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

                Call<List<Distance>> call = apiInterface.getDistances(0, "Johannesburg", "Vosloorus", "Johannesburg", -26.209340, 28.039378);

                call.enqueue(new Callback<List<Distance>>() {
                    @Override
                    public void onResponse(Call<List<Distance>> call, Response<List<Distance>> response) {
                        distance = response.body();
                        Toast.makeText(getApplicationContext(), distance.get(0).getRankname() , Toast.LENGTH_LONG).show();
                        //adapter = new RecyclerAdapter(contacts);
                        //recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<List<Distance>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                    }
                });


                // Code here executes on main thread after user presses button
                Intent intent = new Intent(MapsActivity.this, TripConfirm.class);
                startActivity(intent);
            }
        });
    }

    public void initToolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbarTitle);

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_menu_black_18dp);
        toolbar.setNavigationOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MapsActivity.this, "clicking the toolbar!", Toast.LENGTH_SHORT).show();
                }
            }
        );
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

        LatLng myLocation = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions().position(myLocation).title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,15));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
    }
}
