package com.example.nishikanto.itemdeliverapp.adapter.holder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class TripHistoryHolder extends RecyclerView.ViewHolder {
    private Activity activity;
    private Trip trip;

    @BindView(R.id.date)
    TextView date;

    @BindView(R.id.company_icon)
    ImageView productImage;

    @BindView(R.id.pickup_address)
    TextView pickupAddress;

    @BindView(R.id.delivery_address)
    TextView deliveryAddress;

    @BindView(R.id.company_id)
    TextView companyId;

    public TripHistoryHolder(Activity activity, ViewGroup parent, @NonNull View itemView) {
        super(itemView);
        this.activity = activity;
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent item=new Intent(activity, IndividualPaymentActivity.class);
                item.putExtra("status", 1);
                item.putExtra("trip", trip);
                activity.startActivity(item);
            }
        });
    }



    @SuppressLint("SetTextI18n")
    public void setData(Trip trip) {
        this.trip = trip;
        Date date1;

        try {
            @SuppressLint("SimpleDateFormat")
            DateFormat inputFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
            date1 = inputFormatter1.parse(trip.getExpire_date());

            @SuppressLint("SimpleDateFormat")
            DateFormat outputFormatter1 = new SimpleDateFormat("d MMM, yyyy");
            String output1 = outputFormatter1.format(date1);
            date.setText(output1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        deliveryAddress.setText(""+trip.getDelivery_area());
        pickupAddress.setText(""+trip.getPickup_area());
        companyId.setText(activity.getString(R.string.earn)+" "+trip.getCompany_id());

    }
}
