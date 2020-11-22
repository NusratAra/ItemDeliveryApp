package com.example.nishikanto.itemdeliverapp.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.adapter.holder.DeliveryPendingHolder;
import com.example.nishikanto.itemdeliverapp.adapter.holder.InDeliveryHolder;
import com.example.nishikanto.itemdeliverapp.model.Trip;
import com.example.nishikanto.itemdeliverapp.utils.BaseUrlUtils;

import java.util.ArrayList;

public class DeliveryPendingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = DeliveryPendingAdapter.class.getSimpleName();
    Activity activity;
    ArrayList<Trip> filterList;
    ArrayList<Trip> originalList;

    public DeliveryPendingAdapter(Activity activity, ArrayList<Trip> arrayList) {
        this.activity = activity;
        this.originalList = arrayList;
        this.filterList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        switch (viewType){
            case -1:
                view = inflater.inflate(R.layout.content_home_card_pending, parent, false);
                return new DeliveryPendingHolder(this, activity, view, filterList);
            case 0:
                view = inflater.inflate(R.layout.content_home_card, parent, false);
                return new InDeliveryHolder(activity, view, filterList);
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){
            case -1:
                DeliveryPendingHolder deliveryPendingHolder = (DeliveryPendingHolder) holder;
                deliveryPendingHolder.setData(filterList.get(position));
                break;
            case 0:
                InDeliveryHolder inDeliveryHolder = (InDeliveryHolder) holder;
                inDeliveryHolder.setData(filterList.get(position));
                break;
            default:
                throw new IllegalStateException("Unexpected value: ");
        }
    }

    public void onDialogConfirmClicked(int position){
        filterList.remove(position);
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        Trip trip = (Trip) filterList.get(position);

        String status = trip.getStatus();
//        String status = "1";

//        if(position == 0){
//            status = "1";
//        }

//        Log.d(TAG, "status: "+ status);
       // "4"
        if(status.matches(""+ BaseUrlUtils.CREATED_TRIP)){
            return -1;
        } else {
            return 0;
        }
    }


    @Override
    public int getItemCount() {
        Log.d(TAG, "Size: "+ filterList.size());
        return filterList.size();
    }

    public void filterItem(int type) {
        filterList = new ArrayList<Trip>();
        for(int j = 0; j< originalList.size(); j++){
            if(originalList.get(j).getStatus().equals(String.valueOf(type))){
                filterList.add(originalList.get(j));
            }
        }
        notifyDataSetChanged();
    }

    public void filterItemAll() {
        filterList = new ArrayList<Trip>();
        for(int j = 0; j< originalList.size(); j++){
            filterList.add(originalList.get(j));
        }
        notifyDataSetChanged();
    }
}