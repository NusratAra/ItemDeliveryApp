package com.example.nishikanto.itemdeliverapp.driver;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.driver.delivery.PendingReadyForDeliveryActivity;
import com.example.nishikanto.itemdeliverapp.model.Trip;
import com.example.nishikanto.itemdeliverapp.services.NoConnectivityException;
import com.example.nishikanto.itemdeliverapp.services.RetrofitInstance;
import com.example.nishikanto.itemdeliverapp.services.TripAuthenticationService;
import com.example.nishikanto.itemdeliverapp.utils.BaseUrlUtils;
import com.example.nishikanto.itemdeliverapp.utils.DataUtils;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = DeliveryDetailsActivity.class.getSimpleName();
    private TextView tvTitle;
    private Button buttonAccept;
    private Button buttonCancel;
    private TextView tripId;
    private TextView date;
    private TextView customerName;
    private TextView pickupAddress;
    private TextView deliverAddress;
    private String token;
    private DataUtils dataUtils;
    private Trip trip;

    private ImageButton pickupCallRedirect;
    private ImageButton pickupMessageRedirect;
    private ImageButton deliveryCallRedirect;
    private ImageButton deliveryMessageRedirect;

    private FrameLayout pickupPoint;
    private FrameLayout deliveryPoint;

    private CircleImageView companyImage;
    private TextView companyId;
    private TextView companyName;
    private TextView companyAddress;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_details);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        dataUtils = new DataUtils(getApplicationContext());

        initToolbar();
        initFindView();

        getIntentValue();

        buttonCancel.setOnClickListener(this);
        buttonAccept.setOnClickListener(this);

        pickupCallRedirect.setOnClickListener(this);
        pickupMessageRedirect.setOnClickListener(this);
        deliveryCallRedirect.setOnClickListener(this);
        deliveryMessageRedirect.setOnClickListener(this);
        pickupPoint.setOnClickListener(this);
        deliveryPoint.setOnClickListener(this);
    }

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    private void getIntentValue() {

//        Bundle bundle = getIntent().getBundleExtra("trip");
//        if (bundle != null) {
//            trip = bundle.getParcelable("trip_bundle");
//        }
//
        trip = getIntent().getParcelableExtra("trip");
        Log.d(TAG, "getIntentValue: "+ trip);
        if(trip != null){
            Log.d(TAG, "TRIP_code: "+ trip.getTrip_code());
            tripId.setText("#"+trip.getTrip_code());
//            date.setText(trip.getCreated_at());
            Log.d(TAG, "BodyTrip: "+ new GsonBuilder().setPrettyPrinting().create().toJson(trip));


            Log.d(TAG, "CompanyId: "+ trip.getCompanies().get(0).getId());
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

        buttonAccept = findViewById(R.id.btn_accept);
        buttonCancel = findViewById(R.id.btn_cancel);
        tripId = findViewById(R.id.trip_id);
        date = findViewById(R.id.date);

        companyId = findViewById(R.id.company_id);
        companyName = findViewById(R.id.company_name);
        companyAddress = findViewById(R.id.company_address);
        companyImage = findViewById(R.id.company_image);

        customerName = findViewById(R.id.customer_name);
        pickupAddress = findViewById(R.id.pickup_address);
        deliverAddress = findViewById(R.id.delivery_address);

        pickupCallRedirect = findViewById(R.id.pickup_phone_redirect);
        pickupMessageRedirect = findViewById(R.id.pickup_message_redirect);
        deliveryCallRedirect = findViewById(R.id.delivery_phone_redirect);
        deliveryMessageRedirect = findViewById(R.id.delivery_message_redirect);
        pickupPoint = findViewById(R.id.pickup_point);
        deliveryPoint = findViewById(R.id.delivery_point);
    }

    private void initToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

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

        Typeface custom_font = ResourcesCompat.getFont(this, R.font.poppins_medium);
        tvTitle = actionBar.getCustomView().findViewById(R.id.tvTitle);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.details);
        tvTitle.setTypeface(custom_font);
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.colorBlackLight2));
        actionBar.setTitle(tvTitle.getText());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_accept:
                statusChangeCall(BaseUrlUtils.ACCEPT_BY_DRIVER);
                break;
            case R.id.btn_cancel:
                statusChangeCall(BaseUrlUtils.DENIED_BY_DRIVER);
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

    private void statusChangeCall(int statusValue) {

        token = "Bearer "+ dataUtils.getStr("access");
//        Log.d(TAG, "TRIP: "+ trip.getStatus());

        TripAuthenticationService tripAuthenticationService = RetrofitInstance.getServiceCall(getApplicationContext());
//        Call<Trip> tripCall = tripAuthenticationService.changeStatus(token, trip.getId(), statusValue);
        Call<Trip> tripCall = tripAuthenticationService.changeStatus(token, trip.getId(), statusValue);


        tripCall.enqueue(new Callback<Trip>() {
            @Override
            public void onResponse(Call<Trip> call, Response<Trip> response) {
                if(response.body() != null){
                    Log.d(TAG, "onResponseTripCode: "+ response.body().getTrip_code());
                    Log.d(TAG, "onResponseStatus: "+ response.body().getStatus());

                    //TODO trip = response.body();

                    if(statusValue == BaseUrlUtils.ACCEPT_BY_DRIVER){
                        Log.d(TAG, "TRIP_Status1: "+ trip.getStatus());
                        Intent i=new Intent(getBaseContext(), PendingReadyForDeliveryActivity.class);
                        i.putExtra("trip", trip);
                        startActivity(i);
                        finish();
                    } else if(statusValue == BaseUrlUtils.DENIED_BY_DRIVER){
                        Log.d(TAG, "TRIP_Status2: "+ trip.getStatus());
                        onBackPressed();
                    }

                }
                if(response.errorBody() !=  null){
                    try {
                        Log.d(TAG, "onResponse: "+ response.errorBody().string());
                        Toast.makeText(getApplicationContext(), getString(R.string.invalid_status), Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Trip> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    Log.e(TAG, "onFailureThrowEx: " + t.getMessage());
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.server_error_customer), Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.server_error), Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            }
        });
    }
}
