package com.example.nishikanto.itemdeliverapp.authentication;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
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
import com.example.nishikanto.itemdeliverapp.verification.DriverOTP1Activity;
import com.google.gson.JsonElement;

import jp.wasabeef.blurry.Blurry;
import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupPage2Activity extends AppCompatActivity {
    private static final String TAG = SignupPage2Activity.class.getSimpleName();

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

    private boolean isCheckedTerm = true;
    private String role = "D";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page2);
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
        getIntentValues();

        nationalId = findViewById(R.id.national_id);
        vehiclePlateNo = findViewById(R.id.vehicle_plate_number);
        vehiclePlateModel = findViewById(R.id.vehicle_model);
        ibenNo = findViewById(R.id.iben_number);

        registerButton = findViewById(R.id.reg_button);
        checkbox = findViewById(R.id.checkbox);
        checkbox.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_medium));
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox.isChecked()) {
                    isCheckedTerm = true;
                } else {

                    //to be false
                    isCheckedTerm = true;
                }
            }
        });

        registerButton.setCustomTextFont(R.font.poppins_medium);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (!isFieldsEmpty()) {
                    registerRequestCall();
                    //undo later
                    //showDialog();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please fill up all fields!", Toast.LENGTH_SHORT);
                    toast.show();
                }
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

    }

    private boolean isFieldsEmpty() {

        if (
                !nationalId.getText().toString().equals("") &&
                !vehiclePlateNo.getText().toString().equals("") &&
                !vehiclePlateModel.getText().toString().equals("") &&
                !ibenNo.getText().toString().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    private void registerRequestCall() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        int nationalIdText = Integer.parseInt(nationalId.getText().toString());
        String vehiclePlateNoText = vehiclePlateNo.getText().toString();
        int vehicleModelNoText = Integer.parseInt(vehiclePlateModel.getText().toString());
        int ibenNoText = Integer.parseInt(ibenNo.getText().toString());

        Log.d(TAG, "registerRequestCall: " + emailText + "+" + nameText + "+" + isCheckedTerm + "+" + role + "+" +
                phoneNoText + "+" + cityText + "+" + postalCodeText + "+" + addressText + "+" + passwordText + "+" + confirmPasswordText);

        AuthenticationService authenticationService = RetrofitInstance.getService(getApplicationContext());
        //terms, role,
        Call<JsonElement> userCall = authenticationService.register(emailText, nameText, isCheckedTerm, role, phoneNoText, cityText, postalCodeText,
                addressText, nationalIdText, vehiclePlateNoText, vehicleModelNoText, ibenNoText, passwordText, confirmPasswordText);
        userCall.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.body() != null) {
                    Log.d(TAG, "SuccessAuth1: " + response.body());
                    Intent i = new Intent(getBaseContext(), LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
                if (response.errorBody() != null) {
                    Log.d(TAG, "SuccessAuth2: " + response.errorBody());
                    Toast.makeText(getApplicationContext(),
                            "This email already exists! Please try with a different one.",
                            Toast.LENGTH_SHORT).show();
                }
                if (response.isSuccessful()) {
                    Log.d(TAG, "SUCCESS: " + response.code());
                } else {
                    Log.d(TAG, "Failed: " + response.code());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                if (t instanceof NoConnectivityException) {
                    Log.e(TAG, "onFailureThrowEx: " + t.getMessage());
                    Toast toast = Toast.makeText(getApplicationContext(), "Check your internet connection.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Server Error!", Toast.LENGTH_SHORT);
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

//
//        Bitmap map = takeScreenShot(SignupPage2Activity.this);
//
//        Bitmap fast = fastblur(map, 10);
//        final Drawable draw = new BitmapDrawable(getResources(), fast);
//        linearLayout.setBackground(draw);
////

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

    private static Bitmap takeScreenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);

        view.destroyDrawingCache();
        return b;
    }

    public Bitmap fastblur(Bitmap sentBitmap, int radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);


                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];


                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }
}
