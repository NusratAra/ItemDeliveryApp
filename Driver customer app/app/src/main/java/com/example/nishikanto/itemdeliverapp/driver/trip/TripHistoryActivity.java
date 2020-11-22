package com.example.nishikanto.itemdeliverapp.driver.trip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.adapter.TripHistoryAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class TripHistoryActivity extends AppCompatActivity {

    private static final String TAG = TripHistoryActivity.class.getSimpleName();

    private ActionBar actionBar;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TripHistoryAdapter tripHistoryAdaper;
    private ImageView filter;

    private BottomSheetBehavior mBottomSheetBehaviour;
    private View blurView;

    private View view_all;
    private View view_complete;
    private View view_expired;

    private TextView text_all;
    private TextView text_complete;
    private TextView text_expired;
    private TextView text_cancel;

    private ImageView tik_all;
    private ImageView tik_complete;
    private ImageView tik_expired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);

        initToolbar();
        initView();

        text_all.setOnClickListener(textAllListener);
        text_complete.setOnClickListener(textCompleteListener);
        text_expired.setOnClickListener(textExpiredListener);
        text_cancel.setOnClickListener(textCancelListener);

        bottomSheetClick();

        //TODO: funnel icon
        initRecyclerView();

    }



    private void bottomSheetClick() {

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

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void initRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_payment);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        tripHistoryAdaper = new TripHistoryAdapter(this);
        recyclerView.setAdapter(tripHistoryAdaper);

    }

    private void initView() {

        filter = actionBar.getCustomView().findViewById(R.id.filter);
        blurView = findViewById(R.id.blur_view);

        view_all = findViewById(R.id.view_all);
        view_complete = findViewById(R.id.view_complete);
        view_expired = findViewById(R.id.view_expired);

        tik_all = findViewById(R.id.tik_all);
        tik_complete = findViewById(R.id.tik_complete);
        tik_expired = findViewById(R.id.tik_expired);

        text_all = findViewById(R.id.text_all);
        text_complete = findViewById(R.id.text_complete);
        text_expired = findViewById(R.id.text_expired);
        text_cancel = findViewById(R.id.text_cancel);
    }

    private void initToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = LayoutInflater.from(this).inflate(R.layout.custom_toolbar_trip, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(customView, params);
        actionBar.getCustomView().findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    View.OnClickListener textAllListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            view_all.setVisibility(View.VISIBLE);
            view_complete.setVisibility(View.INVISIBLE);
            view_expired.setVisibility(View.INVISIBLE);

            tik_all.setVisibility(View.VISIBLE);
            tik_complete.setVisibility(View.INVISIBLE);
            tik_expired.setVisibility(View.INVISIBLE);
        }
    };

    View.OnClickListener textCompleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            view_all.setVisibility(View.INVISIBLE);
            view_complete.setVisibility(View.VISIBLE);
            view_expired.setVisibility(View.INVISIBLE);

            tik_all.setVisibility(View.INVISIBLE);
            tik_complete.setVisibility(View.VISIBLE);
            tik_expired.setVisibility(View.INVISIBLE);
        }
    };

    View.OnClickListener textExpiredListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            view_all.setVisibility(View.INVISIBLE);
            view_complete.setVisibility(View.INVISIBLE);
            view_expired.setVisibility(View.VISIBLE);

            tik_all.setVisibility(View.INVISIBLE);
            tik_complete.setVisibility(View.INVISIBLE);
            tik_expired.setVisibility(View.VISIBLE);
        }
    };

    View.OnClickListener textCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    };
}
