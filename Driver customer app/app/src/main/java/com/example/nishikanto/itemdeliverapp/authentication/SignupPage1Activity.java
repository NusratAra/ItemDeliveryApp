package com.example.nishikanto.itemdeliverapp.authentication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.nishikanto.itemdeliverapp.R;
import com.example.nishikanto.itemdeliverapp.model.User;
import com.example.nishikanto.itemdeliverapp.utils.PathUtils;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import mehdi.sakout.fancybuttons.FancyButton;

//import com.zhihu.matisse.Matisse;
//import com.zhihu.matisse.MimeType;
//import com.zhihu.matisse.engine.impl.GlideEngine;
//import com.zhihu.matisse.filter.Filter;

public class SignupPage1Activity extends AppCompatActivity {

    private static final String TAG = SignupPage1Activity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE = 23;

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

    private FrameLayout profileImageLayout;
    private CircleImageView circleViewImage;

    private User user;
//    private Uri imageUrl;
    private String imageUrlString = "";


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page1);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        initToolbar();
        initFindView();
        initSpinner();


        profileImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageCallback();
            }
        });

        nextButton.setCustomTextFont(R.font.poppins_medium);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Image: " + imageUrlString);

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



        regToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getBaseContext(), LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void initSpinner() {

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
    }

    private void initFindView() {

        name = findViewById(R.id.input_name);
        phoneNo = findViewById(R.id.contact_number);
        email = findViewById(R.id.email);
        postalCode = findViewById(R.id.postal_code);
        address = findViewById(R.id.address);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);

        nextButton = findViewById(R.id.next_button);
        regToLogin = findViewById(R.id.reg_to_login);

        profileImageLayout = findViewById(R.id.profile_image_layout);
        circleViewImage = findViewById(R.id.circle_view_image);
    }

    private void initToolbar() {

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
    }

    private void imageCallback() {

//        Matisse.from(SignupPage1Activity.this)
//                .choose(MimeType.ofAll())
//                .countable(true)
//                .maxSelectable(9)
//                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.album_item_height))
//                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
//                .thumbnailScale(0.85f)
//                .imageEngine(new GlideEngine())
//                .showPreview(true) // Default is `true`
//                .forResult(REQUEST_CODE_CHOOSE);

        ImagePicker.Companion.with(this)    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: ");

            Uri selectedImage = Uri.fromFile(ImagePicker.Companion.getFile(data));
//            imageUrl = selectedImage.get(0);
            Log.d(TAG, "File1: " + selectedImage);


            imageUrlString = saveBitmap(selectedImage);

            setImageToView(imageUrlString);
            Log.d(TAG, "File2: " + imageUrlString);
//            File file = new File(uri);
//            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
//            Log.d(TAG, "File: " + uri);
//            MultipartBody.Part body = MultipartBody.Part.createFormData("profile_or_logo", file.getName(), requestFile);
//            UpdateImageCall(body);
        }
    }

    private void setImageToView(String uri) {
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


    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
                //TODO null check imageUrl
                !imageUrlString.equals("") &&
                !nameText.equals("") &&
                !emailText.equals("") &&
                !phoneNoText.equals("") &&
                !cityText.equals("") &&
                !postalCodeText.equals("") &&
                !addressText.equals("") &&
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
        intent.putExtra("image_url", imageUrlString);
        startActivity(intent);

//        if (isEmailValid(emailText)) {
//            Intent intent=new Intent(getBaseContext(), SignupPage2Activity.class);
//            intent.putExtra("name", nameText);
//            intent.putExtra("phone", phoneNoText);
//            intent.putExtra("email", emailText);
//            intent.putExtra("city", cityText);
//            intent.putExtra("postal_code", postalCodeText);
//            intent.putExtra("address", addressText);
//            intent.putExtra("password", passwordText);
//            intent.putExtra("confirm_pass", confirmPasswordText);
//            intent.putExtra("image_url", imageUrlString);
//            startActivity(intent);
//        } else {
//            Toast.makeText(this, "Invalid Email!!!", Toast.LENGTH_SHORT).show();
//        }

    }
}
