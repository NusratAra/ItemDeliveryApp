package com.example.nishikanto.itemdeliverapp.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.adapter.holder.PaymentHistoryHolder;
import com.example.nishikanto.itemdeliverapp.model.SinglePaymentHistory;

import java.util.ArrayList;
import java.util.List;

public class PaymentHistoryAdaper extends RecyclerView.Adapter<PaymentHistoryHolder> {
    private List<SinglePaymentHistory> paymentHistoryList;
    private Activity activity;


    public PaymentHistoryAdaper(Activity activity, ArrayList<SinglePaymentHistory> paymentHistoryList) {
        this.paymentHistoryList = paymentHistoryList;
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
        holder.setData(paymentHistoryList.get(position));

    }

    @Override
    public int getItemCount() {
        return paymentHistoryList.size();
    }
}

