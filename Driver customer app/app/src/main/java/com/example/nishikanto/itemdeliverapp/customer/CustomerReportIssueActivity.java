package com.example.nishikanto.itemdeliverapp.customer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.nishikanto.itemdeliverapp.R;

public class CustomerReportIssueActivity extends AppCompatActivity {

    private ListView listView ;
    private TextView tvTitle;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"RestrictedApi", "ResourceAsColor", "WrongConstant"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_report_issue);
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
        tvTitle.setText(R.string.report_issue);
        tvTitle.setTypeface(custom_font);
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.colorBlackLight2));
        actionBar.setTitle(tvTitle.getText());



        // Defined Array values to show in ListView
        String[] values = new String[] {getString(R.string.issue1),
                getString(R.string.issue2),
                getString(R.string.issue3),
                getString(R.string.issue1),
                getString(R.string.issue2),
                getString(R.string.issue3)
        };


        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, values);

        final ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);



        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);


                Intent i=new Intent(getBaseContext(), IssueWritingActivity.class);
                startActivity(i);
                finish();

            }

        });


    }
}
