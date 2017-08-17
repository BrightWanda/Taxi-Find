package com.taxifind.kts.taxifind;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.taxifind.kts.POJOs.Distance;

import java.util.ArrayList;


public class ChooseRank extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private GPSTracker gps;
    private Location mLocation;
    private double longitude, latitude;
    ListView list;
    LazyAdapter adapter;
    private ArrayList<Distance> distance;
    public static final String EXTRA_MESSAGE = "com.taxifind.kts.taxifind.MESSAGE";
    public static final String LONG = "com.taxifind.kts.taxifind.LONG";
    public static final String LAT = "com.taxifind.kts.taxifind.LAT";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_rank);

        Intent intent = getIntent();
        longitude = Double.parseDouble(intent.getStringExtra(MapsActivity.LONG));
        latitude = Double.parseDouble(intent.getStringExtra(MapsActivity.LAT));

        distance = (ArrayList<Distance>) intent.getSerializableExtra(EXTRA_MESSAGE);
        list= (ListView)findViewById(R.id.list);

        adapter=new LazyAdapter(this, distance);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(), TripConfirm.class);
                intent.putExtra(LONG, longitude +"");
                intent.putExtra(LAT, latitude + "");
                startActivity(intent);
            }
        });

        initToolBar();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
                        Toast.makeText(ChooseRank.this, "clicking the toolbar!", Toast.LENGTH_SHORT).show();
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
    }
    /*
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng myLocation = new LatLng(latitude, longitude);

        latitude += 100.50;
        latitude -= 10.50;

        LatLng my2ndLocation = new LatLng(latitude, longitude);

        latitude -= 150.50;
        latitude += 160.50;

        LatLng my3rdLocation = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions().position(myLocation).title("Your Location"));
        mMap.addMarker(new MarkerOptions().position(my2ndLocation).title("A"));
        mMap.addMarker(new MarkerOptions().position(my3rdLocation).title("B"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(my3rdLocation,15));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
    }*/
}