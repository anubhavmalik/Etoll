package com.anubhavmalikdeveloper.e_toll.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anubhavmalikdeveloper.e_toll.Adapters.TripAdapter;
import com.anubhavmalikdeveloper.e_toll.BookTripActivity;
import com.anubhavmalikdeveloper.e_toll.R;
import com.anubhavmalikdeveloper.e_toll.asyncTasks.FetchRoute;
import com.anubhavmalikdeveloper.e_toll.database.TripDBHelper;
import com.anubhavmalikdeveloper.e_toll.models.RouteModel;

import java.util.concurrent.ExecutionException;

/**
 * Created by Anubhav on 09-12-2017.
 */

public class TripStatusFragment extends Fragment {

    Context context;
    TextView currentTripTextView, bookTripTextView;
    RecyclerView tripRecyclerView;

    LinearLayout linearLayout;
    TripDBHelper tripDBHelper;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();
        tripDBHelper=TripDBHelper.getInstance(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.trip_status_fragment_layout,container,false);
        currentTripTextView=view.findViewById(R.id.current_trip_text_view);
        bookTripTextView=view.findViewById(R.id.book_trip_text_view);
        tripRecyclerView=view.findViewById(R.id.trip_recycler_view);
        tripRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        bookTripTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, BookTripActivity.class));
            }
        });
        linearLayout=view.findViewById(R.id.linear_layout);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(tripDBHelper.getBookingTime().size()>0){
            linearLayout.setVisibility(View.GONE);
            tripRecyclerView.setAdapter(new TripAdapter(context,tripDBHelper.getBookingTime()));
        }else{
            linearLayout.setVisibility(View.VISIBLE);
        }
    }
}
