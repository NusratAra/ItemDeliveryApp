package com.example.nishikanto.itemdeliverapp.verification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.chaos.view.PinView;
import com.example.nishikanto.itemdeliverapp.R;

import mehdi.sakout.fancybuttons.FancyButton;

//TODO: should be deleted
//TODO: should rename package
public class DriverOTP2Activity extends AppCompatActivity {

    private static final String TAG = DriverOTP2Activity.class.getSimpleName();

    private FancyButton btnContinue;
    private PinView code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_otp2);
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

        code = findViewById(R.id.code);
        btnContinue = findViewById(R.id.btn_continue);


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                codeRequestCall(code.getText().toString());
                Log.d(TAG, "CodeMain: "+code.getText());
                Intent i=new Intent(getBaseContext(), ResetPasswordActivity.class);
                i.putExtra("code", code.getText().toString());
                startActivity(i);
                finish();
            }
        });

        btnContinue.setCustomTextFont(R.font.poppins_medium);

    }
}
