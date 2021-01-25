package com.example.nishikanto.itemdeliverapp.driver.trip;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.adapter.TripHistoryAdapter;
import com.example.nishikanto.itemdeliverapp.model.Trip;
import com.example.nishikanto.itemdeliverapp.model.Trips;
import com.example.nishikanto.itemdeliverapp.services.NoConnectivityException;
import com.example.nishikanto.itemdeliverapp.services.RetrofitInstance;
import com.example.nishikanto.itemdeliverapp.services.TripAuthenticationService;
import com.example.nishikanto.itemdeliverapp.utils.BaseUrlUtils;
import com.example.nishikanto.itemdeliverapp.utils.DataUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripHistoryActivity extends AppCompatActivity {

    private static final String TAG = TripHistoryActivity.class.getSimpleName();

    private ActionBar actionBar;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TripHistoryAdapter tripHistoryAdaper;
    private ImageView filter;

    private BottomSheetBehavior mBottomSheetBehaviour;
    private View blurView;

    private View view_all;
    private View view_complete;
    private View view_expired;

    private ImageView tik_all;
    private ImageView tik_complete;
    private ImageView tik_expired;
    private TextView totalEarn;
    private ArrayList<Trip> tripArrayList;

    private RadioGroup radioGroup;
    private ConstraintLayout allTripLayout;
    private ConstraintLayout completedTripLayout;
    private ConstraintLayout expiredTripLayout;
    private ConstraintLayout cancelTripLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);

        initToolbar();
        initView();

        tripHistoryCall();

        allTripLayout.setOnClickListener(textAllListener);
        completedTripLayout.setOnClickListener(textCompleteListener);
        expiredTripLayout.setOnClickListener(textExpiredListener);
        cancelTripLayout.setOnClickListener(textCancelListener);

        bottomSheetClick();

        //TODO: funnel icon


    }

    private void tripHistoryCall() {

        DataUtils dataUtils = new DataUtils(getApplicationContext());
        String token = "Bearer "+ dataUtils.getStr("access");
        tripArrayList = new ArrayList<>();

        TripAuthenticationService tripAuthenticationService = RetrofitInstance.getServiceCall(getApplicationContext());
        Call<Trips> tripCall = tripAuthenticationService.getTripHistory(token);
        tripCall.enqueue(new Callback<Trips>() {
            @Override
            public void onResponse(Call<Trips> call, Response<Trips> response) {
                if(response.body() != null){
                    Log.d(TAG, "TripHistory: "+ new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                    Log.d(TAG, "Total: "+ response.body().getTotal());
                    totalEarn.setText(getString(R.string.earn)+" "+response.body().getTotal());
                    initRecyclerView(response.body().getTrips());

                }
                if(response.errorBody() != null){
                    Log.d(TAG, "onResponseE: "+ response.message());
                    Log.d(TAG, "TripHistoryError: "+ new GsonBuilder().setPrettyPrinting().create().toJson(response.errorBody()));

                }
            }

            @Override
            public void onFailure(Call<Trips> call, Throwable t) {
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


    private void bottomSheetClick() {

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

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void initRecyclerView(ArrayList<Trip> trips) {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_payment);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        tripHistoryAdaper = new TripHistoryAdapter(this, trips);
        recyclerView.setAdapter(tripHistoryAdaper);

        paidClickListenerCall();

    }

    private void paidClickListenerCall() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = group.findViewById(checkedId);
                int index = group.indexOfChild(radioButton);

                switch (index){
                    case 0:
                        tripHistoryAdaper.filterAllItem();
                        break;
                    case 1:
                        tripHistoryAdaper.filterItemByPaid(true);
                        break;
                    case 2:
                        tripHistoryAdaper.filterItemByPaid(false);
                        break;
                }
            }
        });
    }

    private void initView() {

        filter = actionBar.getCustomView().findViewById(R.id.filter);
        blurView = findViewById(R.id.blur_view);

        radioGroup = findViewById(R.id.radio_group);

        allTripLayout = findViewById(R.id.all_trip_layout);
        completedTripLayout = findViewById(R.id.completed_trip_layout);
        expiredTripLayout = findViewById(R.id.expired_trip_layout);
        cancelTripLayout = findViewById(R.id.cancel_trip_layout);

        view_all = findViewById(R.id.view_all);
        view_complete = findViewById(R.id.view_complete);
        view_expired = findViewById(R.id.view_expired);

        tik_all = findViewById(R.id.tik_all);
        tik_complete = findViewById(R.id.tik_complete);
        tik_expired = findViewById(R.id.tik_expired);

        totalEarn = findViewById(R.id.total_earn);
    }

    private void initToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = LayoutInflater.from(this).inflate(R.layout.custom_toolbar_trip, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        actionBar.getCustomView().findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    View.OnClickListener textAllListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            view_all.setVisibility(View.VISIBLE);
            view_complete.setVisibility(View.INVISIBLE);
            view_expired.setVisibility(View.INVISIBLE);

            tik_all.setVisibility(View.VISIBLE);
            tik_complete.setVisibility(View.INVISIBLE);
            tik_expired.setVisibility(View.INVISIBLE);

            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            tripHistoryAdaper.filterAllItem();
        }
    };

    View.OnClickListener textCompleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            view_all.setVisibility(View.INVISIBLE);
            view_complete.setVisibility(View.VISIBLE);
            view_expired.setVisibility(View.INVISIBLE);

            tik_all.setVisibility(View.INVISIBLE);
            tik_complete.setVisibility(View.VISIBLE);
            tik_expired.setVisibility(View.INVISIBLE);

            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            tripHistoryAdaper.filterItemCompletion(BaseUrlUtils.DELIVERED);
        }
    };

    View.OnClickListener textExpiredListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            view_all.setVisibility(View.INVISIBLE);
            view_complete.setVisibility(View.INVISIBLE);
            view_expired.setVisibility(View.VISIBLE);

            tik_all.setVisibility(View.INVISIBLE);
            tik_complete.setVisibility(View.INVISIBLE);
            tik_expired.setVisibility(View.VISIBLE);

            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            tripHistoryAdaper.filterItemCompletion(BaseUrlUtils.FINISHED);
        }
    };

    View.OnClickListener textCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    };
}
