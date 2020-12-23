package com.example.nishikanto.itemdeliverapp.services;

import com.example.nishikanto.itemdeliverapp.model.DriverLocation;
import com.example.nishikanto.itemdeliverapp.model.Issues;
import com.example.nishikanto.itemdeliverapp.model.NewIssue;
import com.example.nishikanto.itemdeliverapp.model.PaymentHistory;
import com.example.nishikanto.itemdeliverapp.model.SingleTrip;
import com.example.nishikanto.itemdeliverapp.model.Trip;
import com.example.nishikanto.itemdeliverapp.model.Trips;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TripAuthenticationService {

    @GET("api/driver-trips/{id}")
    Call<Trips> getAllTrips(@Header("Authorization") String  token,
                            @Path(value = "id", encoded = true) int id);

    @GET("api/trips/track-trip/{track_id}")
    Call<SingleTrip> trackTrip(
            @Path(value = "track_id", encoded = true) String track_id);

    @FormUrlEncoded
    @PUT("api/trips/update/{id}")
    Call<Trip> changeStatus(@Header("Authorization") String  token,
                            @Path(value = "id", encoded = true) int id,
                            @Field("status") int status);

    @FormUrlEncoded
    @PUT("api/trips/reschedule-trip/{id}")
    Call<SingleTrip> rescheduleTrack(@Path(value = "id", encoded = true) int id,
                                     @Field("date") String date,
                                     @Field("slot") String slot);

    @PUT("api/customer-accept-trip/{id}")
    Call<SingleTrip> acceptByCustomer(@Path(value = "id", encoded = true) int id);

    @GET("api/issues")
    Call<Issues> getTripIssues();

//    @GET("api/trip-issues/{id}")
//    Call<SingleTripIssue> getSingleTripIssues(@Path(value = "id", encoded = true) int id);

    @FormUrlEncoded
    @POST("api/create-trip-issues/")
    Call<NewIssue> addNewIssue(@Field("trip") int trip,
                               @Field("text") String text,
                               @Field("issue") int issue);

    @FormUrlEncoded
    @PUT("api/update-driver-location")
    Call<DriverLocation> updateLocation(@Header("Authorization") String  token,
                                        @Field("current_lat") String current_lat,
                                        @Field("current_long") String current_long);

    @FormUrlEncoded
    @PUT("api/trips/update/{id}")
    Call<SingleTrip> updateTrip(@Header("Authorization") String  token,
                                @Path(value = "id", encoded = true) int id,
                                @Field("status") int status,
                                @Field("driver_id") int driver_id,
                                @Field("code") int code);

    @GET("api/driver-payment-history")
    Call<PaymentHistory> getPaymentHistory(@Header("Authorization") String  token);

    @GET("api/trips/")
    Call<Trips> getTripHistory(@Header("Authorization") String  token);

}
