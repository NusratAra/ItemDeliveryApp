package com.example.nishikanto.itemdeliverapp.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nishikanto.itemdeliverapp.driver.payment.PaymentDetailsActivity;
import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.adapter.holder.PaymentDetailsHolder;

public class PaymentDetailsAdapter extends RecyclerView.Adapter<PaymentDetailsHolder> {
    private Activity activity;
    public PaymentDetailsAdapter(PaymentDetailsActivity paymentDetailsActivity) {
        this.activity = paymentDetailsActivity;
    }

    @NonNull
    @Override
    public PaymentDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PaymentDetailsHolder(activity,
                LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_details_list, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull PaymentDetailsHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
