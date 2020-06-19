package com.example.nishikanto.itemdeliverapp.adapter.holder;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nishikanto.itemdeliverapp.driver.payment.PaymentDetailsActivity;
import com.example.nishikanto.itemdeliverapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PaymentHistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private List<String > list;
    private Activity activity;

    @BindView(R.id.date)
    TextView date;


    public PaymentHistoryHolder(Activity activity, ViewGroup parent, @NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.activity = activity;
        itemView.setOnClickListener(this);
    }

    public void setData(List list) {
        this.list = list;
        Log.d("List", String.valueOf(list));
        for (int i=0;i <list.size(); i++) {
            date.setText(list.get(i).toString());
        }
    }

    @Override
    public void onClick(View v) {
        Intent i=new Intent(activity, PaymentDetailsActivity.class);
        activity.startActivity(i);
    }
}
