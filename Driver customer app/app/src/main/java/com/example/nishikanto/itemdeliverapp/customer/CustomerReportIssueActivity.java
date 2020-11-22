package com.example.nishikanto.itemdeliverapp.customer;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.adapter.IssueReportAdapter;
import com.example.nishikanto.itemdeliverapp.model.Issue;
import com.example.nishikanto.itemdeliverapp.model.Issues;
import com.example.nishikanto.itemdeliverapp.services.NoConnectivityException;
import com.example.nishikanto.itemdeliverapp.services.RetrofitInstance;
import com.example.nishikanto.itemdeliverapp.services.TripAuthenticationService;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerReportIssueActivity extends AppCompatActivity {
    private static final String TAG = CustomerReportIssueActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private ListView listView ;
    private TextView tvTitle;

    private Toolbar toolbar;
    private ActionBar actionBar;

    private IssueReportAdapter issueReportAdapter;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"RestrictedApi", "ResourceAsColor", "WrongConstant"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_report_issue);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initToolbar();
        getAllTripIssue();

    }

    private void getAllTripIssue() {
        TripAuthenticationService tripAuthenticationService = RetrofitInstance.getServiceCall(getApplicationContext());
        Call<Issues> issueCall = tripAuthenticationService.getTripIssues();
        issueCall.enqueue(new Callback<Issues>() {
            @Override
            public void onResponse(Call<Issues> call, Response<Issues> response) {
                if(response.body() != null){
                    Log.d(TAG, "TrackIssue: "+ new GsonBuilder().setPrettyPrinting().create().toJson(response.body().getIssues()));

                    initRecyclerView(response.body().getIssues());

                }
            }

            @Override
            public void onFailure(Call<Issues> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    Log.e(TAG, "onFailureThrowEx: " + t.getMessage());
                    Toast toast = Toast.makeText(getApplicationContext(), "Check your internet connection.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Server Error!", Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }            }
        });
    }

    private void initRecyclerView(ArrayList<Issue> issuesArrayList) {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_report);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        issueReportAdapter = new IssueReportAdapter(this, issuesArrayList);
        recyclerView.setAdapter(issueReportAdapter);
    }

    private void initToolbar() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Typeface custom_font = ResourcesCompat.getFont(this, R.font.poppins_medium);


        actionBar = getSupportActionBar();

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
        tvTitle.setText(R.string.report_issue);
        tvTitle.setTypeface(custom_font);
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.colorBlackLight2));
        actionBar.setTitle(tvTitle.getText());
    }
}
