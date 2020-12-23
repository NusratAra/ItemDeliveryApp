package com.example.nishikanto.itemdeliverapp.driver.payment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.adapter.PaymentHistoryAdaper;
import com.example.nishikanto.itemdeliverapp.model.PaymentHistory;
import com.example.nishikanto.itemdeliverapp.model.SinglePaymentHistory;
import com.example.nishikanto.itemdeliverapp.services.NoConnectivityException;
import com.example.nishikanto.itemdeliverapp.services.RetrofitInstance;
import com.example.nishikanto.itemdeliverapp.services.TripAuthenticationService;
import com.example.nishikanto.itemdeliverapp.utils.DataUtils;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentHistoryActivity extends AppCompatActivity {
    private static final String TAG = PaymentHistoryActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private PaymentHistoryAdaper paymentHistoryAdaper;
    private ActionBar actionBar;
    private Toolbar toolbar;
    private TextView totalRides;
    private TextView totalEarns;

    private int rideNumber;
    private float earnAmount;

    private ArrayList<SinglePaymentHistory> paymentHistoryList;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initToolbar();
        initfindView();

        historyCall();
    }

    private void initfindView() {

        totalRides = findViewById(R.id.total_ride);
        totalEarns = findViewById(R.id.total_earn);
    }

    @SuppressLint("SetTextI18n")
    private void paymentSum() {
        Log.d(TAG, "paymentSum: "+ rideNumber+"+"+earnAmount);
        totalRides.setText(""+rideNumber);
        totalEarns.setText(getString(R.string.total_earn)+" "+earnAmount);
    }

    private void historyCall() {
        DataUtils dataUtils = new DataUtils(getApplicationContext());
        String token = "Bearer "+ dataUtils.getStr("access");
        Log.d(TAG, "historyCall: "+ token);

        paymentHistoryList = new ArrayList<>();

        TripAuthenticationService tripAuthenticationService = RetrofitInstance.getServiceCall(getApplicationContext());
        Call<PaymentHistory> paymentHistoryCall = tripAuthenticationService.getPaymentHistory(token);

        paymentHistoryCall.enqueue(new Callback<PaymentHistory>() {
            @Override
            public void onResponse(Call<PaymentHistory> call, Response<PaymentHistory> response) {

                if(response.body() != null){

                    paymentHistoryList = response.body().getDriver_stats().getDate_wise();
                    Log.d(TAG, "PaymentHistory: "+ new GsonBuilder().setPrettyPrinting().create().toJson(paymentHistoryList));

                    rideNumber = (int) response.body().getDriver_stats().getTotal_trip_sum().getTotal_trip__sum();
                    earnAmount = response.body().getDriver_stats().getTotal_income_sum().getTotal_bill__sum();

                    Log.d(TAG, "Ride&Earn: "+ rideNumber+ "+"+ earnAmount);
                    paymentSum();
                    recyclerViewCall();

                }
                if(response.errorBody() != null){
                    Log.d(TAG, "onResponseE: "+ response.message());
                    Log.d(TAG, "PaymentHistoryError: "+ new GsonBuilder().setPrettyPrinting().create().toJson(response.errorBody()));

                }
            }

            @Override
            public void onFailure(Call<PaymentHistory> call, Throwable t) {
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

    private void recyclerViewCall() {

//TODO: Calender icon
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_payment);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        paymentHistoryAdaper = new PaymentHistoryAdaper(this, paymentHistoryList);
        recyclerView.setAdapter(paymentHistoryAdaper);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initToolbar() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = LayoutInflater.from(this).inflate(R.layout.custom_toolbar_payment, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        actionBar.setCustomView(customView, params);
        actionBar.getCustomView().findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolbar.setElevation(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
