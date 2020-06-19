package com.example.nishikanto.itemdeliverapp.adapter.holder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nishikanto.itemdeliverapp.driver.DeliveryDetailsActivity;
import com.example.nishikanto.itemdeliverapp.driver.DriverHomeActivity;
import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.adapter.DeliveryPendingAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.blurry.Blurry;

public class DeliveryPendingHolder extends BaseViewHolder implements View.OnClickListener {
    private final DeliveryPendingAdapter deliveryPendingAdapter;
    private Activity activity;
    private TextView cancel;
    private TextView confirm;


    @BindView(R.id.details_delivery)
    Button detailsDelivery;

    @BindView(R.id.cancel_delivery)
    Button cancelDelivery;

    public DeliveryPendingHolder(DeliveryPendingAdapter deliveryPendingAdapter, Activity activity, View view) {
        super(view);
        ButterKnife.bind(this, view);
        this.activity = activity;
        view.setOnClickListener(this);
        cancelDelivery.setOnClickListener(this);
        detailsDelivery.setOnClickListener(this);

        this.deliveryPendingAdapter = deliveryPendingAdapter;

    }



    @Override
    protected void clear() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_delivery:
                showDialog();
                break;
            default:
                Intent item=new Intent(activity, DeliveryDetailsActivity.class);
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
                alertDialog.dismiss();
                deliveryPendingAdapter.onDialogConfirmClicked(getAdapterPosition());
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
}
