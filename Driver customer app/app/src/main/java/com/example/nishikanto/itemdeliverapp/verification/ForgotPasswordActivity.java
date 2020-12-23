package com.example.nishikanto.itemdeliverapp.verification;

import android.app.ProgressDialog;
import android.content.Intent;
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

import org.json.JSONObject;

import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String TAG = ForgotPasswordActivity.class.getSimpleName();

    private FancyButton btnForgetPass;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
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

        email = findViewById(R.id.input_email);
        btnForgetPass = findViewById(R.id.btn_forget_pass);

        btnForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFieldEmpty()){
                    forgetPasswordCall(email.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.email_input_toast), Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnForgetPass.setCustomTextFont(R.font.poppins_medium);

    }

    private boolean isFieldEmpty() {
        if(!email.getText().toString().equals("")){
            return false;
        } else {
            return true;
        }
    }

    private void forgetPasswordCall(String emailtext) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        AuthenticationService authenticationService = RetrofitInstance.getService(getApplicationContext());
        Call<JSONObject> userCall = authenticationService.forgetPass(emailtext);
        userCall.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (response.body() != null) {
                    Log.d(TAG, "SuccessResponseBody: " + response.body());
                }
                if (response.errorBody() != null) {
                    progressDialog.dismiss();
                    Log.d(TAG, "SuccessResponseErrorBody: " + response.errorBody());
                    Toast.makeText(getApplicationContext(), getString(R.string.wrong_email), Toast.LENGTH_SHORT).show();
                }
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    Intent i=new Intent(getBaseContext(), DriverOTP2Activity.class);
                    startActivity(i);
                    finish();
                } else {
                    progressDialog.dismiss();
                    Log.d(TAG, "onResponse: "+ response.code());
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.email_or_pass_wrong), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    Log.e(TAG, "onFailureThrowEx: " + t.getMessage());
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.server_error_customer), Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.server_error), Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
                progressDialog.dismiss();
            }
        });
    }
}
