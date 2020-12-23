package com.example.nishikanto.itemdeliverapp.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.model.DriverLocation;
import com.example.nishikanto.itemdeliverapp.services.NoConnectivityException;
import com.example.nishikanto.itemdeliverapp.services.RetrofitInstance;
import com.example.nishikanto.itemdeliverapp.services.TripAuthenticationService;
import com.example.nishikanto.itemdeliverapp.utils.DataUtils;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = MyReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
        DataUtils dataUtils = new DataUtils(context);
        String token = "Bearer "+ dataUtils.getStr("access");
        if (location != null) {
            Toast.makeText(context, LocationUtils.getLocationText(location),
                    Toast.LENGTH_SHORT).show();

            locationCall(context, token, location.getLatitude(), location.getLongitude());
//                location.getLatitude()
        }
    }

    private void locationCall(Context context, String token, double latitude, double longitude) {
        TripAuthenticationService tripAuthenticationService = RetrofitInstance.getServiceCall(context);
        Call<DriverLocation> driverLocationCall = tripAuthenticationService.updateLocation(token, String.valueOf(latitude), String.valueOf(longitude));
        driverLocationCall.enqueue(new Callback<DriverLocation>() {
            @Override
            public void onResponse(Call<DriverLocation> call, Response<DriverLocation> response) {
                Log.d("TAG", "LocationUpdate: "+ new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));

            }

            @Override
            public void onFailure(Call<DriverLocation> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    Log.e(TAG, "onFailureThrowEx: " + t.getMessage());
                    Toast toast = Toast.makeText(context, context.getString(R.string.server_error_customer), Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(context, context.getString(R.string.server_error), Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            }
        });
    }
}