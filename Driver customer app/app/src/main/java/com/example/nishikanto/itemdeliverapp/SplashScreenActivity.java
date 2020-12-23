package com.example.nishikanto.itemdeliverapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nishikanto.itemdeliverapp.driver.DriverHomeActivity;
import com.example.nishikanto.itemdeliverapp.model.User;
import com.example.nishikanto.itemdeliverapp.model.UserCredential;
import com.example.nishikanto.itemdeliverapp.services.AuthenticationService;
import com.example.nishikanto.itemdeliverapp.services.NoConnectivityException;
import com.example.nishikanto.itemdeliverapp.services.RetrofitInstance;
import com.example.nishikanto.itemdeliverapp.utils.DataUtils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG = SplashScreenActivity.class.getSimpleName();

    private DataUtils dataUtils;
    private String accessToken;
    private String refreshToken;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        dataUtils = new DataUtils(getApplicationContext());
        accessToken = dataUtils.getStr("access");
        refreshToken = dataUtils.getStr("refresh");
        Log.d(TAG, "AccessToken: " + accessToken);
        Log.d(TAG, "RefreshToken: " + refreshToken);
        checkTokens();

    }

    private void checkTokens() {

        if (!accessToken.equals("") && !refreshToken.equals("")) {
            getAuthUser(accessToken);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    newUserCall();
                }
            },2000);
        }


    }

    private void getAuthUser(String accessToken) {
        String access = "Bearer " + accessToken;
                AuthenticationService authenticationService = RetrofitInstance.getService(getApplicationContext());
        Call<User> userCall = authenticationService.getAuthUser(access);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(count > 1){
                    dataUtils.setStr("access", "");
                    dataUtils.setStr("refresh", "");
                    newUserCall();
                    return;
                }
                if(response.body() != null){
                    Log.d(TAG, "ResponseBodyAuth: "+ response.body().toString());
                    getUserInfo(response.body());
                    homePageRedirect();
                }
                if(response.errorBody() != null){
                    try {
                        Log.d(TAG, "ErrorBodyAuth: "+ response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callForAccessToken(refreshToken);
                }

                Log.d(TAG, "ResponseMessage: Auth"+ response.message());

                count++;


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


    private void getUserInfo(User response) {
        User user = new User();
        user.setId(response.getId());
        user.setEmail(response.getEmail());
        user.setRole(response.getRole());
        user.setIs_active(response.getIs_active());
        user.setIs_verified(response.getIs_verified());
        user.setAddress(response.getAddress());
        user.setDate_joined(response.getDate_joined());
        Log.d(TAG, "onResponseUser: "+ user.getId());

        ((ItemDeliveryApplication) getApplicationContext()).setUser(user);
    }

    private void callForAccessToken(String token) {

        AuthenticationService authenticationService = RetrofitInstance.getService(getApplicationContext());
        Call<UserCredential> userCredentialCall = authenticationService.refreshToken(token);
        userCredentialCall.enqueue(new Callback<UserCredential>() {
            @Override
            public void onResponse(Call<UserCredential> call, Response<UserCredential> response) {
                if (response.body() != null) {
                    Log.d(TAG, "onResponseBodyR: " + response.body().toString());
                    Log.d(TAG, "AccessFromRefresh: "+ response.body().getAccess());

                    dataUtils = new DataUtils(getApplicationContext());
                    dataUtils.setStr("refresh", response.body().getRefresh());
                    dataUtils.setStr("access", response.body().getAccess());

                    getAuthUser(response.body().getAccess());
                }
                if (response.errorBody() != null) {
                    try {
                        Log.d(TAG, "onResponseErrorBodyR: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    newUserCall();
                }

                Log.d(TAG, "ResponseMessage: "+ response.message());
            }

            @Override
            public void onFailure(Call<UserCredential> call, Throwable t) {
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

    private void homePageRedirect() {
        //LoadingHomeActivity
        Intent i = new Intent(getBaseContext(), DriverHomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }


    private void newUserCall() {
        //SelectWayActivity
        Intent i = new Intent(getBaseContext(), SelectWayActivity.class);
        startActivity(i);
        finish();
    }
}
