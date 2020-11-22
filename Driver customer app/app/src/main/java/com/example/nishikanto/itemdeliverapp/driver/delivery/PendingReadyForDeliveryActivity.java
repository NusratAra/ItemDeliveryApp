package com.example.nishikanto.itemdeliverapp.driver.delivery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.model.Trip;
import com.example.nishikanto.itemdeliverapp.utils.BaseUrlUtils;
import com.example.nishikanto.itemdeliverapp.utils.DataUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class PendingReadyForDeliveryActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = PendingReadyForDeliveryActivity.class.getSimpleName();

    private BottomSheetBehavior mBottomSheetBehaviour;
    private ImageView btnImageBack;
    private View blurView;
    private LinearLayout outerLayout;
    private DataUtils dataUtils;
    private Trip trip;
    private Toolbar toolbar;
    private  ActionBar actionBar;

    private TextView tripId;
    private TextView date;
    private TextView customerName;
    private TextView pickupAddress;
    private TextView deliverAddress;
    private String token;

    private CircleImageView companyImage;
    private TextView companyId;
    private TextView companyName;
    private TextView companyAddress;

    private ImageButton pickupCallRedirect;
    private ImageButton pickupMessageRedirect;
    private ImageButton deliveryCallRedirect;
    private ImageButton deliveryMessageRedirect;

    private FrameLayout pickupPoint;
    private FrameLayout deliveryPoint;

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_ready_for_delivery);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initToolbar();
        initFindView();
        getIntentValue();

        outerLayout.setOnClickListener(this);
        btnImageBack.setOnClickListener(this);
        blurView.setOnClickListener(this);

        pickupCallRedirect.setOnClickListener(this);
        pickupMessageRedirect.setOnClickListener(this);
        deliveryCallRedirect.setOnClickListener(this);
        deliveryMessageRedirect.setOnClickListener(this);
        pickupPoint.setOnClickListener(this);
        deliveryPoint.setOnClickListener(this);

    }

    private void getIntentValue() {

        trip = getIntent().getParcelableExtra("trip");

        Log.d(TAG, "getIntentValue: "+ trip);
        Log.d(TAG, "getIntentValue: "+ trip.getStatus());
        if(trip != null){
            Log.d(TAG, "TRIP_code: "+ trip.getTrip_code());
            tripId.setText("#"+trip.getTrip_code());
//            date.setText(trip.getCreated_at());

            companyId.setText(getString(R.string.earn)+" "+trip.getCompanies().get(0).getId());
            companyName.setText(trip.getCompanies().get(0).getUsername());
            companyAddress.setText(trip.getCompanies().get(0).getAddress());

            if(trip.getCompanies().get(0).getProfile_or_logo() != null){
                Glide.with(this).asDrawable()
                        .load(BaseUrlUtils.BASE_URL+trip.getCompanies().get(0).getProfile_or_logo())
                        .into(companyImage);
            }
            customerName.setText("Name: "+trip.getCustomer_name());
            pickupAddress.setText(trip.getPickup_area());
            deliverAddress.setText(trip.getDelivery_area());

            Date date1 = null;
            DateFormat inputFormatter1 = new SimpleDateFormat("yyyy-MM-dd");

//                date1 = inputFormatter1.parse(trip.getCreated_at());
            date1 = Calendar.getInstance().getTime();
            DateFormat outputFormatter1 = new SimpleDateFormat("MMM dd, hh:mm aaa");
            String output1 = outputFormatter1.format(date1);
            date.setText(output1);


        }
    }

    private void initFindView() {

        tripId = findViewById(R.id.trip_id);
        date = findViewById(R.id.date);
        customerName = findViewById(R.id.customer_name);
        pickupAddress = findViewById(R.id.pickup_address);
        deliverAddress = findViewById(R.id.delivery_address);

        companyId = findViewById(R.id.company_id);
        companyName = findViewById(R.id.company_name);
        companyAddress = findViewById(R.id.company_address);
        companyImage = findViewById(R.id.company_image);

        pickupCallRedirect = findViewById(R.id.pickup_phone_redirect);
        pickupMessageRedirect = findViewById(R.id.pickup_message_redirect);
        deliveryCallRedirect = findViewById(R.id.delivery_phone_redirect);
        deliveryMessageRedirect = findViewById(R.id.delivery_message_redirect);
        pickupPoint = findViewById(R.id.pickup_point);
        deliveryPoint = findViewById(R.id.delivery_point);

        btnImageBack = actionBar.getCustomView().findViewById(R.id.btn_image_back);
        blurView = findViewById(R.id.blur_view);

        outerLayout = actionBar.getCustomView().findViewById(R.id.outer_layout);


        View designBottomSheet = (View) findViewById(R.id.design_bottom_sheet);
        mBottomSheetBehaviour = BottomSheetBehavior.from(designBottomSheet);

        mBottomSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_COLLAPSED)
                    blurView.setVisibility(View.GONE);
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                blurView.setVisibility(View.VISIBLE);
                blurView.setAlpha(v);
            }
        });
    }

    private void initToolbar() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = LayoutInflater.from(this).inflate(R.layout.custom_toolbar_pending_delivery, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        actionBar.setCustomView(customView, params);
        actionBar.getCustomView().findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_image_back:
                mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.blur_view:
                mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.outer_layout:
                Log.d(TAG, "checkStatus: "+ trip.getStatus());
                if(trip.getStatus().equals(""+BaseUrlUtils.READY_FOR_DELIVERY)){
                    Intent i=new Intent(getBaseContext(), ReadyForDeliveryActivity.class);
                    startActivity(i);
                    finish();
                }
                break;
            case R.id.pickup_phone_redirect:
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", trip.getCustomer_phone(), null)));
                break;
            case R.id.pickup_message_redirect:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", trip.getCustomer_phone(), null)));
                break;
            case R.id.delivery_phone_redirect:
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", trip.getCustomer_phone(), null)));
                break;
            case R.id.delivery_message_redirect:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", trip.getCustomer_phone(), null)));
                break;
            case R.id.pickup_point:
                startMapIntent(trip.getPickup_latitude(), trip.getPickup_longitude());
                break;
            case R.id.delivery_point:
                startMapIntent(trip.getDelivery_latitude(), trip.getDelivery_longitude());
                break;
        }
    }

    private void startMapIntent(String latitude, String longitude) {

        // Creates an Intent that will load a map of San Francisco
        Uri gmmIntentUri = Uri.parse("geo:"+ latitude+ ","+ longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }
}
