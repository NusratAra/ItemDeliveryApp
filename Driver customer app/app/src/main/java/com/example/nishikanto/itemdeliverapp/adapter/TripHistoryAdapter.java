package com.example.nishikanto.itemdeliverapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.driver.trip.TripHistoryActivity;
import com.example.nishikanto.itemdeliverapp.adapter.holder.TripHistoryHolder;
import com.example.nishikanto.itemdeliverapp.model.Trip;

import java.util.ArrayList;

public class TripHistoryAdapter extends RecyclerView.Adapter<TripHistoryHolder> {
    Activity activity;
    ArrayList<Trip> trip;

    public TripHistoryAdapter(TripHistoryActivity tripHistoryActivity, ArrayList<Trip> trip) {
        this.activity = tripHistoryActivity;
        this.trip = trip;
    }

    @NonNull
    @Override
    public TripHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new TripHistoryHolder(
                activity,
                parent,
                inflater.inflate(R.layout.trip_history_list, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TripHistoryHolder holder, int position) {
        holder.setData(trip.get(position));
    }

    @Override
    public int getItemCount() {
        return trip.size();
    }
}
