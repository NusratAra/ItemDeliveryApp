package com.example.nishikanto.itemdeliverapp.driver.profile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.services.AuthenticationService;
import com.example.nishikanto.itemdeliverapp.services.NoConnectivityException;
import com.example.nishikanto.itemdeliverapp.services.RetrofitInstance;
import com.example.nishikanto.itemdeliverapp.utils.DataUtils;
import com.google.gson.JsonElement;

import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private static final String TAG = ChangePasswordActivity.class.getSimpleName();

    private FancyButton btnUpdatePassword;
    private EditText oldPassword;
    private EditText newPassword;
    private EditText confirmNewPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();

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

        oldPassword = findViewById(R.id.old_password);
        newPassword = findViewById(R.id.new_password);
        confirmNewPassword = findViewById(R.id.confirm_new_password);

        btnUpdatePassword = findViewById(R.id.btn_update);
        btnUpdatePassword.setCustomTextFont(R.font.poppins_medium);

        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFieldEmpty()){
                    if(isMatchedPass()){
                        changePassCall(oldPassword.getText().toString(), newPassword.getText().toString(), confirmNewPassword.getText().toString());
                    } else {
                        Toast.makeText(getApplicationContext(), "Password didn't match!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean isFieldEmpty() {
        if(!oldPassword.getText().toString().equals("") && !newPassword.getText().toString().equals("") && !confirmNewPassword.getText().toString().equals("")){
            return false;
        }
        else {
            return true;
        }
    }

    private boolean isMatchedPass() {
        if(newPassword.getText().toString().equals(confirmNewPassword.getText().toString())){
            return true;
        } else {
            return false;
        }
    }

    private void changePassCall(String oldPass, String newPass, String confirmPass) {

        String accessToken = "Bearer "+new DataUtils(getApplicationContext()).getStr("access");
        Log.d(TAG, "AccessToken: "+ accessToken);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        AuthenticationService authenticationService = RetrofitInstance.getService(getApplicationContext());
        Call<JsonElement> userCall = authenticationService.changePass(accessToken, newPass, confirmPass);
        userCall.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.body() != null){
                    Log.d(TAG, "onResponseBody: "+ response.body().toString());
                }
                if(response.errorBody() != null){
                    Log.d(TAG, "onResponseError: "+ response.errorBody().toString());
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Password wrong!", Toast.LENGTH_SHORT).show();
                }
                if(response.message() != null){
                    Log.d(TAG, "onResponseMessage: "+ response.message().toString());
                }
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse1: "+ response.body().toString());
                    progressDialog.dismiss();
                } else {
                    Log.d(TAG, "onResponse2: "+ response.errorBody().toString());
                    progressDialog.dismiss();
                    Toast toast = Toast.makeText(getApplicationContext(), "Password wrong!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                onBackPressed();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    Log.e(TAG, "onFailureThrowEx: " + t.getMessage());
                    Toast toast = Toast.makeText(getApplicationContext(), "Check your internet connection.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Server Error!", Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
                progressDialog.dismiss();
            }
        });
    }
}
