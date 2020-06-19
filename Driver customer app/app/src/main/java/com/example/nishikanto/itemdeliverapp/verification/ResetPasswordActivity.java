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
import com.example.nishikanto.itemdeliverapp.authentication.LoginActivity;
import com.example.nishikanto.itemdeliverapp.services.AuthenticationService;
import com.example.nishikanto.itemdeliverapp.services.NoConnectivityException;
import com.example.nishikanto.itemdeliverapp.services.RetrofitInstance;

import org.json.JSONObject;

import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {
    private static final String TAG = ResetPasswordActivity.class.getSimpleName();

    private FancyButton btnSend;
    private EditText newPass;
    private EditText confirmNewPass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

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

        Intent intent = getIntent();
        int code = Integer.parseInt(intent.getStringExtra("code"));
        Log.d(TAG, "Code: "+ code);

        newPass = findViewById(R.id.new_password);
        confirmNewPass = findViewById(R.id.confirm_new_password);
        btnSend = findViewById(R.id.btn_reset);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFieldEmpty()){
                    if(isMatchedPass()) {
                        resetCall(code, newPass.getText().toString(), confirmNewPass.getText().toString());
                    } else {
                        Toast.makeText(getApplicationContext(), "Password didn't match!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnSend.setCustomTextFont(R.font.poppins_medium);

    }

    private boolean isMatchedPass() {
        if(newPass.getText().toString().equals(confirmNewPass.getText().toString())){
            return true;
        } else {
            return false;
        }
    }

    private boolean isFieldEmpty() {
        if(!newPass.getText().toString().equals("") && !confirmNewPass.getText().toString().equals("")){
            return false;
        }
        else {
            return true;
        }
    }

    private void resetCall(int codeText, String newPassword, String confirmNewPassword) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        AuthenticationService authenticationService = RetrofitInstance.getService(getApplicationContext());
        Call<JSONObject> userCall = authenticationService.resetPass(newPassword, confirmNewPassword, codeText);

        userCall.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (response.body() != null) {
                    Log.d(TAG, "SuccessResponseBody: " + response.body());
                }
                if (response.errorBody() != null) {
                    progressDialog.dismiss();
                    Log.d(TAG, "SuccessResponseErrorBody: " + response.errorBody());
                    Toast.makeText(getApplicationContext(), "Code or password wrong!", Toast.LENGTH_SHORT).show();
                }
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    Intent i=new Intent(getBaseContext(), LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
                if(response.message() != null){
                    Log.d(TAG, "onResponse3: "+ response.message());
                }
                else {
                    progressDialog.dismiss();
                    Log.d(TAG, "onResponse1: "+ response.code());
                    Log.d(TAG, "onResponse2: "+ response);


                    Toast toast = Toast.makeText(getApplicationContext(), "Email or password wrong!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
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
