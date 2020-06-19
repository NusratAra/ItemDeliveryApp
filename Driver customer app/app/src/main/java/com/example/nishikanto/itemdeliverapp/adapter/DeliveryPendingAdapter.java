package com.example.nishikanto.itemdeliverapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.adapter.holder.BaseViewHolder;
import com.example.nishikanto.itemdeliverapp.adapter.holder.DeliveryPendingHolder;
import com.example.nishikanto.itemdeliverapp.adapter.holder.InDeliveryHolder;

import java.util.ArrayList;
import java.util.List;

public class DeliveryPendingAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    Activity activity;
    List filterList;
    List originalList;

    public DeliveryPendingAdapter(Activity activity, ArrayList<Integer> arrayList) {
        this.activity = activity;
        this.originalList = arrayList;
        this.filterList = arrayList;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        BaseViewHolder viewHolder;
        View view;

        switch (viewType){
            case -1:
                view = inflater.inflate(R.layout.content_home_card_pending, parent, false);
                viewHolder = new DeliveryPendingHolder(this, activity, view);
                break;
            case 0:
                view = inflater.inflate(R.layout.content_home_card, parent, false);
                viewHolder = new InDeliveryHolder(activity, view);
                break;
            case 1:
                view = inflater.inflate(R.layout.content_home_card_temp, parent, false);
                viewHolder = new InDeliveryHolder(activity, view);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
        return viewHolder;
    }

    public void onDialogConfirmClicked(int position){
        filterList.remove(position);
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        int type = (int) filterList.get(position);
        return type;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public void filterItem(int type) {
        filterList = new ArrayList<Integer>();
        for(int j = 0; j< originalList.size(); j++){
            if((int) originalList.get(j) == type){
                filterList.add(originalList.get(j));
            }
        }
        notifyDataSetChanged();
    }

    public void filterItemAll() {
        filterList = new ArrayList<Integer>();
        for(int j = 0; j< originalList.size(); j++){
            filterList.add(originalList.get(j));
        }
        notifyDataSetChanged();
    }
}














//case 2:
//        view = inflater.inflate(R.layout.content_home_card, parent, false);
//        viewHolder = new ReadyForDeliveryHolder(view);
//        break;