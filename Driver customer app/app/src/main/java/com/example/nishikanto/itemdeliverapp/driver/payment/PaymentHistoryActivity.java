package com.example.nishikanto.itemdeliverapp.driver.payment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.adapter.PaymentHistoryAdaper;

import java.util.ArrayList;

public class PaymentHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private PaymentHistoryAdaper paymentHistoryAdaper;

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = LayoutInflater.from(this).inflate(R.layout.custom_toolbar_payment, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        actionBar.setCustomView(customView, params);
        actionBar.getCustomView().findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });





//        final Drawable upArrow = getResources().getDrawable(R.drawable.arrow_back);
//        upArrow.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
//        getSupportActionBar().setHomeAsUpIndicator(upArrow);
//        getSupportActionBar().setElevation(0);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            toolbar.setForegroundGravity(Gravity.CENTER_VERTICAL);
//        }
//        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);

        toolbar.setElevation(0);




//TODO: Calender icon
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_payment);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ArrayList<String> dates = new ArrayList<>();
        dates.add(getString(R.string.date1));
        dates.add(getString(R.string.date2));
        dates.add(getString(R.string.date3));
        dates.add(getString(R.string.date1));
        dates.add(getString(R.string.date2));
        dates.add(getString(R.string.date3));
        dates.add(getString(R.string.date1));
        dates.add(getString(R.string.date2));

        // specify an adapter (see also next example)
        paymentHistoryAdaper = new PaymentHistoryAdaper(this, dates);
        recyclerView.setAdapter(paymentHistoryAdaper);
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
