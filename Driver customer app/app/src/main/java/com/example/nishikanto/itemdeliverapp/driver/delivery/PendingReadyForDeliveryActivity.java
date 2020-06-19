package com.example.nishikanto.itemdeliverapp.driver.delivery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nishikanto.itemdeliverapp.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class PendingReadyForDeliveryActivity extends AppCompatActivity implements View.OnClickListener {

    private BottomSheetBehavior mBottomSheetBehaviour;
    private ImageView btnImageBack;
    private View blurView;
    private LinearLayout outerLayout;

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_ready_for_delivery);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = LayoutInflater.from(this).inflate(R.layout.custom_toolbar_pending_delivery, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        actionBar.setCustomView(customView, params);
        actionBar.getCustomView().findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnImageBack = actionBar.getCustomView().findViewById(R.id.btn_image_back);
        blurView = findViewById(R.id.blur_view);

        outerLayout = actionBar.getCustomView().findViewById(R.id.outer_layout);
        outerLayout.setOnClickListener(this);


        View designBottomSheet = (View) findViewById(R.id.design_bottom_sheet);
        mBottomSheetBehaviour = BottomSheetBehavior.from(designBottomSheet);

        mBottomSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_COLLAPSED)
                    blurView.setVisibility(View.GONE);
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                blurView.setVisibility(View.VISIBLE);
                blurView.setAlpha(v);
            }
        });

        btnImageBack.setOnClickListener(this);
        blurView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_image_back:
                mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.blur_view:
                mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.outer_layout:
                Intent i=new Intent(getBaseContext(), ReadyForDeliveryActivity.class);
                startActivity(i);
                finish();
                break;

        }
    }
}
