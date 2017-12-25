package com.anubhavmalikdeveloper.e_toll.models;

import java.util.ArrayList;

/**
 * Created by PC on 23-12-2017.
 */

public class RouteModel {
    private ArrayList<String> latArrayList,lngArrayList;
    private String distance;
    private String time;
    private String source;
    private String destination;

    public RouteModel(ArrayList<String> latArrayList, ArrayList<String> lngArrayList, String distance, String time){

        this.latArrayList = latArrayList;
        this.lngArrayList = lngArrayList;
        this.distance = distance;
        this.time = time;
    }

    public RouteModel(ArrayList<String> latArrayList, ArrayList<String> lngArrayList, String distance, String time, String source, String destination){

        this.latArrayList = latArrayList;
        this.lngArrayList = lngArrayList;
        this.distance = distance;
        this.time = time;
        this.source = source;
        this.destination = destination;
    }

    public ArrayList<String> getLatArrayList() {
        return latArrayList;
    }

    public ArrayList<String> getLngArrayList() {
        return lngArrayList;
    }

    public String getDistance() {
        return distance;
    }

    public String getTime() {
        return time;
    }

    public String getDestination() {
        return destination;
    }

    public String getSource() {
        return source;
    }
}
