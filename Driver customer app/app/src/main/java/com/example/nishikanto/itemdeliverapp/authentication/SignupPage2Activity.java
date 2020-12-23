package com.example.nishikanto.itemdeliverapp.authentication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.services.AuthenticationService;
import com.example.nishikanto.itemdeliverapp.services.NoConnectivityException;
import com.example.nishikanto.itemdeliverapp.services.RetrofitInstance;
import com.example.nishikanto.itemdeliverapp.utils.DataUtils;
import com.example.nishikanto.itemdeliverapp.verification.DriverOTP1Activity;
import com.google.gson.JsonElement;

import java.io.File;

import jp.wasabeef.blurry.Blurry;
import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupPage2Activity extends AppCompatActivity {
    private static final String TAG = SignupPage2Activity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE = 23;
    private static String BASE_URL = "http://134.209.102.212";

    public FancyButton btnSendVerification;
    public RadioGroup radioGroup;
    private FancyButton registerButton;

    private CheckBox checkbox;
    private EditText nationalId;
    private EditText vehiclePlateNo;
    private EditText vehiclePlateModel;
    private EditText ibenNo;

    private String nameText;
    private String phoneNoText;
    private String emailText;
    private String postalCodeText;
    private String addressText;
    private String passwordText;
    private String confirmPasswordText;
    private String cityText;
    private String imageUrl;
    private String roleString = "D";

    private ProgressDialog progressDialog;

    private boolean isCheckedTerm = true;


    private String accessToken;
    private DataUtils dataUtils;
    private int userId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page2);

        dataUtils = new DataUtils(getApplicationContext());

        initToolbar();
        getIntentValues();

        Log.d(TAG, "ImageUrl: " + imageUrl);

        initFindView();


        checkbox.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_medium));
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox.isChecked()) {
                    isCheckedTerm = true;
                } else {

                    //to be false
                    isCheckedTerm = false;
                }
            }
        });

        registerButton.setCustomTextFont(R.font.poppins_medium);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (!isFieldsEmpty()) {
                    Log.d(TAG, "TermCheck: " +isCheckedTerm);

                    imageUploadCall();
                    //undo later
                    //showDialog();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.fill_all_fields), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }

    private void initFindView() {

        nationalId = findViewById(R.id.national_id);
        vehiclePlateNo = findViewById(R.id.vehicle_plate_number);
        vehiclePlateModel = findViewById(R.id.vehicle_model);
        ibenNo = findViewById(R.id.iben_number);

        registerButton = findViewById(R.id.reg_button);
        checkbox = findViewById(R.id.checkbox);
    }

    private void initToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


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
    }

    private void getIntentValues() {

        Intent intent = getIntent();
        nameText = intent.getStringExtra("name");
        phoneNoText = intent.getStringExtra("phone");
        emailText = intent.getStringExtra("email");
        cityText = intent.getStringExtra("city");
        postalCodeText = intent.getStringExtra("postal_code");
        addressText = intent.getStringExtra("address");
        passwordText = intent.getStringExtra("password");
        confirmPasswordText = intent.getStringExtra("confirm_pass");
//        imageUrl = Uri.parse(intent.getStringExtra("image_url"));
        imageUrl = intent.getStringExtra("image_url");


    }

    private boolean isFieldsEmpty() {

        if (
                !nationalId.getText().toString().equals("") &&
                !vehiclePlateNo.getText().toString().equals("") &&
                !vehiclePlateModel.getText().toString().equals("") &&
                !ibenNo.getText().toString().equals("") &&
                !imageUrl.equals("") &&
                isCheckedTerm) {
            return false;
        } else {
            return true;
        }
    }

//    private void registerRequestCall() {
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();
//        int nationalIdText = Integer.parseInt(nationalId.getText().toString());
//        String vehiclePlateNoText = vehiclePlateNo.getText().toString();
//        int vehicleModelNoText = Integer.parseInt(vehiclePlateModel.getText().toString());
//        int ibenNoText = Integer.parseInt(ibenNo.getText().toString());
//
//        Log.d(TAG, "registerRequestCall: " + emailText + "+" + nameText + "+" + isCheckedTerm + "+" + roleString + "+" +
//                phoneNoText + "+" + cityText + "+" + postalCodeText + "+" + addressText + "+" + passwordText + "+" + confirmPasswordText);
//
//        AuthenticationService authenticationService = RetrofitInstance.getService(getApplicationContext());
//        //terms, role,
//        Call<JsonElement> userCall = authenticationService.register(emailText, nameText, isCheckedTerm, roleString, phoneNoText, cityText, postalCodeText,
//                addressText, nationalIdText, vehiclePlateNoText, vehicleModelNoText, ibenNoText, passwordText, confirmPasswordText);
//        userCall.enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                if (response.body() != null) {
//                    Log.d(TAG, "SuccessAuth1: " + response.body());
//                    if(response.body().getAsJsonObject().get("token") != null){
//                        Log.d(TAG, "TokenFromSignUp: " + response.body().getAsJsonObject().get("token"));
//                        Log.d(TAG, "IDFromSignUp: " + response.body().getAsJsonObject().get("user").getAsJsonObject().get("id"));
//
//
//                        userId = Integer.parseInt(response.body().getAsJsonObject().get("user").getAsJsonObject().get("id").toString());
//                        dataUtils.setStr("access", response.body().getAsJsonObject().get("token").toString());
//                        //imageUploadCall();
//                        progressDialog.dismiss();
//
//                        Intent i = new Intent(getBaseContext(), LoginActivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(i);
//                        finish();
//                    }
//
//                }
//                if (response.errorBody() != null) {
//                    Log.d(TAG, "SuccessAuth2: " + response.errorBody());
//                    Toast.makeText(getApplicationContext(),
//                            "This email already exists! Please try with a different one.",
//                            Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                }
//                if (response.isSuccessful()) {
//                    Log.d(TAG, "SUCCESS: " + response.code());
//                } else {
//                    Log.d(TAG, "Failed: " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonElement> call, Throwable t) {
//                if (t instanceof NoConnectivityException) {
//                    Log.e(TAG, "onFailureThrowEx: " + t.getMessage());
//                    Toast toast = Toast.makeText(getApplicationContext(), "Check your internet connection.", Toast.LENGTH_SHORT);
//                    toast.show();
//                } else {
//                    Toast toast = Toast.makeText(getApplicationContext(), "Server Error!", Toast.LENGTH_SHORT);
//                    toast.show();
//                    Log.d(TAG, "onFailure: " + t.getMessage());
//                }
//                progressDialog.dismiss();
//            }
//        });
//    }

    private void imageUploadCall() {

        String nationalIdText = nationalId.getText().toString();
        String vehiclePlateNoText = vehiclePlateNo.getText().toString();
        String vehicleModelNoText = vehiclePlateModel.getText().toString();
        String ibenNoText = ibenNo.getText().toString();

        if(imageUrl != null){
            File file = new File(imageUrl);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            Log.d(TAG, "File: " + imageUrl);
            MultipartBody.Part imageBody = MultipartBody.Part.createFormData("profile_or_logo", file.getName(), requestFile);

            RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data"), nameText);
            RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), emailText);
            RequestBody terms_conditions = RequestBody.create(MediaType.parse("multipart/form-data"), Boolean.toString(isCheckedTerm));
            RequestBody role = RequestBody.create(MediaType.parse("multipart/form-data"), roleString);
            RequestBody contact_number = RequestBody.create(MediaType.parse("multipart/form-data"), phoneNoText);
            RequestBody city = RequestBody.create(MediaType.parse("multipart/form-data"), cityText);
            RequestBody postal_code = RequestBody.create(MediaType.parse("multipart/form-data"), postalCodeText);
            RequestBody address = RequestBody.create(MediaType.parse("multipart/form-data"), addressText);
            RequestBody iqma_national_id = RequestBody.create(MediaType.parse("multipart/form-data"), nationalIdText);
            RequestBody vehicale_plate_no = RequestBody.create(MediaType.parse("multipart/form-data"), vehiclePlateNoText);
            RequestBody vehicale_model = RequestBody.create(MediaType.parse("multipart/form-data"), vehicleModelNoText);
            RequestBody iebn = RequestBody.create(MediaType.parse("multipart/form-data"), ibenNoText);
            RequestBody password1 = RequestBody.create(MediaType.parse("multipart/form-data"), passwordText);
            RequestBody password2 = RequestBody.create(MediaType.parse("multipart/form-data"), confirmPasswordText);


            UpdateImageCall(imageBody, username, email, terms_conditions, role,
                    contact_number, city, postal_code, address, iqma_national_id,
                    vehicale_plate_no, vehicale_model, iebn, password1, password2);
        }
    }

    private void UpdateImageCall(MultipartBody.Part requestFile, RequestBody username, RequestBody email, RequestBody terms_conditions, RequestBody role, RequestBody contact_number, RequestBody city, RequestBody postal_code, RequestBody address, RequestBody iqma_national_id, RequestBody vehicale_plate_no, RequestBody vehicale_model, RequestBody iebn, RequestBody password1, RequestBody password2) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        AuthenticationService authenticationService = RetrofitInstance.getService(getApplicationContext());
        Call<JsonElement> userCall =  authenticationService.register(requestFile, username, email, terms_conditions, role,
                contact_number, city, postal_code, address, iqma_national_id,
                vehicale_plate_no, vehicale_model, iebn, password1, password2);

        userCall.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.body() != null) {
                    Log.d(TAG, "ResponseBodyImage: " + response.body());
                    Intent i = new Intent(getBaseContext(), LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();


                }
                if (response.errorBody() != null) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.already_exist_email),
                            Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    Log.e(TAG, "onFailureThrowEx: " + t.getMessage());
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.server_error_customer), Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.server_error), Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
                progressDialog.dismiss();

            }
        });


    }

    public void showDialog() {
//Theme_D1NoTitleDim
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.dialog_verification_way, null);
        final Dialog alertDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        alertDialog.setContentView(deleteDialogView);

        FrameLayout linearLayout = alertDialog.findViewById(R.id.outer_layout);
        ImageView blurView = alertDialog.findViewById(R.id.blur_view);
        btnSendVerification = alertDialog.findViewById(R.id.btn_send_verification);

        btnSendVerification.setCustomTextFont(R.font.poppins_medium);


        deleteDialogView.findViewById(R.id.btn_send_verification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), DriverOTP1Activity.class);
                i.putExtra("email", emailText);
                i.putExtra("password", passwordText);
                i.putExtra("confirm_password", confirmPasswordText);
                startActivity(i);
                alertDialog.dismiss();
                finish();
            }
        });


        Blurry.with(this)
                .radius(25)
                .sampling(1)
                .color(Color.argb(80, 0, 0, 0))
                .capture(findViewById(R.id.mainLayout))
                .into(blurView);


        Window window = alertDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        alertDialog.show();

    }

}
