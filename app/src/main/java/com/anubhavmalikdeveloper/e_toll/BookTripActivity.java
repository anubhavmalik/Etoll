package com.anubhavmalikdeveloper.e_toll;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anubhavmalikdeveloper.e_toll.asyncTasks.FetchRoute;
import com.anubhavmalikdeveloper.e_toll.database.TripDBHelper;
import com.anubhavmalikdeveloper.e_toll.listeners.OnRouteFetchFinishListener;
import com.anubhavmalikdeveloper.e_toll.models.RouteModel;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.nlopez.smartlocation.OnGeocodingListener;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.geocoding.utils.LocationAddress;

public class BookTripActivity extends AppCompatActivity implements OnMapReadyCallback{

    SupportMapFragment supportMapFragment;
    GoogleMap mGoogleMap;
    EditText sourceEditText,destinationEditText;
    Button submitButton,bookButton;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private Activity activity;
    private Context context;

    TextView timeTextView, distanceTextView, changeTripTextView;

    ProgressBar progressBar;
    LinearLayout formLinearLayout, routeInfoLinearLayout;

    RouteModel routeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final InputMethodManager keyboard = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        progressBar=(ProgressBar)findViewById(R.id.book_trip_progress_bar);
        formLinearLayout=(LinearLayout) findViewById(R.id.book_trip_form_linear_Layout);
        routeInfoLinearLayout=(LinearLayout)findViewById(R.id.route_info_linear_layout);

        timeTextView=(TextView)findViewById(R.id.time_text_view);
        distanceTextView=(TextView)findViewById(R.id.distance_text_view);
        changeTripTextView=(TextView)findViewById(R.id.changeTrip_text_view);

        activity =this;
        context=this;
        supportMapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.book_trip_map);
        supportMapFragment.getMapAsync(this);
        sourceEditText=(EditText) findViewById(R.id.source_edit_text);
        destinationEditText=(EditText)findViewById(R.id.destination_edit_text);
        submitButton=(Button)findViewById(R.id.submit_button);
        bookButton=(Button)findViewById(R.id.book_button);

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TripDBHelper(context).addTrip(routeModel, sourceEditText.getText().toString(),destinationEditText.getText().toString());
                startActivity(new Intent(context, PaymentActivity.class));
            }
        });

        changeTripTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                routeInfoLinearLayout.setVisibility(View.GONE);
                formLinearLayout.setVisibility(View.VISIBLE);
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmartLocation.with(context).location().stop();
                final String source=sourceEditText.getText().toString(), destination=destinationEditText.getText().toString();
                if(source.equals("")){
                    sourceEditText.setError("Source cannot be empty");
                }else if(destination.equals("")){
                    destinationEditText.setError("Destination cannot be empty");
                }else{
                    submitButton.requestFocus();
                    keyboard.hideSoftInputFromWindow(submitButton.getWindowToken(),0);

                    submitButton.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    new FetchRoute(new OnRouteFetchFinishListener() {
                        @Override
                        public void onRouteFetchFinished(RouteModel rm) {
                            routeModel=rm;
                            routeInfoLinearLayout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            formLinearLayout.setVisibility(View.GONE);
                            mGoogleMap.clear();
                            timeTextView.setText(routeModel.getTime());
                            distanceTextView.setText(routeModel.getDistance());
                            putMarkersOnMap(context,routeModel.getLatArrayList(),routeModel.getLngArrayList(), mGoogleMap);
                            getGeoCodedResult(context,source, destination, mGoogleMap);
                        }
                    }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,source,destination);

                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap=googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already grante
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            mGoogleMap.setMyLocationEnabled(true);
        }
        SmartLocation.with(context).location()
                .oneFix()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude()))
                        .title("Your Location"));
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),6));
                    }
                });
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(activity,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    void putMarkersOnMap(Context context,ArrayList<String> latArrayList, ArrayList<String> lngArrayList, GoogleMap mGoogleMap){
        final LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for(int i=0;i<latArrayList.size();i++){
            LatLng latLng = new LatLng(Double.parseDouble(latArrayList.get(i)), Double.parseDouble(lngArrayList.get(i)));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            mGoogleMap.addMarker(markerOptions);
            builder.include(latLng);
        }
        LatLngBounds bounds = builder.build();
        int width = context.getResources().getDisplayMetrics().widthPixels;
        int height = context.getResources().getDisplayMetrics().heightPixels;

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, 0);
        mGoogleMap.animateCamera(cu);
    }

    void getGeoCodedResult(Context context,String s, final String d, final GoogleMap mGoogleMap){
        SmartLocation.with(context).geocoding()
                .direct(s, new OnGeocodingListener() {
                    @Override
                    public void onLocationResolved(String name, List<LocationAddress> results) {
                        if (results.size() > 0) {
                            final Location sLocation = results.get(0).getLocation();
                            final LatLng source=new LatLng(sLocation.getLatitude(),sLocation.getLongitude());
                            mGoogleMap.addMarker(new MarkerOptions().position(source
                            ).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                        }
                    }
                });
        SmartLocation.with(context).geocoding()
                .direct(d, new OnGeocodingListener() {
                    @Override
                    public void onLocationResolved(String name, List<LocationAddress> results) {
                        if (results.size() > 0) {
                            Location dLocation = results.get(0).getLocation();
                            LatLng dest=new LatLng(dLocation.getLatitude(),dLocation.getLongitude());
                            mGoogleMap.addMarker(new MarkerOptions().position(dest
                            ).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        }
                    }
                });
    }

}
