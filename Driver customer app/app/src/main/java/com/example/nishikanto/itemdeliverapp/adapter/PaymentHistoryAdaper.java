package com.example.nishikanto.itemdeliverapp.adapter;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.adapter.holder.PaymentHistoryHolder;
import com.example.nishikanto.itemdeliverapp.model.SinglePaymentHistory;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class PaymentHistoryAdaper extends RecyclerView.Adapter<PaymentHistoryHolder> {
    private ArrayList<SinglePaymentHistory> paymentHistoryList;
    private ArrayList<SinglePaymentHistory> paymentHistoryListFilter = new ArrayList<>();
    private Activity activity;


    public PaymentHistoryAdaper(Activity activity, ArrayList<SinglePaymentHistory> paymentHistoryList) {
        this.paymentHistoryList = paymentHistoryList;
        this.paymentHistoryListFilter.addAll(paymentHistoryList);
        this.activity = activity;
        Log.d("TAG", "paymentHistoryList: "+ new GsonBuilder().setPrettyPrinting().create().toJson(this.paymentHistoryList));
        Log.d("TAG", "paymentHistoryListFilter: "+ new GsonBuilder().setPrettyPrinting().create().toJson(this.paymentHistoryListFilter));

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
        Log.d("TAG", "paymentHistoryListFilter: "+ new GsonBuilder().setPrettyPrinting().create().toJson(paymentHistoryListFilter));

        holder.setData(paymentHistoryListFilter.get(position));

    }

    @Override
    public int getItemCount() {
        return paymentHistoryListFilter.size();
    }

    public void filterItemByDate(String date) {

        paymentHistoryListFilter = new ArrayList<>();
        for(int j = 0; j< paymentHistoryList.size(); j++){
            if(paymentHistoryList.get(j).getCreated_at__date().matches(date)){
                paymentHistoryListFilter.add(paymentHistoryList.get(j));
            }
        }
        notifyDataSetChanged();
    }
}

