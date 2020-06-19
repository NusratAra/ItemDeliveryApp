package com.example.nishikanto.itemdeliverapp.driver.delivery;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.driver.DriverHomeActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import jp.wasabeef.blurry.Blurry;
import mehdi.sakout.fancybuttons.FancyButton;

public class DeliveredActivity extends AppCompatActivity implements View.OnClickListener {

    public FancyButton btnSendVerification;
    private BottomSheetBehavior mBottomSheetBehaviour;
    private ImageView btnImageBack;
    private ImageView btn_image;
    private ImageView crossButton;
    private View blurView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivered);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = LayoutInflater.from(this).inflate(R.layout.custom_toolbar_delivered, null);
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

        btn_image = actionBar.getCustomView().findViewById(R.id.btn_image);
        btn_image.setOnClickListener(this);


        View designBottomSheet = (View) findViewById(R.id.design_bottom_sheet);
        mBottomSheetBehaviour = BottomSheetBehavior.from(designBottomSheet);
        mBottomSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_COLLAPSED){
                    blurView.setVisibility(View.GONE);
                }

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
            case R.id.btn_image:
                showDialog();
                break;
        }
    }
    public void showDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.dialog_trip_verification, null);
        final Dialog alertDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        alertDialog.setContentView(deleteDialogView);
        alertDialog.setCancelable(true);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });

        deleteDialogView.findViewById(R.id.cross_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btnSendVerification = alertDialog.findViewById(R.id.btn_continue);
        btnSendVerification.setCustomTextFont(R.font.poppins_medium);

        deleteDialogView.findViewById(R.id.btn_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                showSuccessDialog();

            }
        });

        Blurry.with(this)
                .radius(25)
                .sampling(1)
                .color(Color.argb(80, 0, 0, 0))
                .capture(findViewById(R.id.mainLayout))
                .into(alertDialog.findViewById(R.id.blurView));

        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void showSuccessDialog() {
//Theme_D1NoTitleDim
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.dialog_trip_success, null);
        final Dialog alertDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);


        alertDialog.setContentView(deleteDialogView);
        alertDialog.setCancelable(true);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });

        btnSendVerification = alertDialog.findViewById(R.id.btn_continue);
        btnSendVerification.setCustomTextFont(R.font.poppins_medium);




        deleteDialogView.findViewById(R.id.btn_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), DriverHomeActivity.class);
                startActivity(i);
                alertDialog.dismiss();
                finish();
            }
        });

        Blurry.with(this)
                .radius(25)
                .sampling(1)
                .color(Color.argb(80, 0, 0, 0))
                .capture(findViewById(R.id.mainLayout))
                .into(alertDialog.findViewById(R.id.blurView));

        alertDialog.show();

    }

}
