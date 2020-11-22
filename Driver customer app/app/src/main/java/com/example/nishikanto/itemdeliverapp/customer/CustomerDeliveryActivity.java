package com.example.nishikanto.itemdeliverapp.customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.nishikanto.itemdeliverapp.ItemDeliveryApplication;
import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.fragment.ConfirmDeliveryFragment;
import com.example.nishikanto.itemdeliverapp.fragment.DeliveryCancelFragment;
import com.example.nishikanto.itemdeliverapp.fragment.DeliveryLocationFragment;
import com.example.nishikanto.itemdeliverapp.fragment.DeliverySuccessFragment;
import com.example.nishikanto.itemdeliverapp.fragment.ReadyForDeliveryFragment;
import com.example.nishikanto.itemdeliverapp.model.Trip;
import com.google.gson.GsonBuilder;

public class CustomerDeliveryActivity extends AppCompatActivity {
    private static final String TAG = CustomerDeliveryActivity.class.getSimpleName();
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ImageView issue_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_delivery);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = LayoutInflater.from(this).inflate(R.layout.custom_toolbar_set_schedule, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        actionBar.setCustomView(customView, params);
        actionBar.getCustomView().findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        issue_report = actionBar.getCustomView().findViewById(R.id.issue_report);
        issue_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getBaseContext(), CustomerReportIssueActivity.class);
                startActivity(i);
                finish();
            }
        });

        Trip trip = ((ItemDeliveryApplication) getApplicationContext()).getTrip();

        Log.d(TAG, "TrackTrip: "+ new GsonBuilder().setPrettyPrinting().create().toJson(trip));

//        fragmentManager = getSupportFragmentManager();
//        fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.frame_layout, new DeliveryLocationFragment());
//        fragmentTransaction.commit();

        if(trip.getStatus().matches("0")){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frame_layout, new ConfirmDeliveryFragment());
            fragmentTransaction.commit();
        } else if(trip.getStatus().matches("1") || trip.getStatus().matches("2") || trip.getStatus().matches("4")){
            Log.d(TAG, "STATUS: "+ trip.getStatus());
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frame_layout, new ReadyForDeliveryFragment());
            fragmentTransaction.commit();
        } else if(trip.getStatus().matches("5")){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frame_layout, new DeliveryLocationFragment());
            fragmentTransaction.commit();
        } else if(trip.getStatus().matches("6")){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frame_layout, new DeliverySuccessFragment());
            fragmentTransaction.commit();
        } else if(trip.getStatus().matches("8") || trip.getStatus().matches("9")){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.frame_layout, new DeliveryCancelFragment());
            fragmentTransaction.commit();
        }

    }
    //kI
}
