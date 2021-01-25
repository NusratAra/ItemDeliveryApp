package com.example.nishikanto.itemdeliverapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.nishikanto.itemdeliverapp.authentication.LoginActivity;
import com.example.nishikanto.itemdeliverapp.customer.TypeTrackIdActivity;
import com.example.nishikanto.itemdeliverapp.utils.DataUtils;
import com.example.nishikanto.itemdeliverapp.utils.LocaleHelper;

import java.util.Locale;

public class SelectWayActivity extends AppCompatActivity {
    private static final String TAG = SelectWayActivity.class.getSimpleName();
    private SwitchCompat btnSwitch;
    private TextView customerText;
    private TextView driverText;
    private ImageView customer_img;
    private ImageView driver_img;


    private DataUtils dataUtils;


    @SuppressLint({"WrongViewCast", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocaleHelper.setLocale(this, LocaleHelper.getLanguage(this));

        setContentView(R.layout.activity_select_way);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        dataUtils = new DataUtils(getApplicationContext());

        Typeface custom_font = ResourcesCompat.getFont(this, R.font.poppins_medium);

        customerText = findViewById(R.id.customer_text);
        driverText = findViewById(R.id.driver_text);

        customer_img =findViewById(R.id.user_img);
        driver_img = findViewById(R.id.driver_img);

        customerText.setTypeface(custom_font);
        driverText.setTypeface(custom_font);

        driverText.setOnClickListener(driverClickListener);
        driver_img.setOnClickListener(driverClickListener);
        customerText.setOnClickListener(customerClickListener);
        customer_img.setOnClickListener(customerClickListener);
        btnSwitch = findViewById(R.id.btn_switch);



        btnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked && Locale.getDefault().getLanguage().equals("en")){
                    LocaleHelper.setLocale(SelectWayActivity.this, "ar");
                    recreate();
                } else if(isChecked && Locale.getDefault().getLanguage().equals("ar")){
                    LocaleHelper.setLocale(SelectWayActivity.this, "en");
                    recreate();
                }
            }
        });

        if(LocaleHelper.getLanguage(SelectWayActivity.this).equals("en")){
            btnSwitch.setChecked(true);
        }else {
            btnSwitch.setChecked(false);
        }

    }


    View.OnClickListener customerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i=new Intent(getBaseContext(), TypeTrackIdActivity.class);
            startActivity(i);
        }
    };

    View.OnClickListener driverClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i=new Intent(getBaseContext(), LoginActivity.class);
            startActivity(i);
        }
    };
}
