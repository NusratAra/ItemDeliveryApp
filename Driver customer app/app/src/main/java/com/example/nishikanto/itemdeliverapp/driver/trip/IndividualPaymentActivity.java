package com.example.nishikanto.itemdeliverapp.driver.trip;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.nishikanto.itemdeliverapp.R;

public class IndividualPaymentActivity extends AppCompatActivity {

    private TextView tvTitle;
    private LinearLayout statusLayout;
    private LinearLayout callRedirectLayout;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_payment);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Typeface custom_font = ResourcesCompat.getFont(this, R.font.poppins_medium);

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

        tvTitle = actionBar.getCustomView().findViewById(R.id.tvTitle);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.details);
        tvTitle.setTypeface(custom_font);
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.colorBlackLight2));
        actionBar.setTitle(tvTitle.getText());
        statusLayout = findViewById(R.id.status_layout);
        callRedirectLayout = findViewById(R.id.call_redirect_layout);

        int statusValue = getIntent().getIntExtra("status", 0);

        if(statusValue == 1){
            statusLayout.setVisibility(View.VISIBLE);
            //callRedirectLayout.setVisibility(View.GONE);
        } else if(statusValue == 2){
            //callRedirectLayout.setVisibility(View.GONE);
        }

    }
}
