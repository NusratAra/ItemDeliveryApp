package com.example.nishikanto.itemdeliverapp.driver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nishikanto.itemdeliverapp.ItemDeliveryApplication;
import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.adapter.DeliveryPendingAdapter;
import com.example.nishikanto.itemdeliverapp.driver.profile.DriverProfileActivity;
import com.example.nishikanto.itemdeliverapp.model.User;
import com.example.nishikanto.itemdeliverapp.services.AuthenticationService;
import com.example.nishikanto.itemdeliverapp.services.NoConnectivityException;
import com.example.nishikanto.itemdeliverapp.services.RetrofitInstance;
import com.example.nishikanto.itemdeliverapp.utils.DataUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverHomeActivity extends AppCompatActivity {
    private static final String TAG = DriverHomeActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DeliveryPendingAdapter deliveryPendingAdapter;
    private FrameLayout profileImage;
    private LinearLayout filterLayout;
    private BottomSheetBehavior mBottomSheetBehaviour;
    private View blurView;

    private View view_all;
    private View view_ready_for_delivery;
    private View view_in_delivery;

    private ConstraintLayout layout_all;
    private ConstraintLayout layout_ready_for_delivery;
    private ConstraintLayout layout_in_delivery;

    private ImageView tik_all;
    private ImageView tik_ready_for_delivery;
    private ImageView tik_in_delivery;

    private LinearLayout layout;

    private ActionBar actionBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);

        initToolbar();
        findAllView();

        initListener();
        initBottomsheet();
        initRecyclerView();

        //getDriverProfileCall();

    }

    private void getDriverProfileCall() {
        DataUtils dataUtils = new DataUtils(getApplicationContext());

        if(((ItemDeliveryApplication)getApplicationContext()).getUser() != null){

            int idNo = ((ItemDeliveryApplication)getApplicationContext()).getUser().getId();
            String accessToken = "Bearer "+ dataUtils.getStr("access");

            AuthenticationService authenticationService = RetrofitInstance.getService(getApplicationContext());
            Log.d(TAG, "getDriverProfileCall: "+ idNo+"+"+accessToken);

            Call<User> userCall = authenticationService.getDriverProfile(accessToken, idNo);
            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.body() != null){
                        Log.d(TAG, "DriverProfileResponseBody: "+ response.body().toString());
                        ((ItemDeliveryApplication) getApplicationContext()).setUser(response.body());
                        Log.d(TAG, "UserName: "+ ((ItemDeliveryApplication) getApplicationContext()).getUser().getUsername());
                        Log.d(TAG, "iebn: "+ ((ItemDeliveryApplication) getApplicationContext()).getUser().getIebn());
                    } if(response.isSuccessful()){
                        Log.d(TAG, "SUCCESS: ");
                    }
                    if(response.errorBody() != null){
                        Log.d(TAG, "DriverProfileResponseErrorBody: "+ response.errorBody());
                    }
                    else {
                        Log.d(TAG, "onResponse: "+ response);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    if (t instanceof NoConnectivityException) {
                        Log.e(TAG, "onFailureThrowEx: " + t.getMessage());
                        Toast toast = Toast.makeText(getApplicationContext(), "Check your internet connection.", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Server Error!", Toast.LENGTH_SHORT);
                        toast.show();
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                }
            });
        }

    }

    private void findAllView() {

        filterLayout = actionBar.getCustomView().findViewById(R.id.filter_layout);
        filterLayout.setVisibility(View.INVISIBLE);

        layout = findViewById(R.id.layout);
        blurView = findViewById(R.id.blur_view);
        profileImage = actionBar.getCustomView().findViewById(R.id.profile_image);

        view_all = findViewById(R.id.view_all);
        view_ready_for_delivery = findViewById(R.id.view_ready_delivery);
        view_in_delivery = findViewById(R.id.view_in_delivery);
        tik_all = findViewById(R.id.tik_all);
        tik_ready_for_delivery = findViewById(R.id.tik_ready_delivery);
        tik_in_delivery = findViewById(R.id.tik_in_delivery);

        layout_all = findViewById(R.id.view_all_layout);
        layout_ready_for_delivery = findViewById(R.id.view_ready_layout);
        layout_in_delivery = findViewById(R.id.view_delivery_layout);
    }

    private void initRecyclerView() {

//        ArrayList<>
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(-1);
        arrayList.add(0);
        arrayList.add(1);
        arrayList.add(0);
        arrayList.add(1);
        arrayList.add(0);
        arrayList.add(1);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_home);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        deliveryPendingAdapter = new DeliveryPendingAdapter(this, arrayList);
        recyclerView.setAdapter(deliveryPendingAdapter);
    }

    private void initBottomsheet() {
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

    private void initToolbar(){

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        actionBar = getSupportActionBar();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = LayoutInflater.from(this).inflate(R.layout.custom_toolbar_home, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);

    }

    private void initListener(){

        layout_all.setOnClickListener(layoutAllListener);
        layout_ready_for_delivery.setOnClickListener(readyForDeliveryListener);
        layout_in_delivery.setOnClickListener(inDeliveryListener);
        filterLayout.setOnClickListener(filterLayoutListener);
        profileImage.setOnClickListener(profileImageListener);
        layout.setOnClickListener(layoutListener);

    }

    View.OnClickListener layoutAllListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            view_all.setVisibility(View.VISIBLE);
            view_ready_for_delivery.setVisibility(View.INVISIBLE);
            view_in_delivery.setVisibility(View.INVISIBLE);

            tik_all.setVisibility(View.VISIBLE);
            tik_ready_for_delivery.setVisibility(View.INVISIBLE);
            tik_in_delivery.setVisibility(View.INVISIBLE);
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            deliveryPendingAdapter.filterItemAll();
        }
    };

    View.OnClickListener readyForDeliveryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            view_all.setVisibility(View.INVISIBLE);
            view_ready_for_delivery.setVisibility(View.VISIBLE);
            view_in_delivery.setVisibility(View.INVISIBLE);

            tik_all.setVisibility(View.INVISIBLE);
            tik_ready_for_delivery.setVisibility(View.VISIBLE);
            tik_in_delivery.setVisibility(View.INVISIBLE);
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            deliveryPendingAdapter.filterItem(0);

        }
    };

    View.OnClickListener inDeliveryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            view_all.setVisibility(View.INVISIBLE);
            view_ready_for_delivery.setVisibility(View.INVISIBLE);
            view_in_delivery.setVisibility(View.VISIBLE);

            tik_all.setVisibility(View.INVISIBLE);
            tik_ready_for_delivery.setVisibility(View.INVISIBLE);
            tik_in_delivery.setVisibility(View.VISIBLE);
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
            deliveryPendingAdapter.filterItem(1);

        }
    };

    View.OnClickListener filterLayoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    };

    View.OnClickListener profileImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i=new Intent(getBaseContext(), DriverProfileActivity.class);
            startActivity(i);
        }
    };

    View.OnClickListener layoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            layout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            filterLayout.setVisibility(View.VISIBLE);
        }
    };
}
