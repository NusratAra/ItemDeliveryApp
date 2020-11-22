package com.example.nishikanto.itemdeliverapp.customer;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.nishikanto.itemdeliverapp.ItemDeliveryApplication;
import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.model.Issue;
import com.example.nishikanto.itemdeliverapp.model.NewIssue;
import com.example.nishikanto.itemdeliverapp.model.Trip;
import com.example.nishikanto.itemdeliverapp.services.NoConnectivityException;
import com.example.nishikanto.itemdeliverapp.services.RetrofitInstance;
import com.example.nishikanto.itemdeliverapp.services.TripAuthenticationService;
import com.google.gson.GsonBuilder;

import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IssueWritingActivity extends AppCompatActivity {

    private static final String TAG = IssueWritingActivity.class.getSimpleName();
    private FancyButton btnSend;
    private TextView tvTitle;
    private EditText write_issue;
    private TextView issue_count;
    private int MAX_LINE = 140;

    private Toolbar toolbar;
    private ActionBar actionBar;
    private Issue issue;
    private TextView tripCode;
    private Trip trip;
    private int tripId;
    private String issueType;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"ResourceAsColor", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_writing);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initFindView();
        getIntentValue();

        initToolbar();

        initTextWatcher();



        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                issuePostCall(tripId, write_issue.getText().toString());
            }
        });

    }

    private void issuePostCall(int tripId, String  issueText) {
        ////////////////////////////////////////////////////////////////
//        int issueId = 1;
        int issueId = issue.getId();
        ///////////////////////////////////////////////////////////////
        Log.d(TAG, "issuePostCall: "+ tripId+"+"+issueText+"+"+issueId);
        TripAuthenticationService tripAuthenticationService = RetrofitInstance.getServiceCall(getApplicationContext());
        Call<NewIssue> issueCall = tripAuthenticationService.addNewIssue(tripId, issueText, issueId);
        issueCall.enqueue(new Callback<NewIssue>() {
            @Override
            public void onResponse(Call<NewIssue> call, Response<NewIssue> response) {
                if(response.body() != null){
                    Log.d(TAG, "PostedIssue: "+ new GsonBuilder().setPrettyPrinting().create().toJson((response.body())));
                    Toast.makeText(IssueWritingActivity.this,
                            "Your report is recorded.", Toast.LENGTH_SHORT).show();
                }
                if(response.errorBody() != null){
                    Toast.makeText(IssueWritingActivity.this,
                            "Something is wrong! Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewIssue> call, Throwable t) {
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

    @SuppressLint("SetTextI18n")
    private void getIntentValue() {
        issue = getIntent().getParcelableExtra("issue");
        trip = ((ItemDeliveryApplication) getApplicationContext()).getTrip();

        Log.d(TAG, "GetIntentTrip: "+ new GsonBuilder().setPrettyPrinting().create().toJson((trip)));
        Log.d(TAG, "GetIntentIssue: "+ new GsonBuilder().setPrettyPrinting().create().toJson((issue)));

        if(trip != null && issue != null){
            tripId = trip.getId();
            issueType = issue.getType();
            tripCode.setText(""+getString(R.string.issue_text)+ trip.getTrip_code());
        }

    }

    private void initTextWatcher() {

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                issue_count.setText(s.length()+getString( R.string.word_count));
                if(s.length() == MAX_LINE){
                    issue_count.setTextColor(getResources().getColor(R.color.colorMagentaDark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        write_issue.addTextChangedListener(textWatcher);
    }

    private void initFindView() {
        write_issue = findViewById(R.id.write_issue);
        issue_count = findViewById(R.id.issue_count);
        tripCode = findViewById(R.id.trip_code);
        btnSend = findViewById(R.id.btn_send);
        btnSend.setCustomTextFont(R.font.poppins_medium);
    }

    @SuppressLint("SetTextI18n")
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
        tvTitle.setText(""+issueType);
        tvTitle.setTypeface(custom_font);
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.colorBlackLight2));
        actionBar.setTitle(tvTitle.getText());

    }
}
