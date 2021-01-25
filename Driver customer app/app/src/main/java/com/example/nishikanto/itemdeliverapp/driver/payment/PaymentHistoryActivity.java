package com.example.nishikanto.itemdeliverapp.driver.payment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
    private ImageView calenderFilter;

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

    @SuppressLint("SimpleDateFormat")
    private void calenderFilterCall() {


        calenderFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar myCalendar = Calendar.getInstance();

                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String strDate = format.format(myCalendar.getTime());
                        Log.d(TAG, "GetCalenderTime: "+ strDate);
                        paymentHistoryAdaper.filterItemByDate(strDate);
                    }

                };

                new DatePickerDialog(PaymentHistoryActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });




    }

    private void initfindView() {

        totalRides = findViewById(R.id.total_ride);
        totalEarns = findViewById(R.id.total_earn);
        calenderFilter = findViewById(R.id.calender);
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
                    calenderFilterCall();

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
//        paymentHistoryAdaper.filterItemAllDate();
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
