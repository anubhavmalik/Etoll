package com.anubhavmalikdeveloper.e_toll.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anubhavmalikdeveloper.e_toll.R;

/**
 * Created by Anubhav on 09-12-2017.
 */

public class StartTripFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       //return super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.start_trip_fragment_layout,container,false);
        return view;

    }
}
