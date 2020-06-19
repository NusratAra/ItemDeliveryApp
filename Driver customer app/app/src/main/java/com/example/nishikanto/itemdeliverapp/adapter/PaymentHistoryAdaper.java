package com.example.nishikanto.itemdeliverapp.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.adapter.holder.PaymentHistoryHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PaymentHistoryAdaper extends RecyclerView.Adapter<PaymentHistoryHolder> {
    private List<String> dates;
    private Activity activity;


    public PaymentHistoryAdaper(Activity activity, ArrayList<String> dates) {
        this.dates = dates;
        this.activity = activity;
    }


    @NonNull
    @Override
    public PaymentHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new PaymentHistoryHolder(
                activity,
                parent,
                inflater.inflate(R.layout.payment_history_list, parent, false)
        );

//        return new PaymentHistoryHolder(
//                LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_history_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentHistoryHolder holder, int position) {
        holder.setData(Collections.singletonList(dates.get(position)));

    }

    @Override
    public int getItemCount() {
        return dates.size();
    }
}

