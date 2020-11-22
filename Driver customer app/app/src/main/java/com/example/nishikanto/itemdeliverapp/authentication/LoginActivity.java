package com.example.nishikanto.itemdeliverapp.authentication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nishikanto.itemdeliverapp.ItemDeliveryApplication;
import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.driver.DriverHomeActivity;
import com.example.nishikanto.itemdeliverapp.model.User;
import com.example.nishikanto.itemdeliverapp.model.UserCredential;
import com.example.nishikanto.itemdeliverapp.services.AuthenticationService;
import com.example.nishikanto.itemdeliverapp.services.NoConnectivityException;
import com.example.nishikanto.itemdeliverapp.services.RetrofitInstance;
import com.example.nishikanto.itemdeliverapp.utils.DataUtils;
import com.example.nishikanto.itemdeliverapp.verification.ForgotPasswordActivity;

import java.io.IOException;

import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    public static String TAG = LoginActivity.class.getSimpleName();

    private FancyButton btnLogin;
    private LinearLayout loginToReg;
    private TextView loginTextFix;
    private TextView forgetPass;
    private EditText email;
    private EditText password;
    private ProgressDialog progressDialog;
    private DataUtils dataUtils;
    private String accessToken;
    private User user;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

        btnLogin = findViewById(R.id.btn_login);
        loginToReg = findViewById(R.id.login_to_reg);
        forgetPass = findViewById(R.id.forget_pass);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_pass);


        btnLogin.setCustomTextFont(R.font.poppins_medium);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFieldEmpty()){
                    loginAuth(email.getText().toString(), password.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        loginToReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getBaseContext(), SignupPage1Activity.class);
                startActivity(i);
                finish();
            }
        });

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getBaseContext(), ForgotPasswordActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private boolean isFieldEmpty() {
        if(!email.getText().toString().equals("") && !password.getText().toString().equals("")){
            return false;
        }
        else {
            return true;
        }
    }

    private void loginAuth(String email, String password){
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        AuthenticationService authenticationService = RetrofitInstance.getService(getApplicationContext());
        Call<UserCredential> callToken = authenticationService.getTokens(email, password);
        callToken.enqueue(new Callback<UserCredential>() {
            @Override
            public void onResponse(Call<UserCredential> call, Response<UserCredential> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponseRefresh: " + response.body().getRefresh());
                    dataUtils = new DataUtils(getApplicationContext());
                    dataUtils.setStr("refresh", response.body().getRefresh());
                    dataUtils.setStr("access", response.body().getAccess());
                    accessToken = "Bearer " + dataUtils.getStr("access");

                    getUsers();

                } else {
                    progressDialog.dismiss();
                    try {
                        Log.d(TAG, "onResponseRefresh: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "Email or password wrong!", Toast.LENGTH_SHORT).show();
                }
//                Log.d(TAG, "onResponseAccess: " + response.body().getAccess());

            }

            @Override
            public void onFailure(Call<UserCredential> call, Throwable t) {
                if(t instanceof NoConnectivityException){
                    Log.e(TAG, "onFailureThrowEx: "+ t.getMessage());
                    Toast toast = Toast.makeText(getApplicationContext(), "Check your internet connection.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Server Error!", Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d(TAG, "onFailure: "+ t.getMessage());
                }
                progressDialog.dismiss();
            }
        });
    }

    private void getUsers() {
        AuthenticationService authenticationService = RetrofitInstance.getService(getApplicationContext());
        Call<User> userCall = authenticationService.getAuthUser(accessToken);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body() != null){
                    getUserInfo(response.body());
                    Intent i=new Intent(getBaseContext(), DriverHomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
                progressDialog.dismiss();

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
                progressDialog.dismiss();

            }
        });

    }


    //TODO add more user data
    private void getUserInfo(User response) {
        user = new User();
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
