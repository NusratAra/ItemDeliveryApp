package com.example.nishikanto.itemdeliverapp.driver;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nishikanto.itemdeliverapp.ItemDeliveryApplication;
import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.adapter.DeliveryPendingAdapter;
import com.example.nishikanto.itemdeliverapp.driver.profile.DriverProfileActivity;
import com.example.nishikanto.itemdeliverapp.location.LocationUpdatesService;
import com.example.nishikanto.itemdeliverapp.location.MyReceiver;
import com.example.nishikanto.itemdeliverapp.model.Trip;
import com.example.nishikanto.itemdeliverapp.model.Trips;
import com.example.nishikanto.itemdeliverapp.model.User;
import com.example.nishikanto.itemdeliverapp.services.AuthenticationService;
import com.example.nishikanto.itemdeliverapp.services.NoConnectivityException;
import com.example.nishikanto.itemdeliverapp.services.RetrofitInstance;
import com.example.nishikanto.itemdeliverapp.services.TripAuthenticationService;
import com.example.nishikanto.itemdeliverapp.utils.BaseUrlUtils;
import com.example.nishikanto.itemdeliverapp.utils.DataUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.GsonBuilder;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
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

    private DataUtils dataUtils;
    private String token;
    private User user;
    private int userId;

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
    private CircleImageView driverProfilePic;

    private ActionBar actionBar;
    private ArrayList<Trip> tripArrayList;




    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;

    // A reference to the service used to get location updates.
    private LocationUpdatesService mService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;


    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;

            mService.requestLocationUpdates();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };









    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);
        dataUtils = new DataUtils(getApplicationContext());
        tripArrayList = new ArrayList<>();

        initToolbar();
        findAllView();

        initListener();
        initBottomsheet();
        initRecyclerView(tripArrayList);

        layout.setVisibility(View.VISIBLE);
//        recyclerView.setVisibility(View.GONE);
//        filterLayout.setVisibility(View.GONE);

        updateLocation();

        getDriverProfileCall();
        allTripCall();

        if(tripArrayList != null){

            layout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            filterLayout.setVisibility(View.VISIBLE);
        }

    }

    private void updateLocation() {
        myReceiver = new MyReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();

        AndPermission.with(this)
                .runtime()
                .permission(Permission.ACCESS_COARSE_LOCATION)
                .permission(Permission.ACCESS_FINE_LOCATION)
                .onGranted(permissions -> {
                    // Storage permission are allowed.
                    bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
                            Context.BIND_AUTO_CREATE);
                })
                .onDenied(permissions -> {
                    // Storage permission are not allowed.
                    finish();
                })
                .start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
//        languageSwitch();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mService.removeLocationUpdates();
    }

    private void initProfilePic() {

        user = ((ItemDeliveryApplication) getApplicationContext()).getUser();
        if(user != null){
            if(user.getProfile_or_logo() != null){
                Log.d(TAG, "ProfilePicUrl: "+ user.getProfile_or_logo());
                Glide.with(this).asDrawable()
                        .load(BaseUrlUtils.BASE_URL+user.getProfile_or_logo())
                        .into(driverProfilePic);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void allTripCall() {
        tripArrayList = new ArrayList<Trip>();


        Trips trips = new Trips();

        token = "Bearer "+ dataUtils.getStr("access");
        //////////////////////////////////////
//        userId = 3;

        if(userId != 0){
            Log.d(TAG, "USERID: "+ userId);
            TripAuthenticationService authenticationService = RetrofitInstance.getServiceCall(getApplicationContext());
            Call<Trips> tripCall = authenticationService.getAllTrips(token, userId);

            tripCall.enqueue(new Callback<Trips>() {
                @Override
                public void onResponse(Call<Trips> call, Response<Trips> response) {
                    if(response.body() != null){

                        Log.d(TAG, "onResponseBodyTrip: "+ tripArrayList);

                        trips.setTrips(response.body().getTrips());

                        initRecyclerView(trips.getTrips());

                        Log.d(TAG, "ResponseBodyTrip: "+ new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));




                    }
                    if(response.errorBody() != null){
                        try {
                            Log.d(TAG, "ErrorResponse: "+ response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

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


    }

    private void getDriverProfileCall() {

        if(((ItemDeliveryApplication)getApplicationContext()).getUser() != null){

            userId = ((ItemDeliveryApplication)getApplicationContext()).getUser().getId();

            String accessToken = "Bearer "+ dataUtils.getStr("access");

            AuthenticationService authenticationService = RetrofitInstance.getService(getApplicationContext());
            Log.d(TAG, "getDriverProfileCall: "+ userId+"+"+accessToken);

            Call<User> userCall = authenticationService.getDriverProfile(accessToken, userId);
            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.body() != null){
                        Log.d(TAG, "DriverProfileResponseBody: "+ response.body().toString());
                        ((ItemDeliveryApplication) getApplicationContext()).setUser(response.body());
                        Log.d(TAG, "UserName: "+ ((ItemDeliveryApplication) getApplicationContext()).getUser().getUsername());
                        Log.d(TAG, "iebn: "+ ((ItemDeliveryApplication) getApplicationContext()).getUser().getIebn());

                        initProfilePic();
                    } if(response.isSuccessful()){
                        Log.d(TAG, "SUCCESS: ");
                    }
                    if(response.errorBody() != null){
                        Log.d(TAG, "DriverProfileResponseErrorBody: "+ response.errorBody());
                    }
                    else {
                        Log.d(TAG, "onResponseProfile: "+ response);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
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

    private void findAllView() {

        filterLayout = actionBar.getCustomView().findViewById(R.id.filter_layout);
//        filterLayout.setVisibility(View.INVISIBLE);

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

    private void initRecyclerView(ArrayList<Trip> tripArrayList) {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_home);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        deliveryPendingAdapter = new DeliveryPendingAdapter(this, tripArrayList);
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

        driverProfilePic = toolbar.findViewById(R.id.driver_img);

    }

    private void initListener(){

        layout_all.setOnClickListener(layoutAllListener);
        layout_ready_for_delivery.setOnClickListener(readyForDeliveryListener);
        layout_in_delivery.setOnClickListener(inDeliveryListener);
        filterLayout.setOnClickListener(filterLayoutListener);
        profileImage.setOnClickListener(profileImageListener);
//        layout.setOnClickListener(layoutListener);
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
            deliveryPendingAdapter.filterItem(4);

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
            deliveryPendingAdapter.filterItem(5);

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

        }
    };


    private void languageSwitch() {
        if(dataUtils.getStr("lang").equals("en")){
            setLocal("en");
            recreate();
        } else if(dataUtils.getStr("lang").equals("ar")){
            setLocal("ar");
            recreate();
        }
    }

    private void setLocal(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        dataUtils.setStr("lang", lang);

    }


}
