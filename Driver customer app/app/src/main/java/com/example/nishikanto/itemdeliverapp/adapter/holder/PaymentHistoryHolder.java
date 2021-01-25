package com.example.nishikanto.itemdeliverapp.adapter.holder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.driver.payment.PaymentDetailsActivity;
import com.example.nishikanto.itemdeliverapp.model.SinglePaymentHistory;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PaymentHistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private SinglePaymentHistory singlePaymentHistory;
    private Activity activity;

    @BindView(R.id.date)
    TextView date;

    @BindView(R.id.rides)
    TextView rides;

    @BindView(R.id.paid)
    TextView paid;

    @BindView(R.id.trip_code)
    TextView tripCode;


    public PaymentHistoryHolder(Activity activity, ViewGroup parent, @NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.activity = activity;
        itemView.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    public void setData(SinglePaymentHistory singlePaymentHistory) {
        this.singlePaymentHistory = singlePaymentHistory;
        Date date1;
        Log.d("TAG", "SinglePaymentHistory: "+ new GsonBuilder().setPrettyPrinting().create().toJson(singlePaymentHistory));

        try {
            @SuppressLint("SimpleDateFormat")
            DateFormat inputFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
            date1 = inputFormatter1.parse(singlePaymentHistory.getCreated_at__date());

            @SuppressLint("SimpleDateFormat")
            DateFormat outputFormatter1 = new SimpleDateFormat("d MMM, yyyy");
            String output1 = outputFormatter1.format(date1);
            date.setText(output1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        rides.setText(""+singlePaymentHistory.getTotal_trip());
        paid.setText(""+singlePaymentHistory.getTotal_bill());
        tripCode.setText(activity.getString(R.string.trip_code)+singlePaymentHistory.getTrips().get(0).getTrip_code());

    }

    @Override
    public void onClick(View v) {
        Intent i=new Intent(activity, PaymentDetailsActivity.class);
        i.putExtra("trip_history", singlePaymentHistory);
        activity.startActivity(i);
    }
}
