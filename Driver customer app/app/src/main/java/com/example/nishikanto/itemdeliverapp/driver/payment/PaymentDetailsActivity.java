package com.example.nishikanto.itemdeliverapp.driver.payment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.adapter.PaymentDetailsAdapter;
import com.example.nishikanto.itemdeliverapp.model.SinglePaymentHistory;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentDetailsActivity extends AppCompatActivity {
    private static final String TAG = PaymentDetailsActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private PaymentDetailsAdapter paymentHistoryAdaper;

    private Toolbar toolbar;
    private ActionBar actionBar;
    private SinglePaymentHistory singlePaymentHistory;

    private TextView date;
    private TextView rides;
    private TextView paid;
    private TextView tripCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initToolbar();
        findViewValue();
        getIntentValue();


    }

    private void findViewValue() {

        date = findViewById(R.id.date);
        rides = findViewById(R.id.rides);
        paid = findViewById(R.id.paid);
        tripCode = findViewById(R.id.trip_code);
    }

    @SuppressLint("SetTextI18n")
    private void getIntentValue() {
        singlePaymentHistory = getIntent().getParcelableExtra("trip_history");
        Log.d(TAG, "TripDetails: "+ new GsonBuilder().setPrettyPrinting().create().toJson(singlePaymentHistory));
        rides.setText(""+ singlePaymentHistory.getTotal_trip());
        paid.setText(getString(R.string.total_earn)+" "+ singlePaymentHistory.getTotal_bill());
        tripCode.setText(getString(R.string.trip_code)+ singlePaymentHistory.getTrips().get(0).getTrip_code());

        Date date1;
        try {
            @SuppressLint("SimpleDateFormat")
            DateFormat inputFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
            date1 = inputFormatter1.parse(singlePaymentHistory.getCreated_at__date());

            @SuppressLint("SimpleDateFormat")
            DateFormat outputFormatter1 = new SimpleDateFormat("d MMM, yyyy");
            String output1 = outputFormatter1.format(date1);
            date.setText(output1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        initRecyclerView();

    }

    private void initRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_payment);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        paymentHistoryAdaper = new PaymentDetailsAdapter(this, singlePaymentHistory.getTrips());
        recyclerView.setAdapter(paymentHistoryAdaper);
    }

    private void initToolbar() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = LayoutInflater.from(this).inflate(R.layout.custom_toolbar_payment_2, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        actionBar.getCustomView().findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
