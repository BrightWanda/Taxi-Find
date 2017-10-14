package com.taxifind.kts.base;

import android.content.Intent;
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
    private double longitude, latitude;
    ListView list;
    LazyAdapter adapter;
    private ArrayList<Distance> distance;
    private String dest = "";
    public static final String EXTRA_MESSAGE = "com.taxifind.kts.taxifind.MESSAGE";
    public static final String LONG = "com.taxifind.kts.taxifind.LONG";
    public static final String LAT = "com.taxifind.kts.taxifind.LAT";
    public static final String DESTINATION = "com.taxifind.kts.taxifind.DEST";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_rank);

        Intent intent = getIntent();
        longitude = Double.parseDouble(intent.getStringExtra(MapsActivity.LONG));
        latitude = Double.parseDouble(intent.getStringExtra(MapsActivity.LAT));
        dest = intent.getStringExtra(MapsActivity.DESTINATION).toString();
        distance = (ArrayList<Distance>) intent.getSerializableExtra(EXTRA_MESSAGE);
        list= (ListView)findViewById(R.id.list);

        adapter=new LazyAdapter(this, distance, 1);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(getApplicationContext(), TripConfirm.class);
                intent.putExtra(EXTRA_MESSAGE, adapter.getItem(position));
                intent.putExtra(LONG,longitude+"");
                intent.putExtra(DESTINATION, dest);
                intent.putExtra(LAT,latitude+"");

                startActivity(intent);
            }
    });

        initToolBar();

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
}