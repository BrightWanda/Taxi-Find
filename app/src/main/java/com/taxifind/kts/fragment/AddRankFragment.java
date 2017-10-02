package com.taxifind.kts.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.taxifind.kts.POJOs.Distance;
import com.taxifind.kts.POJOs.UserInput;
import com.taxifind.kts.POJOs.UserInputID;
import com.taxifind.kts.main.ApiClient;
import com.taxifind.kts.main.ApiInterface;
import com.taxifind.kts.main.LazyAdapter;
import com.taxifind.kts.main.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddRankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddRankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRankFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;

    private View rootView;
    private View mProgressView;
    private View mLoginFormView;
    private String myCity = "";
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

    private CheckBox chkBox;

    public AddRankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddRankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddRankFragment newInstance(String param1, String param2) {
        AddRankFragment fragment = new AddRankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_add_rank, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            checkLocationPermission();
        }
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        chkBox = rootView.findViewById(R.id.checkbox_send_loc);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        //userinput = (UserInput) intent.getSerializableExtra("com.taxifind.kts.taxifind.MESSAGE");

        orgincity = rootView.findViewById(R.id.addOrigincity); // Origin City
        originrank = rootView.findViewById(R.id.addOriginrank); // Origin Taxi Rank
        destinationcity = rootView.findViewById(R.id.addDestinationcity); // Destination City
        destinationrank = rootView.findViewById(R.id.addDestinationrank); //Destination Taxi Rank

        mSubmit = rootView.findViewById(R.id.submit); //Submit button

        mSubmit.setEnabled(false);

        Call<UserInputID> call = apiInterface.getUserInputID();

        call.enqueue(new Callback<UserInputID>(){
            @Override
            public void onResponse(Call<UserInputID> call, Response<UserInputID> response) {
                userinputID = response.body();
                if(userinputID != null) {
                    mSubmit.setEnabled(true);
                    postID = Integer.parseInt(userinputID.getOdataCount());
                }
                else
                {
                    Toast.makeText(getActivity(), "Something went wrong, retrying to connect to server!!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserInputID> call, Throwable t) {
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<UserInput> call;

                String origin_city =  orgincity.getText().toString();
                String origin_rank = originrank.getText().toString();
                String destination_city = destinationcity.getText().toString();
                String destination_rank = destinationrank.getText().toString();

                origin_city = origin_city + "(" + origin_rank + ")";
                destination_city = destination_city+ "(" + destination_rank + ")";

                //Toast.makeText(getApplicationContext(), origin_city + "(" + origin_rank + ")" + " : " + destination_city+ "(" + destination_rank + ")", Toast.LENGTH_SHORT).show();
                if(chkBox.isEnabled())
                {
                    call = apiInterface.getUserInput(postID,origin_city,destination_city,"0",latitude,longitude,0.0,0.0,null);
                }
                else{
                    call = apiInterface.getUserInput(postID,origin_city,destination_city,"0",0.0,0.0,0.0,0.0,null);
                }

                call.enqueue(new Callback<UserInput>(){
                    @Override
                    public void onResponse(Call<UserInput> call, Response<UserInput> response) {
                        userinput = response.body();
                        if(userinput != null) {
                            Toast.makeText(getActivity(), response.body().toString(), Toast.LENGTH_LONG).show();
                            orgincity.setText("");
                            originrank.setText("");
                            destinationcity.setText("");
                            destinationrank.setText("");
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "kunzima", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<UserInput> call, Throwable t) {
                        Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        /*chkBox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextView textView = (TextView) rootView.findViewById(R.id.txtOrigin);

                if(chkBox.isChecked())
                {
                    textView.setVisibility(View.GONE);
                }
                else
                {
                    textView.setVisibility(View.VISIBLE);
                }
            }
        });*/

        mLoginFormView = rootView.findViewById(R.id.login_form);
        mProgressView = rootView.findViewById(R.id.login_progress);
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try{
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            myCity = addresses.get(0).getLocality();
        }catch(IOException ex) {
            //Do something with the exception
        }

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        final LocationManager manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }

            if(!manager.isProviderEnabled( LocationManager.GPS_PROVIDER ))
            {
                showSettingsAlert();
            }

            return false;
        } else {
            if(!manager.isProviderEnabled( LocationManager.GPS_PROVIDER ))
            {
                showSettingsAlert();
            }
            return true;
        }
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
