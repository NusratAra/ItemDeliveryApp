package com.example.nishikanto.itemdeliverapp.driver.trip;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.model.Trip;
import com.example.nishikanto.itemdeliverapp.utils.BaseUrlUtils;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class IndividualPaymentActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvTitle;
    private LinearLayout statusLayout;
    private LinearLayout callRedirectLayout;
    private Toolbar toolbar;
    private  ActionBar actionBar;
    private Trip trip;

    private TextView tripId;
    private TextView date;
    private CircleImageView companyIcon;
    private TextView companyId;
    private TextView companyName;
    private TextView companyAddress;
    private TextView pickupAddress;
    private TextView deliveryAddress;
    private TextView customerName;

    private ImageButton pickupCallRedirect;
    private ImageButton pickupMessageRedirect;
    private ImageButton deliveryCallRedirect;
    private ImageButton deliveryMessageRedirect;

    private FrameLayout pickupPoint;
    private FrameLayout deliveryPoint;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_payment);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initToolbar();
        findViewInit();

        statusLayout = findViewById(R.id.status_layout);
        callRedirectLayout = findViewById(R.id.call_redirect_layout);

        int statusValue = getIntent().getIntExtra("status", 0);

        getIntentValue();
        if(statusValue == 1){
            statusLayout.setVisibility(View.VISIBLE);
            getIntentValueTrip();
        } else if(statusValue == 2){
            getIntentValuePayment();
        }

        pickupCallRedirect.setOnClickListener(this);
        pickupMessageRedirect.setOnClickListener(this);
        deliveryCallRedirect.setOnClickListener(this);
        deliveryMessageRedirect.setOnClickListener(this);
        pickupPoint.setOnClickListener(this);
        deliveryPoint.setOnClickListener(this);

    }

    @SuppressLint("SetTextI18n")
    private void getIntentValuePayment() {
        /////////////////////////////////
        //TODO company info
        companyName.setText(""+ trip.getCustomer_name());
        companyAddress.setText(""+trip.getDelivery_area());

        if(trip.getProduct_image() != null){
            Glide.with(this).asDrawable()
                    .load(BaseUrlUtils.BASE_URL+trip.getProduct_image())
                    .into(companyIcon);
        }
    }

    @SuppressLint("SetTextI18n")
    private void getIntentValueTrip() {
        companyName.setText(""+ trip.getCompanies().get(0).getUsername());
        companyAddress.setText(""+trip.getCompanies().get(0).getAddress());

        if(trip.getCompanies().get(0).getProfile_or_logo() != null){
            Glide.with(this).asDrawable()
                    .load(BaseUrlUtils.BASE_URL+trip.getCompanies().get(0).getProfile_or_logo())
                    .into(companyIcon);
        }
    }

    private void findViewInit() {

        tripId = findViewById(R.id.trip_id);
        date = findViewById(R.id.date);
        companyIcon = findViewById(R.id.company_image);
        companyId = findViewById(R.id.company_id);
        companyName = findViewById(R.id.company_name);
        companyAddress = findViewById(R.id.company_address);
        pickupAddress = findViewById(R.id.pickup_address);
        deliveryAddress = findViewById(R.id.delivery_address);
        customerName = findViewById(R.id.customer_name);

        pickupCallRedirect = findViewById(R.id.pickup_phone_redirect);
        pickupMessageRedirect = findViewById(R.id.pickup_message_redirect);
        deliveryCallRedirect = findViewById(R.id.delivery_phone_redirect);
        deliveryMessageRedirect = findViewById(R.id.delivery_message_redirect);
        pickupPoint = findViewById(R.id.pickup_point);
        deliveryPoint = findViewById(R.id.delivery_point);

    }

    @SuppressLint("SetTextI18n")
    private void getIntentValue() {
        this.trip = getIntent().getParcelableExtra("trip");
        Log.d("TAG", "TripHistory: "+ new GsonBuilder().setPrettyPrinting().create().toJson(this.trip));
        tripId.setText(getString(R.string.trip_code)+ trip.getTrip_code());
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

        companyId.setText(getString(R.string.total_earn) + " "+ trip.getCompany_id());

        pickupAddress.setText(""+trip.getPickup_area());
        deliveryAddress.setText(""+trip.getDelivery_area());
        customerName.setText(getString(R.string.name)+ " "+ trip.getCustomer_name());


    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Typeface custom_font = ResourcesCompat.getFont(this, R.font.poppins_medium);

        actionBar = getSupportActionBar();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = LayoutInflater.from(this).inflate(R.layout.custom_toolbar, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        actionBar.setCustomView(customView, params);
        actionBar.getCustomView().findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvTitle = actionBar.getCustomView().findViewById(R.id.tvTitle);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.details);
        tvTitle.setTypeface(custom_font);
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.colorBlackLight2));
        actionBar.setTitle(tvTitle.getText());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
