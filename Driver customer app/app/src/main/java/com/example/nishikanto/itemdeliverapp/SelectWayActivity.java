package com.example.nishikanto.itemdeliverapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
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
                Log.d(TAG, "onCheckedChanged: "+ isChecked);
                if(!isChecked && Locale.getDefault().getLanguage().equals("en")){
                    setLocal("ar");
                    recreate();
                } else if(isChecked && Locale.getDefault().getLanguage().equals("ar")){
                    setLocal("en");
                    recreate();
                }


            }
        });

        if(dataUtils.getStr("lang").equals("ar")){
            btnSwitch.setChecked(false);
        } else{
            btnSwitch.setChecked(true);
        }
    }

    private void setLocal(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        dataUtils.setStr("lang", lang);

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
