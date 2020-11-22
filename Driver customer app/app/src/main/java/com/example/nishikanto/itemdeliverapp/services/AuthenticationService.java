package com.example.nishikanto.itemdeliverapp.services;

import com.example.nishikanto.itemdeliverapp.model.User;
import com.example.nishikanto.itemdeliverapp.model.UserCredential;
import com.google.gson.JsonElement;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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

    @Multipart
    @POST("auth/driver-register/")
    Call<JsonElement> register(@Part MultipartBody.Part profile_or_logo,
                               @Part("username") RequestBody username,
                               @Part("email") RequestBody email,
                               @Part("terms_conditions") RequestBody terms_conditions,
                               @Part("role") RequestBody role,
                               @Part("contact_number") RequestBody contact_number,
                               @Part("city") RequestBody city,
                               @Part("postal_code") RequestBody postal_code,
                               @Part("address") RequestBody address,
                               @Part("iqma_national_id") RequestBody iqma_national_id,
                               @Part("vehicale_plate_no") RequestBody vehicale_plate_no,
                               @Part("vehicale_model") RequestBody vehicale_model,
                               @Part("iebn") RequestBody iebn,
                               @Part("password1") RequestBody password1,
                               @Part("password2") RequestBody password2);

//    @FormUrlEncoded
//    @POST("auth/driver-register/")
//    Call<JsonElement> register(@Field("email") String email,
//                              @Field("username") String username,
//                              @Field("terms_conditions") boolean terms_conditions,
//                              @Field("role") String role,
//                              @Field("contact_number") String contact_number,
//                              @Field("city") String city,
//                              @Field("postal_code") String postal_code,
//                              @Field("address") String address,
//                              @Field("iqma_national_id") int iqma_national_id,
//                              @Field("vehicale_plate_no") String vehicale_plate_no,
//                              @Field("vehicale_model") int vehicale_model,
//                              @Field("iebn") int iebn,
//                              @Field("password1") String password1,
//                              @Field("password2") String password2);

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
                                 @Field("old_password") String old_password,
                                 @Field("new_password") String new_password,
                                 @Field("confirm_password") String confirm_password);

    @FormUrlEncoded
    @PUT("api/driver-profile/{id}")
    Call<User> updateProfile(@Header("Authorization") String  token,
                             @Path(value = "id", encoded = true) int id,
                             @Field("email") String email,
                             @Field("username") String username,
                             @Field("city") String city,
                             @Field("postal_code") String postal_code,
                             @Field("address") String address,
                             @Field("iqma_national_id") int iqma_national_id,
                             @Field("vehicale_plate_no") String vehicale_plate_no,
                             @Field("vehicale_model") int vehicale_model,
                             @Field("iebn") int iebn);

    @FormUrlEncoded
    @PUT("api/driver-profile/{id}")
    Call<User> setActive(@Header("Authorization") String  token,
                             @Path(value = "id", encoded = true) int id,
                             @Field("is_active") boolean is_active);

    @Multipart
    @PUT("api/driver-profile/{id}")
    Call<User> updateProfileImage(@Header("Authorization") String  token,
                                  @Path(value = "id", encoded = true) int id,
                                  @Part MultipartBody.Part profile_or_logo);




}
