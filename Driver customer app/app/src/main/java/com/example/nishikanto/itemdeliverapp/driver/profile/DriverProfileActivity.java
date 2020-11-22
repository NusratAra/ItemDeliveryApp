package com.example.nishikanto.itemdeliverapp.driver.profile;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.nishikanto.itemdeliverapp.ItemDeliveryApplication;
import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.authentication.LoginActivity;
import com.example.nishikanto.itemdeliverapp.driver.payment.PaymentHistoryActivity;
import com.example.nishikanto.itemdeliverapp.driver.trip.TripHistoryActivity;
import com.example.nishikanto.itemdeliverapp.model.User;
import com.example.nishikanto.itemdeliverapp.services.AuthenticationService;
import com.example.nishikanto.itemdeliverapp.services.NoConnectivityException;
import com.example.nishikanto.itemdeliverapp.services.RetrofitInstance;
import com.example.nishikanto.itemdeliverapp.utils.BaseUrlUtils;
import com.example.nishikanto.itemdeliverapp.utils.DataUtils;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = DriverProfileActivity.class.getSimpleName();

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
    private CircleImageView driverProfilePic;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initToolbar();
        initFindView();

        User user = ((ItemDeliveryApplication) getApplicationContext()).getUser();
        if(user != null){
            driverName.setText(user.getUsername());
            if(user.getProfile_or_logo() != null){
                Log.d(TAG, "ProfilePicUrl: "+ user.getProfile_or_logo());
               setImageToView(BaseUrlUtils.BASE_URL+user.getProfile_or_logo());
            }
        } else {
            driverName.setText(getString(R.string.driver_name_text));
        }

        //setOnlineOffline();


        editProfile.setOnClickListener(this);
        tripHistoryLayout.setOnClickListener(this);
        paymentHistoryLayout.setOnClickListener(this);
        changeNumber.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        logoutLayout.setOnClickListener(this);
    }


    @SuppressLint({"ResourceAsColor", "ClickableViewAccessibility"})
    private void setOnlineOffline() {

        checkDriverActive();

        btnSwitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch: ");
                isTouched = true;
                return false;
            }
        });

        btnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isTouched) {
                    isTouched = false;
                    if(isChecked){
                        switchState.setText(R.string.online);
                        Log.d(TAG, "onCheckedChanged: Online ");
                        switchActiveCall(true);
                    } else {
                        switchState.setText(R.string.offline);
                        Log.d(TAG, "onCheckedChanged: Offline ");
                        switchActiveCall(false);

                    }
                }
            }
        });
    }

    private void switchActiveCall(boolean isOnline) {
        DataUtils dataUtils = new DataUtils(getApplicationContext());
        String token = "Bearer "+ dataUtils.getStr("access");
        Log.d(TAG, "switchActiveCall: "+ isOnline);

        if(((ItemDeliveryApplication) getApplicationContext()).getUser().getId() != 0){
            AuthenticationService authenticationService = RetrofitInstance.getService(getApplicationContext());
            Call<User> call =  authenticationService.setActive(token, ((ItemDeliveryApplication) getApplicationContext()).getUser().getId(), isOnline);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.body() != null){
                        ((ItemDeliveryApplication) getApplicationContext()).setUser(response.body());
                        Log.d(TAG, "onResponseActive: "+ ((ItemDeliveryApplication) getApplicationContext()).getUser().getIs_active());
                    }
                    if (response.errorBody() != null) {
                        Log.d(TAG, "ResponseErrorBody: " + response.errorBody());
                        Toast.makeText(getApplicationContext(),
                                "Something is wrong! Please try again later.",
                                Toast.LENGTH_SHORT).show();
                    }
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
                }
            });
        }

    }

    private void setImageToView(String uri) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_IN );
        circularProgressDrawable.setStrokeWidth(8f);
        circularProgressDrawable.setCenterRadius(40f);
        circularProgressDrawable.start();


        Glide.with(this).asDrawable()
                .placeholder(circularProgressDrawable)
                .load(uri)
                .into(driverProfilePic);
    }

    private void checkDriverActive() {
        if(((ItemDeliveryApplication) getApplicationContext()).getUser().getIs_active()){
            switchState.setText(R.string.online);
            btnSwitch.setChecked(true);
        } else {
            switchState.setText(R.string.offline);
            btnSwitch.setChecked(false);
        }
    }

    private void initFindView() {

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
        driverProfilePic = findViewById(R.id.driver_profile_pic);
    }

    private void initToolbar() {

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
