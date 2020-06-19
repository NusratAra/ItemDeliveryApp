package com.example.nishikanto.itemdeliverapp.driver;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.driver.delivery.PendingReadyForDeliveryActivity;

public class DeliveryDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvTitle;
    private Button buttonAccept;
    private Button buttonCancel;
    private LinearLayout callRedirectLayout;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_details);
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


        buttonAccept = findViewById(R.id.btn_accept);
        buttonCancel = findViewById(R.id.btn_cancel);

        callRedirectLayout = findViewById(R.id.call_redirect_layout);
//        callRedirectLayout.setVisibility(View.GONE);

        buttonCancel.setOnClickListener(this);
        buttonAccept.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_accept:
                Intent i=new Intent(getBaseContext(), PendingReadyForDeliveryActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.btn_cancel:
                onBackPressed();
                break;
        }
    }
}
