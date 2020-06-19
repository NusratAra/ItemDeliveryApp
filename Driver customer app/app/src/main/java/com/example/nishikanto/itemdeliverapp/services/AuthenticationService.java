package com.example.nishikanto.itemdeliverapp.services;

import com.example.nishikanto.itemdeliverapp.model.User;
import com.example.nishikanto.itemdeliverapp.model.UserCredential;
import com.google.gson.JsonElement;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AuthenticationService {

    @FormUrlEncoded
    @POST("jwt/token/obtain/")
    Call<UserCredential> getTokens(@Field("email") String email,
                                   @Field("password") String password);

    @GET("api/auth/me/")
    Call<User> getAuthUser(@Header("Authorization") String  token);

    @GET("api/driver-profile/{id}")
    Call<User> getDriverProfile(@Header("Authorization") String  token,
                                @Path(value = "id", encoded = true) int id);

    @FormUrlEncoded
    @POST("jwt/token/refresh/")
    Call<UserCredential> refreshToken(@Field("refresh") String refresh);

    @FormUrlEncoded
    @POST("auth/driver-register/")
    Call<JsonElement> register(@Field("email") String email,
                              @Field("username") String username,
                              @Field("terms_conditions") boolean terms_conditions,
                              @Field("role") String role,
                              @Field("contact_number") String contact_number,
                              @Field("city") String city,
                              @Field("postal_code") String postal_code,
                              @Field("address") String address,
                              @Field("iqma_national_id") int iqma_national_id,
                              @Field("vehicale_plate_no") String vehicale_plate_no,
                              @Field("vehicale_model") int vehicale_model,
                              @Field("iebn") int iebn,
                              @Field("password1") String password1,
                              @Field("password2") String password2);

    @FormUrlEncoded
    @POST("auth/request-password-reset/")
    Call<JSONObject> forgetPass(@Field("email") String email);

    @FormUrlEncoded
    @POST("auth/password-reset/")
    Call<JSONObject> resetPass(@Field("password") String password,
                         @Field("confirm_password") String confirm_password,
                         @Field("code") int code);

    @FormUrlEncoded
    @POST("auth/password-change/")
    Call<JsonElement> changePass(@Header("Authorization") String  token,
                                 @Field("new_password") String new_password,
                                 @Field("confirm_password") String confirm_password);
}
