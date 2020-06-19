package com.example.nishikanto.itemdeliverapp.driver.profile;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.nishikanto.itemdeliverapp.ItemDeliveryApplication;
import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.authentication.LoginActivity;
import com.example.nishikanto.itemdeliverapp.driver.payment.PaymentHistoryActivity;
import com.example.nishikanto.itemdeliverapp.driver.trip.TripHistoryActivity;
import com.example.nishikanto.itemdeliverapp.utils.DataUtils;

import jp.wasabeef.blurry.Blurry;

public class DriverProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout editProfile;
    private LinearLayout tripHistoryLayout;
    private LinearLayout paymentHistoryLayout;
    private TextView driverName;
    private TextView tvTitle;
    private LinearLayout logoutLayout;
    private TextView btnCancel;
    private TextView btnConfirm;
    private LinearLayout changeNumber;
    private LinearLayout changePassword;
    private Intent intent;
    private TextView switchState;
    private SwitchCompat btnSwitch;
    public static Boolean isTouched = false;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"ResourceAsColor", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        Typeface custom_font = ResourcesCompat.getFont(this, R.font.poppins_medium);


        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = LayoutInflater.from(this).inflate(R.layout.custom_toolbar, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        actionBar.getCustomView().findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvTitle = actionBar.getCustomView().findViewById(R.id.tvTitle);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.my_profile_text);
        tvTitle.setTypeface(custom_font);
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.colorBlackLight2));
        actionBar.setTitle(tvTitle.getText());

        driverName =findViewById(R.id.driver_name);
        editProfile = findViewById(R.id.edit_profile_layout);
        tripHistoryLayout = findViewById(R.id.trip_history_layout);
        paymentHistoryLayout = findViewById(R.id.payment_history_layout);
//        blurView = findViewById(R.id.blur_view);
        changeNumber = findViewById(R.id.change_number);
        changePassword = findViewById(R.id.change_pass);
        logoutLayout = findViewById(R.id.logout_layout);
        btnSwitch = findViewById(R.id.btn_switch);
        switchState = findViewById(R.id.switch_state);
        switchState.setText(R.string.offline);
        if(((ItemDeliveryApplication) getApplicationContext()).getUser().getUsername() != null){
            driverName.setText(((ItemDeliveryApplication) getApplicationContext()).getUser().getUsername());
        } else {
            driverName.setText(getString(R.string.driver_name_text));
        }

        btnSwitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isTouched = true;
                return false;
            }
        });

        btnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isTouched) {
                    isTouched = false;
                    if (isChecked) {
                        switchState.setText(R.string.online);
                    }
                    else {
                        switchState.setText(R.string.offline);
                    }
                }
            }
        });

        editProfile.setOnClickListener(this);
        tripHistoryLayout.setOnClickListener(this);
        paymentHistoryLayout.setOnClickListener(this);
        changeNumber.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        logoutLayout.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDialog() {
//Theme_D1NoTitleDim
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.dialog_logout, null);
        final Dialog alertDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        alertDialog.setContentView(deleteDialogView);

        btnCancel = alertDialog.findViewById(R.id.cancel);
        btnConfirm = alertDialog.findViewById(R.id.confirm);

//        btnCancel.setCustomTextFont(R.font.poppins_medium);



        deleteDialogView.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataUtils dataUtils = new DataUtils(getApplicationContext());
                dataUtils.setStr("access", "");
                dataUtils.setStr("refresh", "");
                alertDialog.dismiss();
                Intent intent = new Intent(DriverProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        deleteDialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
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
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.trip_history_layout:
                intent =new Intent(getBaseContext(), TripHistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.payment_history_layout:
                intent =new Intent(getBaseContext(), PaymentHistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.edit_profile_layout:
                intent =new Intent(getBaseContext(), EditProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.change_number:
                intent =new Intent(getBaseContext(), ChangePhoneNumberActivity.class);
                startActivity(intent);
                break;
            case R.id.change_pass:
                intent =new Intent(getBaseContext(), ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.logout_layout:
                showDialog();
                break;
        }
    }
}
