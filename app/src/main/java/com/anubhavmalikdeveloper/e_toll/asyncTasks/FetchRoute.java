package com.anubhavmalikdeveloper.e_toll.asyncTasks;

import android.os.AsyncTask;

import com.anubhavmalikdeveloper.e_toll.listeners.OnRouteFetchFinishListener;
import com.anubhavmalikdeveloper.e_toll.models.RouteModel;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by PC on 23-12-2017.
 */

public class FetchRoute extends AsyncTask<String,Void,Void> {
    private Connection.Response response;
    private ArrayList<String> latArrayList=new ArrayList<>();
    private ArrayList<String> lngArrayList=new ArrayList<>();
    private OnRouteFetchFinishListener onRouteFetchFinishListener;

    private String distance,time;

    public FetchRoute(OnRouteFetchFinishListener onRouteFetchFinishListener){

        this.onRouteFetchFinishListener = onRouteFetchFinishListener;
    }
    @Override
    protected Void doInBackground(String... params) {
        try {
            response=Jsoup.connect("http://tis.nhai.gov.in/UploadHandler.ashx?Up=3&Source="+params[0]+",%20India&Destination="+params[1]+",%20India&waypoints=")
                    .method(Connection.Method.GET).execute();
            String data=response.body();
            String s1[]=data.split("\\$\\$");
            String latlng[]=s1[0].split("\\$");
            String s2[];
            for (String i :latlng) {
                s2=i.split(",");
                latArrayList.add(s2[0]);
                lngArrayList.add(s2[1]);
            }
            String s3[]=s1[1].split("!");
            distance=s3[0];
            time=s3[1];
            return null;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        onRouteFetchFinishListener.onRouteFetchFinished(new RouteModel(latArrayList,lngArrayList,distance,time));

    }
}
