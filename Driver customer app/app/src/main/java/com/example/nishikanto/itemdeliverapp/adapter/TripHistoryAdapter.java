package com.example.nishikanto.itemdeliverapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.driver.trip.TripHistoryActivity;
import com.example.nishikanto.itemdeliverapp.adapter.holder.TripHistoryHolder;

public class TripHistoryAdapter extends RecyclerView.Adapter<TripHistoryHolder> {
    Activity activity;

    public TripHistoryAdapter(TripHistoryActivity tripHistoryActivity) {
        this.activity = tripHistoryActivity;
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

    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
