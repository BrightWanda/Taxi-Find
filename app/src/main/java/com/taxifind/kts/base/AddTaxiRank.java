package com.taxifind.kts.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
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

import com.taxifind.kts.POJOs.UserInput;
import com.taxifind.kts.POJOs.UserInputID;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This activity is responsible for allowing the user to input Taxi Rank information
 */
public class AddTaxiRank extends AppCompatActivity{

    private View mProgressView;
    private View mLoginFormView;
    private GPSTracker gps;
    private double longitude, latitude;
    TextView orgincity; // Origin City
    TextView originrank; // Origin Taxi Rank
    TextView destinationcity; // Destination City
    TextView destinationrank; //Destination Taxi Rank
    Button mSubmit;
    Geocoder geocoder;
    private List<Address> addresses;
    private UserInput userinput;
    private UserInputID userinputID;
    private ApiInterface apiInterface;
    private int postID = 0;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_taxi_rank);

        Intent intent = getIntent();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        userinput = (UserInput) intent.getSerializableExtra("com.taxifind.kts.taxifind.MESSAGE");

        orgincity = (TextView)findViewById(R.id.addOrigincity); // Origin City
        originrank = (TextView)findViewById(R.id.addOriginrank); // Origin Taxi Rank
        destinationcity = (TextView)findViewById(R.id.addDestinationcity); // Destination City
        destinationrank = (TextView)findViewById(R.id.addDestinationrank); //Destination Taxi Rank

        mSubmit = (Button) findViewById(R.id.submit); //Submit button

        mSubmit.setEnabled(false);

        Call<UserInputID> call = apiInterface.getUserInputID();

        call.enqueue(new Callback<UserInputID>(){
            @Override
            public void onResponse(Call<UserInputID> call, Response<UserInputID> response) {
                userinputID = response.body();
                if(userinputID != null) {
                    Toast.makeText(getApplicationContext(), userinputID.getOdataCount(), Toast.LENGTH_LONG).show();
                    mSubmit.setEnabled(true);
                    postID = Integer.parseInt(userinputID.getOdataCount());
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Something went wrong, retrying to connect to server!!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserInputID> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });





        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String origin_city =  orgincity.getText().toString();
                String origin_rank = originrank.getText().toString();
                String destination_city = destinationcity.getText().toString();
                String destination_rank = destinationrank.getText().toString();

                origin_city = origin_city + "(" + origin_rank + ")";
                destination_city = destination_city+ "(" + destination_rank + ")";

                //Toast.makeText(getApplicationContext(), origin_city + "(" + origin_rank + ")" + " : " + destination_city+ "(" + destination_rank + ")", Toast.LENGTH_SHORT).show();
                Call<UserInput> call = apiInterface.getUserInput(postID,origin_city,destination_city,"0",0.0,0.0,0.0,0.0,null);

                call.enqueue(new Callback<UserInput>(){
                    @Override
                    public void onResponse(Call<UserInput> call, Response<UserInput> response) {
                        userinput = response.body();
                        if(userinput != null) {
                            Toast.makeText(getApplicationContext(), response.body().toString(), Toast.LENGTH_LONG).show();
                            orgincity.setText("");
                            originrank.setText("");
                            destinationcity.setText("");
                            destinationrank.setText("");
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "kunzima", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<UserInput> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                    }
                });
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

