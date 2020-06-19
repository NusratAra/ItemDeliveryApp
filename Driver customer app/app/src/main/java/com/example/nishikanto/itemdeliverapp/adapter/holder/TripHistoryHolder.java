package com.example.nishikanto.itemdeliverapp.adapter.holder;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nishikanto.itemdeliverapp.driver.trip.IndividualPaymentActivity;

public class TripHistoryHolder extends RecyclerView.ViewHolder {
    private Activity activity;
    public TripHistoryHolder(Activity activity, ViewGroup parent, @NonNull View itemView) {
        super(itemView);
        this.activity = activity;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent item=new Intent(activity, IndividualPaymentActivity.class);
                item.putExtra("status", 1);
                activity.startActivity(item);
            }
        });
    }
}