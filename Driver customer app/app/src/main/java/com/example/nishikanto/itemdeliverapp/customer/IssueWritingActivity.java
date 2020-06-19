package com.example.nishikanto.itemdeliverapp.customer;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.nishikanto.itemdeliverapp.R;

import mehdi.sakout.fancybuttons.FancyButton;

public class IssueWritingActivity extends AppCompatActivity {

    private FancyButton btnSend;
    private TextView tvTitle;
    private EditText write_issue;
    private TextView issue_count;
    private int MAX_LINE = 140;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"ResourceAsColor", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_writing);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Typeface custom_font = ResourcesCompat.getFont(this, R.font.poppins_medium);


        ActionBar actionBar = getSupportActionBar();

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

        tvTitle.setText(R.string.issue2);
        tvTitle.setTypeface(custom_font);
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.colorBlackLight2));
        actionBar.setTitle(tvTitle.getText());

        write_issue = findViewById(R.id.write_issue);
        issue_count = findViewById(R.id.issue_count);

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


        btnSend = findViewById(R.id.btn_send);
        btnSend.setCustomTextFont(R.font.poppins_medium);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(IssueWritingActivity.this,
                        "Your report is recorded.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
