package com.example.nishikanto.itemdeliverapp.adapter.holder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.driver.trip.IndividualPaymentActivity;
import com.example.nishikanto.itemdeliverapp.model.Trip;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentDetailsHolder extends RecyclerView.ViewHolder {
    private Activity activity;
    private Trip trip;

    @BindView(R.id.date)
    TextView dateText;

    @BindView(R.id.trip_id)
    TextView tripId;

    @BindView(R.id.delivery_address)
    TextView deliveryAddress;

    @BindView(R.id.pickup_address)
    TextView pickupAddress;

    public PaymentDetailsHolder(Activity activity, @NonNull View itemView) {
        super(itemView);
        this.activity = activity;
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent item=new Intent(activity, IndividualPaymentActivity.class);
                item.putExtra("status", 2);
                item.putExtra("trip", trip);
                activity.startActivity(item);
                activity.finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void setData(Trip trip) {
        this.trip = trip;
        Date date1;

        try {
            @SuppressLint("SimpleDateFormat")
            DateFormat inputFormatter1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
            date1 = inputFormatter1.parse(this.trip.getExpire_date());

            @SuppressLint("SimpleDateFormat")
            DateFormat outputFormatter1 = new SimpleDateFormat("d MMM, yyyy");
            String output1 = outputFormatter1.format(date1);
            dateText.setText(""+output1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tripId.setText(activity.getString(R.string.total_earn)+ " "+ this.trip.getId());
        deliveryAddress.setText(""+ this.trip.getDelivery_area());
        pickupAddress.setText(""+ this.trip.getPickup_area());

    }
}
