package com.example.nishikanto.itemdeliverapp.adapter.holder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.adapter.DeliveryPendingAdapter;
import com.example.nishikanto.itemdeliverapp.driver.DeliveryDetailsActivity;
import com.example.nishikanto.itemdeliverapp.driver.DriverHomeActivity;
import com.example.nishikanto.itemdeliverapp.model.Trip;
import com.example.nishikanto.itemdeliverapp.model.Trips;
import com.example.nishikanto.itemdeliverapp.services.NoConnectivityException;
import com.example.nishikanto.itemdeliverapp.services.RetrofitInstance;
import com.example.nishikanto.itemdeliverapp.services.TripAuthenticationService;
import com.example.nishikanto.itemdeliverapp.utils.BaseUrlUtils;
import com.example.nishikanto.itemdeliverapp.utils.DataUtils;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryPendingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String TAG = DeliveryPendingHolder.class.getSimpleName();
    private final DeliveryPendingAdapter deliveryPendingAdapter;
    private Activity activity;
    private TextView cancel;
    private TextView confirm;
    private ArrayList<Trip> tripArrayList;

    private String token;
    private DataUtils dataUtils;
    private Trip trip;


    @BindView(R.id.details_delivery)
    Button detailsDelivery;

    @BindView(R.id.cancel_delivery)
    Button cancelDelivery;

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

    public DeliveryPendingHolder(DeliveryPendingAdapter deliveryPendingAdapter, Activity activity, View view, ArrayList<Trip> tripArrayList) {
        super(view);
        ButterKnife.bind(this, view);
        this.activity = activity;
        this.tripArrayList = tripArrayList;
        trip = this.activity.getIntent().getParcelableExtra("trip");
        dataUtils = new DataUtils(this.activity);


        view.setOnClickListener(this);
        cancelDelivery.setOnClickListener(this);
        detailsDelivery.setOnClickListener(this);

        this.deliveryPendingAdapter = deliveryPendingAdapter;

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.cancel_delivery:
                //TODO update status
                showDialog();
                break;
            default:
                Intent item=new Intent(activity, DeliveryDetailsActivity.class);
                item.putExtra("trip", tripArrayList.get(getAdapterPosition()));
                activity.startActivity(item);
        }
    }

    public void showDialog() {
        LayoutInflater factory = LayoutInflater.from(activity);
        final View deleteDialogView = factory.inflate(R.layout.dialog_cancel_trip, null);
        final Dialog alertDialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        alertDialog.setContentView(deleteDialogView);

        cancel = alertDialog.findViewById(R.id.cancel);
        confirm = alertDialog.findViewById(R.id.confirm);

//        cancel.setCustomTextFont(R.font.poppins_medium);

        deleteDialogView.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusChangeCall(BaseUrlUtils.DENIED_BY_DRIVER);
                alertDialog.dismiss();
            }
        });

        deleteDialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        Blurry.with(activity)
                .radius(25)
                .sampling(1)
                .color(Color.argb(80, 0, 0, 0))
                .capture(((DriverHomeActivity)activity).findViewById(R.id.mainLayout))
                .into(alertDialog.findViewById(R.id.blurView));


        alertDialog.show();

    }

    @SuppressLint("SetTextI18n")
    public void setData(Trip trip) {
        this.trip = trip;

        Log.d(TAG, "TripDetails: "+ new GsonBuilder().setPrettyPrinting().create().toJson(this.trip));
        companyId.setText(activity.getString(R.string.earn) + " " + this.trip.getCompanies().get(0).getId());
        companyName.setText(this.trip.getCompanies().get(0).getUsername());
        if(this.trip.getCompanies().get(0).getProfile_or_logo() != null){
            Glide.with(activity).asDrawable()
                    .load(BaseUrlUtils.BASE_URL+trip.getCompanies().get(0).getProfile_or_logo())
                    .into(productImage);
        }
        pickupAddress.setText(this.trip.getPickup_area());
        deliveryAddress.setText(this.trip.getDelivery_area());
    }

    private void statusChangeCall(int statusValue) {
        Trips trips = new Trips();

        token = "Bearer "+ dataUtils.getStr("access");
        Log.d(TAG, "TRIP: "+ trip.getStatus());

        TripAuthenticationService tripAuthenticationService = RetrofitInstance.getServiceCall(activity);
        Call<Trip> tripCall = tripAuthenticationService.changeStatus(token, trip.getId(), statusValue);

        tripCall.enqueue(new Callback<Trip>() {
            @Override
            public void onResponse(Call<Trip> call, Response<Trip> response) {
                if(response.body() != null){
                    Log.d(TAG, "onResponseStatus: "+ response.body().getStatus());
                    deliveryPendingAdapter.onDialogConfirmClicked(getAdapterPosition());

                }

                if(response.errorBody() !=  null){
                    try {
                        Log.d(TAG, "onResponse: "+ response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Trip> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    Log.e(TAG, "onFailureThrowEx: " + t.getMessage());
                    Toast toast = Toast.makeText(activity, activity.getString(R.string.server_error_customer), Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(activity, activity.getString(R.string.server_error), Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            }
        });
    }
}
