package com.example.nishikanto.itemdeliverapp.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.adapter.holder.TripHistoryHolder;
import com.example.nishikanto.itemdeliverapp.driver.trip.TripHistoryActivity;
import com.example.nishikanto.itemdeliverapp.model.Trip;

import java.util.ArrayList;

public class TripHistoryAdapter extends RecyclerView.Adapter<TripHistoryHolder> {
    Activity activity;
    ArrayList<Trip> tripList;
    ArrayList<Trip> tripListFilter = new ArrayList<>();

    public TripHistoryAdapter(TripHistoryActivity tripHistoryActivity, ArrayList<Trip> tripList) {
        this.activity = tripHistoryActivity;
        this.tripList = tripList;
        this.tripListFilter.addAll(tripList);
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
        holder.setData(tripListFilter.get(position));
    }

    @Override
    public int getItemCount() {
        return tripListFilter.size();
    }

    public void filterItemByPaid(boolean isPaid) {

        tripListFilter = new ArrayList<>();
        if(isPaid){
            for(int j = 0; j< tripList.size(); j++){
                if(tripList.get(j).isIs_paid()){
                    tripListFilter.add(tripList.get(j));
                }
            }
            notifyDataSetChanged();
        } else {
            for(int j = 0; j< tripList.size(); j++){
                if(!tripList.get(j).isIs_paid()){
                    tripListFilter.add(tripList.get(j));
                }
            }
            notifyDataSetChanged();
        }

    }

    public void filterItemCompletion(int status) {

        tripListFilter = new ArrayList<>();
        for(int j = 0; j< tripList.size(); j++){
            if(tripList.get(j).getStatus().matches(String.valueOf(status))){
                Log.d("TAG", "filterItemCompletion: ");
                tripListFilter.add(tripList.get(j));
            }
        }
        notifyDataSetChanged();

    }

    public void filterAllItem(){
        tripListFilter = new ArrayList<>();
        tripListFilter.addAll(tripList);
        notifyDataSetChanged();
    }
}
