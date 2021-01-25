package com.example.nishikanto.itemdeliverapp.driver.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.nishikanto.itemdeliverapp.ItemDeliveryApplication;
import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.model.User;
import com.example.nishikanto.itemdeliverapp.services.AuthenticationService;
import com.example.nishikanto.itemdeliverapp.services.NoConnectivityException;
import com.example.nishikanto.itemdeliverapp.services.RetrofitInstance;
import com.example.nishikanto.itemdeliverapp.utils.BaseUrlUtils;
import com.example.nishikanto.itemdeliverapp.utils.DataUtils;
import com.example.nishikanto.itemdeliverapp.utils.PathUtils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import mehdi.sakout.fancybuttons.FancyButton;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.zhihu.matisse.Matisse;
//import com.zhihu.matisse.MimeType;
//import com.zhihu.matisse.engine.impl.GlideEngine;
//import com.zhihu.matisse.filter.Filter;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = EditProfileActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE = 2404;
    private static String BASE_URL = "http://134.209.102.212";


    private TextView tvTitle;
    private FancyButton updateButton;

    private FrameLayout profileImageLayout;
    private CircleImageView circleViewImage;
    private EditText name;
    private EditText phoneNo;
    private EditText email;
    private EditText postalCode;
    private EditText address;
    private EditText password;
    private EditText confirmPassword;
    public RadioGroup radioGroup;
    private FancyButton registerButton;
    private CheckBox checkbox;
    private EditText nationalId;
    private EditText vehiclePlateNo;
    private EditText vehiclePlateModel;
    private EditText ibenNo;
    private Spinner spin;

    private String[] cityName;
    private User user;
    private String accessToken;
    private DataUtils dataUtils;
    private String imageUrl = "";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initToolbar();
        findAllViewValue();
        initSpinner();
        addFieldValue();



        dataUtils = new DataUtils(getApplicationContext());
        accessToken = "Bearer "+ dataUtils.getStr("access");
        
        profileImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageCallback();
            }
        });
        
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfileCall();
//                if(isEmailValid(email.getText().toString())){
//                    updateProfileCall();
//                } else {
//                    Toast.makeText(getApplicationContext(), getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
//                }
            }
        });

    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void imageCallback() {

        ImagePicker.Companion.with(this)	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: "+ requestCode +"+"+ resultCode);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {

            Uri selectedImage = Uri.fromFile(ImagePicker.Companion.getFile(data));

            String imageUriLocal = saveBitmap(selectedImage);

            setImageToView(imageUriLocal);

            File file = new File(imageUriLocal);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            Log.d(TAG, "EditProfileFile: " + imageUriLocal);
            MultipartBody.Part body = MultipartBody.Part.createFormData("profile_or_logo", file.getName(), requestFile);
            UpdateImageCall(body);
        }
    }

    private String saveBitmap(Uri uri) {
        Bitmap bitmap = null;

        try {
            File fileName = new File(PathUtils.getRealPathFromURI(this, uri));

            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);

            OutputStream fOut = null;
            File file = new File(getExternalCacheDir(), "/"+fileName.getName());

            fOut = new FileOutputStream(file);
            scaled.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            return file.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    private void UpdateImageCall(MultipartBody.Part requestFile) {

        if(((ItemDeliveryApplication)getApplicationContext()).getUser().getId() != 0){
            Log.d(TAG, "Data: " + accessToken+"+"+((ItemDeliveryApplication)getApplicationContext()).getUser().getId()+ "+"+ requestFile);
            AuthenticationService authenticationService = RetrofitInstance.getService(getApplicationContext());
            Call<User> userCall =  authenticationService.updateProfileImage(accessToken, ((ItemDeliveryApplication)getApplicationContext()).getUser().getId(), requestFile);

            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.body() != null) {
                        Log.d(TAG, "ResponseBodyImage: " + response.body().getProfile_or_logo());
                        getUserInfo(response.body());
                        imageUrl = BASE_URL+response.body().getProfile_or_logo();
                        Glide.with(EditProfileActivity.this).asDrawable()
                                .load(imageUrl)
                                .into(circleViewImage);

                    }
                    if (response.errorBody() != null) {
                        Log.d(TAG, "ResponseErrorBodyImage: " + response.errorBody());
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.something_wrong),
                                Toast.LENGTH_SHORT).show();
                    }
                    if (response.isSuccessful()) {
                        Log.d(TAG, "SUCCESS: Image" + response.message());
                    } else {
                        Log.d(TAG, "Failed: Image" + response.message());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    if (t instanceof NoConnectivityException) {
                        Log.e(TAG, "onFailureThrowEx: " + t.getMessage());
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.server_error_customer), Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.server_error), Toast.LENGTH_SHORT);
                        toast.show();
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                }
            });
        }


    }

    private void setImageToView(String uri) {
        Log.d(TAG, "setImageToView: "+ uri);
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_IN );
        circularProgressDrawable.setStrokeWidth(8f);
        circularProgressDrawable.setCenterRadius(40f);
        circularProgressDrawable.start();


        Glide.with(this).asDrawable()
                .placeholder(circularProgressDrawable)
                .load(uri)
                .into(circleViewImage);
    }

    private void updateProfileCall() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        if(user.getId() != 0){
            Log.d(TAG, "updateProfileCallName: "+ name.getText().toString());
            AuthenticationService authenticationService = RetrofitInstance.getService(getApplicationContext());
            Call<User> userCall = authenticationService.updateProfile(accessToken,
                    user.getId(),
                    email.getText().toString(),
                    name.getText().toString(),
                    spin.getSelectedItem().toString(),
                    postalCode.getText().toString(),
                    address.getText().toString(),
                    Integer.parseInt(nationalId.getText().toString()),
                    vehiclePlateNo.getText().toString(),
                    vehiclePlateModel.getText().toString(),
                    Integer.parseInt(ibenNo.getText().toString()));

            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.body() != null) {
                        Log.d(TAG, "ResponseBody: "+ new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));

                        getUserInfo(response.body());
                        onBackPressed();
                    }
                    if (response.errorBody() != null) {
                        Log.d(TAG, "ResponseErrorBody: " + response.errorBody());
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.something_wrong),
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
                public void onFailure(Call<User> call, Throwable t) {
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

    }

    private void getUserInfo(User response) {
        user = new User();
        user.setId(response.getId());
        user.setEmail(response.getEmail());
        user.setUsername(response.getUsername());
        user.setCity(response.getCity());
        user.setAddress(response.getAddress());
        user.setPostal_code(response.getPostal_code());
        user.setIqma_national_id(response.getIqma_national_id());
        user.setVehicale_plate_no(response.getVehicale_plate_no());
        user.setVehicale_model(response.getVehicale_model());
        user.setIebn(response.getIebn());
        user.setRole(response.getRole());
        user.setIs_active(response.getIs_active());
        user.setIs_verified(response.getIs_verified());
        user.setDate_joined(response.getDate_joined());
        String string = ""+BASE_URL+response.getProfile_or_logo();
        Log.d(TAG, "onResponseUser: "+ string);

        user.setProfile_or_logo(response.getProfile_or_logo());

        ((ItemDeliveryApplication) getApplicationContext()).setUser(user);
    }

    @Override
    public void onBackPressed() {

        if(!imageUrl.equals("")){
            Intent resultIntent = new Intent();
            Log.d(TAG, "onBackPressed: "+ imageUrl);
            resultIntent.putExtra("image_uri", imageUrl);
            setResult(Activity.RESULT_OK, resultIntent);
        }

        super.onBackPressed();
    }

    private void findAllViewValue() {
        name = findViewById(R.id.input_name);
        email = findViewById(R.id.email);
        spin =  findViewById(R.id.city);
        postalCode = findViewById(R.id.postal_code);
        address = findViewById(R.id.address);
        nationalId = findViewById(R.id.national_id);
        vehiclePlateNo = findViewById(R.id.vehicle_plate_number);
        vehiclePlateModel = findViewById(R.id.vehicle_model);
        ibenNo = findViewById(R.id.iben_number);
        updateButton = findViewById(R.id.update_button);
        profileImageLayout = findViewById(R.id.profile_image_layout);
        circleViewImage = findViewById(R.id.circle_view_image);
    }

    private void addFieldValue() {
        user = ((ItemDeliveryApplication) getApplicationContext()).getUser();
        Log.d(TAG, "UserValue: "+ user.getUsername());
        if(user != null){

            name.setText(user.getUsername());
            email.setText(user.getEmail());
            postalCode.setText(user.getPostal_code());
            address.setText(user.getAddress());
            nationalId.setText(user.getIqma_national_id());
            vehiclePlateNo.setText(user.getVehicale_plate_no());
            vehiclePlateModel.setText(user.getVehicale_model());
            ibenNo.setText(user.getIebn());
            spin.getSelectedItemPosition();

            for(int i=0; i<cityName.length; i++){

                if(cityName[i].matches(user.getCity())){
                    spin.setSelection(i);
                } else {
                    Log.d(TAG, "addFieldValue2: "+ cityName[i]);
                }
            }

            if(user.getProfile_or_logo() != null){
                Log.d(TAG, "ProfilePicUrl: "+ BaseUrlUtils.BASE_URL+user.getProfile_or_logo());
//                setImageToView(BaseUrlUtils.BASE_URL+user.getProfile_or_logo());
                Glide.with(this).asDrawable()
                        .load(BaseUrlUtils.BASE_URL+user.getProfile_or_logo())
                        .into(circleViewImage);
            }

//            Log.d(TAG, "addFieldValue: "+ valueMap.get(cityName).contains(user.getAddress()));
//            Log.d(TAG, "addFieldValue: "+ valueMap.get());

        }

    }

    private void initSpinner() {

        cityName = new String[]{
                "City",
                getString(R.string.city),
                getString(R.string.city1),
                getString(R.string.city2)
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item_profile_edit, cityName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
    }

    private void initToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        Typeface custom_font = ResourcesCompat.getFont(this, R.font.poppins_medium);


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
        tvTitle.setText(R.string.edit_profile_text);
        tvTitle.setTypeface(custom_font);
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.colorBlackLight2));
        actionBar.setTitle(tvTitle.getText());
    }
}