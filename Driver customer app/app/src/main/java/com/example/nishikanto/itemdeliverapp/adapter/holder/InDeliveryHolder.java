package com.example.nishikanto.itemdeliverapp.adapter.holder;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;

public class InDeliveryHolder extends BaseViewHolder {

    private Activity activity;
    private TextView cancel;

    public InDeliveryHolder(Activity activity, View view) {
        super(view);
        ButterKnife.bind(this, view);
        this.activity = activity;

    }

    @Override
    protected void clear() {

    }
}
