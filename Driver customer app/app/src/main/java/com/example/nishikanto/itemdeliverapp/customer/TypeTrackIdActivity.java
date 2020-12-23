package com.example.nishikanto.itemdeliverapp.customer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nishikanto.itemdeliverapp.ItemDeliveryApplication;
import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.model.SingleTrip;
import com.example.nishikanto.itemdeliverapp.model.Trip;
import com.example.nishikanto.itemdeliverapp.services.NoConnectivityException;
import com.example.nishikanto.itemdeliverapp.services.RetrofitInstance;
import com.example.nishikanto.itemdeliverapp.services.TripAuthenticationService;
import com.example.nishikanto.itemdeliverapp.utils.DataUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TypeTrackIdActivity extends AppCompatActivity {
    private static final String TAG = TypeTrackIdActivity.class.getSimpleName();

    private Button btnTrackId;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private EditText trackId;


    private DataUtils dataUtils;
    private String token;
    private int userId;
    private ArrayList<Trip> tripArrayList;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_track_id);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        dataUtils = new DataUtils(getApplicationContext());

        initToolbar();

        trackId = findViewById(R.id.track_id);
        btnTrackId = findViewById(R.id.btn_track_id);
        btnTrackId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmptyTrackId(trackId.getText().toString())){
                    trackIdCall(trackId.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.enter_trackid), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void trackIdCall(String id) {

        SingleTrip trips = new SingleTrip();

        TripAuthenticationService tripAuthenticationService = RetrofitInstance.getServiceCall(getApplicationContext());
        Call<SingleTrip> tripCall = tripAuthenticationService.trackTrip(id);
        Log.d(TAG, "trackId: "+ id);

        tripCall.enqueue(new Callback<SingleTrip>() {
            @Override
            public void onResponse(Call<SingleTrip> call, Response<SingleTrip> response) {
                if(response.body() != null){

                    trips.setTrips(response.body().getTrips());
                    ((ItemDeliveryApplication) getApplicationContext()).setTrip(response.body().getTrips());
//                    Log.d(TAG, "TrackTrip: "+ new GsonBuilder().setPrettyPrinting().create().toJson(((ItemDeliveryApplication) getApplicationContext()).getTrip()));

                    Intent i=new Intent(getBaseContext(), CustomerDeliveryActivity.class);
                    startActivity(i);
                    finish();
                }
                if(response.errorBody() != null){
                    Toast.makeText(getApplicationContext(), getString(R.string.trip_expire), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SingleTrip> call, Throwable t) {
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

    private boolean isEmptyTrackId(String id) {
        if(id.matches("")){
            return true;
        } else {
            return false;
        }
    }

    private void initToolbar() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
    }
}
