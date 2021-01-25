package com.example.nishikanto.itemdeliverapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Trip implements Parcelable {

    private int id;
    private int company_id;
    private String trip_code;
    private String pickup_area;
    private String pickup_latitude;
    private String pickup_longitude;

    private String pickup_location;

    private String delivery_area;
    private String delivery_latitude;
    private String delivery_longitude;

    private String delivery_location;

    private int driver_id;
    private String current_location = null;
    private String current_latitude = null;
    private String current_lognitude = null;
    private String customer_name;
    private String customer_phone;
    private String product_title;
    private String product_image;
    private String product_detail;
    private String distance = null;
    private String status;

    private int otp;
    private String otp_expire_at;

    private String reschedule_date = null;
    private String reschedule_start_time = null;
    private String reschedule_end_time = null;
    private float bill;
    private String expire_date;
    private String created_at;
    private String updated_at;

    private boolean is_paid;

    private ArrayList<Company> company;
//    private String driver = null;


    public Trip(Parcel parcel) {
        id = parcel.readInt();
        company_id = parcel.readInt();
        trip_code = parcel.readString();
        pickup_area = parcel.readString();
        pickup_latitude = parcel.readString();
        pickup_longitude = parcel.readString();
        pickup_location = parcel.readString();
        delivery_area = parcel.readString();
        delivery_latitude = parcel.readString();
        delivery_longitude = parcel.readString();
        delivery_location = parcel.readString();
        driver_id = parcel.readInt();
        current_location = parcel.readString();
        current_latitude = parcel.readString();
        current_lognitude = parcel.readString();
        customer_name = parcel.readString();
        customer_phone = parcel.readString();
        product_title = parcel.readString();
        product_image = parcel.readString();
        product_detail = parcel.readString();
        distance = parcel.readString();
        status = parcel.readString();
        otp = parcel.readInt();
        otp_expire_at = parcel.readString();
        reschedule_date = parcel.readString();
        reschedule_start_time = parcel.readString();
        reschedule_end_time = parcel.readString();
        bill = parcel.readFloat();
        expire_date = parcel.readString();
        created_at = parcel.readString();
        updated_at = parcel.readString();
        is_paid = parcel.readInt() == 1;
        company = parcel.createTypedArrayList(Company.CREATOR);

//        driver = parcel.readString();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getTrip_code() {
        return trip_code;
    }

    public void setTrip_code(String trip_code) {
        this.trip_code = trip_code;
    }

    public String getPickup_area() {
        return pickup_area;
    }

    public void setPickup_area(String pickup_area) {
        this.pickup_area = pickup_area;
    }

    public String getPickup_latitude() {
        return pickup_latitude;
    }

    public void setPickup_latitude(String pickup_latitude) {
        this.pickup_latitude = pickup_latitude;
    }

    public String getPickup_longitude() {
        return pickup_longitude;
    }

    public void setPickup_longitude(String pickup_longitude) {
        this.pickup_longitude = pickup_longitude;
    }

    public String getDelivery_area() {
        return delivery_area;
    }

    public void setDelivery_area(String delivery_area) {
        this.delivery_area = delivery_area;
    }

    public String getDelivery_latitude() {
        return delivery_latitude;
    }

    public void setDelivery_latitude(String delivery_latitude) {
        this.delivery_latitude = delivery_latitude;
    }

    public String getDelivery_longitude() {
        return delivery_longitude;
    }

    public void setDelivery_longitude(String delivery_longitude) {
        this.delivery_longitude = delivery_longitude;
    }


    public int getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(int driver_id) {
        this.driver_id = driver_id;
    }

    public String getCurrent_location() {
        return current_location;
    }

    public void setCurrent_location(String current_location) {
        this.current_location = current_location;
    }

    public String getCurrent_latitude() {
        return current_latitude;
    }

    public void setCurrent_latitude(String current_latitude) {
        this.current_latitude = current_latitude;
    }

    public String getCurrent_lognitude() {
        return current_lognitude;
    }

    public void setCurrent_lognitude(String current_lognitude) {
        this.current_lognitude = current_lognitude;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_detail() {
        return product_detail;
    }

    public void setProduct_detail(String product_detail) {
        this.product_detail = product_detail;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReschedule_date() {
        return reschedule_date;
    }

    public void setReschedule_date(String reschedule_date) {
        this.reschedule_date = reschedule_date;
    }

    public String getReschedule_start_time() {
        return reschedule_start_time;
    }

    public void setReschedule_start_time(String reschedule_start_time) {
        this.reschedule_start_time = reschedule_start_time;
    }

    public String getReschedule_end_time() {
        return reschedule_end_time;
    }

    public void setReschedule_end_time(String reschedule_end_time) {
        this.reschedule_end_time = reschedule_end_time;
    }


    public float getBill() {
        return bill;
    }

    public void setBill(float bill) {
        this.bill = bill;
    }

    public String getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(String expire_date) {
        this.expire_date = expire_date;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getPickup_location() {
        return pickup_location;
    }

    public void setPickup_location(String pickup_location) {
        this.pickup_location = pickup_location;
    }

    public String getDelivery_location() {
        return delivery_location;
    }

    public void setDelivery_location(String delivery_location) {
        this.delivery_location = delivery_location;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    public String getOtp_expire_at() {
        return otp_expire_at;
    }

    public void setOtp_expire_at(String otp_expire_at) {
        this.otp_expire_at = otp_expire_at;
    }

    public boolean isIs_paid() {
        return is_paid;
    }

    public void setIs_paid(boolean is_paid) {
        this.is_paid = is_paid;
    }

    public ArrayList<Company> getCompanies() {
        return company;
    }

    public void setCompanies(ArrayList<Company> companies) {
        this.company = companies;
    }

    //    public String getDriver() {
//        return driver;
//    }
//
//    public void setDriver(String driver) {
//        this.driver = driver;
//    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(company_id);
        dest.writeString(trip_code);
        dest.writeString(pickup_area);
        dest.writeString(pickup_latitude);
        dest.writeString(pickup_longitude);
        dest.writeString(pickup_location);
        dest.writeString(delivery_area);
        dest.writeString(delivery_latitude);
        dest.writeString(delivery_longitude);
        dest.writeString(delivery_location);
        dest.writeInt(driver_id);
        dest.writeString(current_location);
        dest.writeString(current_latitude);
        dest.writeString(current_lognitude);
        dest.writeString(customer_name);
        dest.writeString(customer_phone);
        dest.writeString(product_title);
        dest.writeString(product_image);
        dest.writeString(product_detail);
        dest.writeString(distance);
        dest.writeString(status);
        dest.writeInt(otp);
        dest.writeString(otp_expire_at);
        dest.writeString(reschedule_date);
        dest.writeString(reschedule_start_time);
        dest.writeString(reschedule_end_time);
        dest.writeFloat(bill);
        dest.writeString(expire_date);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeInt(is_paid ? 1: 0);
        dest.writeTypedList(company);
//        dest.writeString(driver);
    }

    public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>(){

        @Override
        public Trip createFromParcel(Parcel parcel) {
            return new Trip(parcel);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[0];
        }
    };
}
