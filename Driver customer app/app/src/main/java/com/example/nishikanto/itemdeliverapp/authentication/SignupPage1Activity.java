package com.example.nishikanto.itemdeliverapp.authentication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nishikanto.itemdeliverapp.R;

import mehdi.sakout.fancybuttons.FancyButton;

public class SignupPage1Activity extends AppCompatActivity {

    private static final String TAG = SignupPage1Activity.class.getSimpleName();

    private FancyButton nextButton;
    private LinearLayout regToLogin;
    private EditText name;
    private EditText phoneNo;
    private EditText email;
    private EditText postalCode;
    private EditText address;
    private EditText password;
    private EditText confirmPassword;
    private Spinner spin;


    private String nameText;
    private String phoneNoText;
    private String emailText;
    private String postalCodeText;
    private String addressText;
    private String passwordText;
    private String confirmPasswordText;
    private String cityText;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page1);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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

        name = findViewById(R.id.input_name);
        phoneNo = findViewById(R.id.contact_number);
        email = findViewById(R.id.email);
        postalCode = findViewById(R.id.postal_code);
        address = findViewById(R.id.address);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);

        nextButton = findViewById(R.id.next_button);
        regToLogin = findViewById(R.id.reg_to_login);


        String[] users = new String[]{
                "City",
                getString(R.string.city),
                getString(R.string.city1),
                getString(R.string.city2)
        };
        spin =  findViewById(R.id.city);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);

        spin.setSelection(0, false);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    ((TextView) view).setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromEditText();
                if(!isFieldsEmpty()){
                    if(isMatchedPass()){
                        nextPageSignup();
                    } else {
                        Toast.makeText(getApplicationContext(), "Password didn't match!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please fill up all fields!", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        nextButton.setCustomTextFont(R.font.poppins_medium);


        regToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getBaseContext(), LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void getDataFromEditText() {

        nameText = name.getText().toString();
        phoneNoText = phoneNo.getText().toString();
        emailText = email.getText().toString();
        postalCodeText = postalCode.getText().toString();
        addressText = address.getText().toString();
        passwordText = password.getText().toString();
        confirmPasswordText = confirmPassword.getText().toString();
        cityText = spin.getSelectedItem().toString();
        if(cityText.equals("City")){
            cityText = "";
        }
    }

    private boolean isMatchedPass() {
        if(passwordText.equals(confirmPasswordText)){
            return true;
        } else {
            return false;
        }
    }

    private boolean isFieldsEmpty() {
        if(
                !nameText.equals("") &&
                !emailText.equals("") &&
                !phoneNoText.equals("") &&
                !passwordText.equals("") &&
                !confirmPasswordText.equals("")){
            return false;
        } else {
            return true;
        }
    }

    private void nextPageSignup() {

        Intent intent=new Intent(getBaseContext(), SignupPage2Activity.class);
        intent.putExtra("name", nameText);
        intent.putExtra("phone", phoneNoText);
        intent.putExtra("email", emailText);
        intent.putExtra("city", cityText);
        intent.putExtra("postal_code", postalCodeText);
        intent.putExtra("address", addressText);
        intent.putExtra("password", passwordText);
        intent.putExtra("confirm_pass", confirmPasswordText);
        startActivity(intent);
    }
}
