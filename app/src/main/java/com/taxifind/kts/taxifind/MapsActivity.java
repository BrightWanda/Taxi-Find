package com.taxifind.kts.taxifind;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.taxifind.kts.POJOs.Distance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private GPSTracker gps;
    private double longitude, latitude;
    private List<Address> addresses;
    private ArrayList<Distance> distance;
    private ApiInterface apiInterface;
    Geocoder geocoder;
    public static final String EXTRA_MESSAGE = "com.taxifind.kts.taxifind.MESSAGE";
    public static final String LONG = "com.taxifind.kts.taxifind.LONG";
    public static final String LAT = "com.taxifind.kts.taxifind.LAT";
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initToolBar();

        gps = new GPSTracker(MapsActivity.this);
        geocoder = new Geocoder(this, Locale.getDefault());

        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);


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

                spinner.setVisibility(View.VISIBLE);
                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

                Call<ArrayList<Distance>> call = apiInterface.getDistances(0, "Johannesburg", "Vosloorus", "Johannesburg", -26.209340, 28.039378);

                call.enqueue(new Callback<ArrayList<Distance>>(){
                    @Override
                    public void onResponse(Call<ArrayList<Distance>> call, Response<ArrayList<Distance>> response) {
                        distance = response.body();
                        Toast.makeText(getApplicationContext(), response.body().toString(), Toast.LENGTH_LONG).show();
                        spinner.setVisibility(View.GONE);
                        Intent intent = new Intent(MapsActivity.this, ChooseRank.class);
                        intent.putExtra(EXTRA_MESSAGE, distance);
                        intent.putExtra(LONG, longitude +"");
                        intent.putExtra(LAT, latitude + "");
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Distance>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                    }
                });
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
                    Intent intent = new Intent(MapsActivity.this, Overview.class);
                    startActivity(intent);
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
