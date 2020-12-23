package com.example.nishikanto.itemdeliverapp.verification;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.chaos.view.PinView;
import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.authentication.LoginActivity;
import com.example.nishikanto.itemdeliverapp.driver.profile.DriverProfileActivity;
import com.example.nishikanto.itemdeliverapp.services.AuthenticationService;
import com.example.nishikanto.itemdeliverapp.services.NoConnectivityException;
import com.example.nishikanto.itemdeliverapp.services.RetrofitInstance;

import org.json.JSONObject;

import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverOTP1Activity extends AppCompatActivity {
    private static final String TAG = DriverOTP1Activity.class.getSimpleName();

    private FancyButton btnContinue;
    private String email;
    private String password;
    private String confirmPassword;
    private PinView code;
    private TextView timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_otp1);
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

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        confirmPassword = intent.getStringExtra("confirm_password");

        code = findViewById(R.id.code);
        btnContinue = findViewById(R.id.btn_continue);
        timer = findViewById(R.id.timer);

        Log.d(TAG, "onCreate: "+ email);

        new CountDownTimer(39000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText(""+ millisUntilFinished / 1000 +" "+ getString(R.string.time));
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                timer.setText("done!");
            }

        }.start();

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int autoLogin = getIntent().getIntExtra("auto_login", 0);

                if(autoLogin == 1){
                    Intent intent=new Intent(getBaseContext(), DriverProfileActivity.class);
                    startActivity(intent);
                    finish();
                } else {
//                    verificationCall();
                    Intent i=new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(i);
                    finish();
                }

                finish();
            }
        });

        btnContinue.setCustomTextFont(R.font.poppins_medium);

    }

    private void verificationCall() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        AuthenticationService authenticationService = RetrofitInstance.getService(getApplicationContext());
        Call<JSONObject> userCall = authenticationService.resetPass(password, confirmPassword, Integer.parseInt(code.getText().toString()));
        userCall.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    Intent i=new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(i);
                    finish();

                } else {
                    progressDialog.dismiss();
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.verification_code_wrong), Toast.LENGTH_SHORT);
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
