package com.anubhavmalikdeveloper.e_toll;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anubhavmalikdeveloper.e_toll.database.TripDBHelper;
import com.anubhavmalikdeveloper.e_toll.models.RouteModel;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class ShowTripActivity extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment supportMapFragment;
    TextView tripTextView;
    int tripId;
    RouteModel routeModel;
    private Context context;

    BookTripActivity bookTripActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        supportMapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.show_trip_map);
        supportMapFragment.getMapAsync(this);
        bookTripActivity=new BookTripActivity();
        context=this;
        tripId=getIntent().getIntExtra("key",0);
        tripTextView=(TextView)findViewById(R.id.trip_text_view);

        routeModel= TripDBHelper.getInstance(context).getTrip(tripId);

        tripTextView.setText("From "+routeModel.getSource()+" to "+routeModel.getDestination());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        bookTripActivity.getGeoCodedResult(context,routeModel.getSource(), routeModel.getDestination(), googleMap);
        bookTripActivity.putMarkersOnMap(context,routeModel.getLatArrayList(),routeModel.getLngArrayList(), googleMap);

    }
}
