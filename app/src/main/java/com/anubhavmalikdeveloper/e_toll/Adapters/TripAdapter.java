package com.anubhavmalikdeveloper.e_toll.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anubhavmalikdeveloper.e_toll.R;
import com.anubhavmalikdeveloper.e_toll.ShowTripActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by PC on 25-12-2017.
 */

public class TripAdapter extends  RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private Context context;
    private HashMap<Integer, String> hashMap;
    ArrayList<Integer> keys;

    public TripAdapter(Context context, HashMap<Integer, String> hashMap){

        this.context = context;
        this.hashMap = hashMap;
        keys=new ArrayList<>();
        keys.addAll(hashMap.keySet());
    }
    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trips, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TripViewHolder holder, final int position) {
        holder.textView.setText("View Trip booked on "+hashMap.get(keys.get(position)));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, ShowTripActivity.class);
                i.putExtra("key", keys.get(position));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hashMap.size();
    }

    class TripViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView textView;
        public TripViewHolder(View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.item_trip_card_view);
            textView=itemView.findViewById(R.id.item_trip_booking_date_text_view);
        }
    }
}
