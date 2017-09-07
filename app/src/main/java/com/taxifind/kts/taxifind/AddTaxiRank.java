package com.taxifind.kts.taxifind;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This activity is responsible for allowing the user to input Taxi Rank information
 */
public class AddTaxiRank extends AppCompatActivity{

    private View mProgressView;
    private View mLoginFormView;
    private GPSTracker gps;
    private double longitude, latitude;
    private String origin_city = "";
    private String destination_city = "";
    private String origin_rank = "";
    private String destination_rank = "";
    Geocoder geocoder;
    private List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_taxi_rank);

        Button mSubmit = (Button) findViewById(R.id.submit); //Submit button

        TextView orgincity = (TextView)findViewById(R.id.addOrigincity); // Origin City
        TextView originrank = (TextView)findViewById(R.id.addOriginrank); // Origin Taxi Rank
        TextView destinationcity = (TextView)findViewById(R.id.addDestinationcity); // Destination City
        TextView destinationrank = (TextView)findViewById(R.id.addDestinationrank); //Destination Taxi Rank

        origin_city = orgincity.getText().toString().trim();
        origin_rank = originrank.getText().toString();
        destination_city = destinationcity.getText().toString();
        destination_rank = destinationrank.getText().toString();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), origin_city, Toast.LENGTH_SHORT).show();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        if(checked)
        {
            gps = new GPSTracker(AddTaxiRank.this);
            geocoder = new Geocoder(this, Locale.getDefault());

            // check if GPS enabled
            if(gps.canGetLocation()){

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

                try{
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);

                }catch(IOException ex) {
                    //Do something with the exception
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                }

                // \n is for new line
                Toast.makeText(getApplicationContext(), addresses.get(0).getAddressLine(2), Toast.LENGTH_LONG).show();
            }else{
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();

            }
        }
    }
}

