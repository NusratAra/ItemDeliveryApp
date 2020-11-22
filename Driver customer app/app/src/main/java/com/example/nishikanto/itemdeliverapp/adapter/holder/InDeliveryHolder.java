package com.example.nishikanto.itemdeliverapp.adapter.holder;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.driver.delivery.DeliveredActivity;
import com.example.nishikanto.itemdeliverapp.driver.delivery.InDeliveryActivity;
import com.example.nishikanto.itemdeliverapp.driver.delivery.ReadyForDeliveryActivity;
import com.example.nishikanto.itemdeliverapp.model.Trip;
import com.example.nishikanto.itemdeliverapp.utils.BaseUrlUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InDeliveryHolder extends RecyclerView.ViewHolder {
    private static final String TAG = InDeliveryHolder.class.getSimpleName();

    private Activity activity;
    private TextView cancel;
    private ArrayList<Trip> tripArrayList;

    private String statusFlag = "";
    private Trip trip;

    @BindView(R.id.company_id)
    TextView companyId;

    @BindView(R.id.company_name)
    TextView companyName;

    @BindView(R.id.product_image)
    ImageView productImage;


    @BindView(R.id.pickup_address)
    TextView pickupAddress;

    @BindView(R.id.delivery_address)
    TextView deliveryAddress;

    @BindView(R.id.button_ready_for_delivery)
    ConstraintLayout buttonReadyForDelivery;

    @BindView(R.id.button_in_delivery)
    ConstraintLayout buttonInDelivery;

    @BindView(R.id.card_view)
    CardView cardViewLayout;

    public InDeliveryHolder(Activity activity, View view, ArrayList<Trip> tripArrayList) {
        super(view);
        ButterKnife.bind(this, view);
        this.activity = activity;
        this.tripArrayList = tripArrayList;
    }

    public void setData(Trip trip){
        this.trip = trip;
        Log.d(TAG, "TripIndividual: "+ trip.getStatus());

        companyId.setText(activity.getString(R.string.earn) + " " + this.trip.getCompanies().get(0).getId());
        companyName.setText(this.trip.getCompanies().get(0).getUsername());
        if(this.trip.getCompanies().get(0).getProfile_or_logo() != null){
            Glide.with(activity).asDrawable()
                    .load(BaseUrlUtils.BASE_URL+trip.getCompanies().get(0).getProfile_or_logo())
                    .into(productImage);
            //TODO productImage -> companyImage
        }

        if(this.trip.getStatus().equals(""+BaseUrlUtils.IN_DELIVERY) || this.trip.getStatus().equals(""+BaseUrlUtils.DELIVERED)){
            statusFlag = "InDelivery";
            buttonReadyForDelivery.setVisibility(View.GONE);
            buttonInDelivery.setVisibility(View.VISIBLE);
        } else if(this.trip.getStatus().equals(""+BaseUrlUtils.READY_FOR_DELIVERY)){
            statusFlag = "ReadyForDelivery";
            buttonReadyForDelivery.setVisibility(View.VISIBLE);
            buttonInDelivery.setVisibility(View.GONE);
        }
//        else if(){
//            statusFlag = "Delivered";
//            buttonReadyForDelivery.setVisibility(View.VISIBLE);
//            buttonInDelivery.setVisibility(View.GONE);
//        } else if(trip.getStatus().equals(""+BaseUrlUtils.ACCEPT_BY_DRIVER)){
//            statusFlag = "PendingDelivery";
//            buttonReadyForDelivery.setVisibility(View.VISIBLE);
//            buttonInDelivery.setVisibility(View.GONE);
//        }

        pickupAddress.setText(trip.getPickup_area());
        deliveryAddress.setText(trip.getDelivery_area());
        cardViewLayout.setOnClickListener(cardClickListener);
    }

    View.OnClickListener cardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(trip.getStatus() != null){
                Log.d(TAG, "TripStatus: "+ trip.getStatus());
            }

            if(trip.getStatus().matches(""+ BaseUrlUtils.IN_DELIVERY)){
                Log.d(TAG, "InDelivery: ");
                Intent item=new Intent(activity, InDeliveryActivity.class);
                item.putExtra("trip", tripArrayList.get(getAdapterPosition()));
                activity.startActivity(item);

            } else if(trip.getStatus().matches(""+ BaseUrlUtils.READY_FOR_DELIVERY)){
                Log.d(TAG, "ReadyForDelivery: ");
                Intent item=new Intent(activity, ReadyForDeliveryActivity.class);
                item.putExtra("trip", tripArrayList.get(getAdapterPosition()));
                activity.startActivity(item);

            } else if(trip.getStatus().matches(""+ BaseUrlUtils.DELIVERED)){
                Log.d(TAG, "Delivered: ");
                Intent item=new Intent(activity, DeliveredActivity.class);
                item.putExtra("trip", tripArrayList.get(getAdapterPosition()));
                activity.startActivity(item);
            }
        }
    };

}
