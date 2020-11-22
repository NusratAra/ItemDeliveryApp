package com.example.nishikanto.itemdeliverapp.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.example.nishikanto.itemdeliverapp.services.NoConnectivityException;
import com.example.nishikanto.itemdeliverapp.services.RetrofitInstance;
import com.example.nishikanto.itemdeliverapp.services.TripAuthenticationService;
import com.example.nishikanto.itemdeliverapp.utils.DataUtils;

import org.json.JSONObject;

import br.com.safety.locationlistenerhelper.core.SettingsLocationTracker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationReceiver extends BroadcastReceiver {

    private static final String TAG = LocationReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != intent && intent.getAction().equals("my.action")) {
            Location locationData = (Location) intent.getParcelableExtra(SettingsLocationTracker.LOCATION_MESSAGE);
            Log.d("Location: ", "Latitude: " + locationData.getLatitude() + "Longitude:" + locationData.getLongitude());
            //send your call to api or do any things with the of location data
        }

        locationCall(context);
    }

    private void locationCall(Context context) {

        DataUtils dataUtils = new DataUtils(context);

        String token = dataUtils.getStr("access");

        TripAuthenticationService tripAuthenticationService = RetrofitInstance.getServiceCall(context);
        Call<JSONObject> call = tripAuthenticationService.updateLocation(token);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if(response.body() != null){
                    Log.d(TAG, "onResponse: "+response.body());

                }
                if (response.errorBody() != null){
                    Log.d(TAG, "onResponseError: "+response.errorBody());

                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    Log.e(TAG, "onFailureThrowExTrip: " + t.getMessage());
                    Toast toast = Toast.makeText(context, "Check your internet connection.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(context, "Server Error!", Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d(TAG, "onFailureTrip: " + t.getMessage());
                }
            }
        });
    }
}