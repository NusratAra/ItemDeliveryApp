package com.example.nishikanto.itemdeliverapp.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public class Company implements Parcelable {

    private int id;
    private String email;
    private String username;
    private boolean terms_conditions;
    private boolean is_logged_in;
    private String last_logged_in_at;
    private boolean is_staff;
    private boolean is_active;
    private String role;
    private boolean is_verified;
    private String contact_number;
    private String city;
    private String postal_code;
    private String address;
    private String company_reg_no;
    private String date_joined;
    private String updated_at;
    private String profile_or_logo = null;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public Company(Parcel parcel) {
        id = parcel.readInt();
        email = parcel.readString();
        username = parcel.readString();
        terms_conditions = parcel.readInt() == 1;
        is_logged_in = parcel.readInt() == 1;
        last_logged_in_at = parcel.readString();
        is_staff = parcel.readInt() == 1;
        is_active = parcel.readInt() == 1;
        role = parcel.readString();
        is_verified = parcel.readInt() == 1;
        contact_number = parcel.readString();
        city = parcel.readString();
        postal_code = parcel.readString();
        address = parcel.readString();
        company_reg_no = parcel.readString();
        date_joined = parcel.readString();
        updated_at = parcel.readString();
        profile_or_logo = parcel.readString();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isTerms_conditions() {
        return terms_conditions;
    }

    public void setTerms_conditions(boolean terms_conditions) {
        this.terms_conditions = terms_conditions;
    }

    public boolean isIs_logged_in() {
        return is_logged_in;
    }

    public void setIs_logged_in(boolean is_logged_in) {
        this.is_logged_in = is_logged_in;
    }

    public String getLast_logged_in_at() {
        return last_logged_in_at;
    }

    public void setLast_logged_in_at(String last_logged_in_at) {
        this.last_logged_in_at = last_logged_in_at;
    }

    public boolean isIs_staff() {
        return is_staff;
    }

    public void setIs_staff(boolean is_staff) {
        this.is_staff = is_staff;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isIs_verified() {
        return is_verified;
    }

    public void setIs_verified(boolean is_verified) {
        this.is_verified = is_verified;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany_reg_no() {
        return company_reg_no;
    }

    public void setCompany_reg_no(String company_reg_no) {
        this.company_reg_no = company_reg_no;
    }

    public String getDate_joined() {
        return date_joined;
    }

    public void setDate_joined(String date_joined) {
        this.date_joined = date_joined;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getProfile_or_logo() {
        return profile_or_logo;
    }

    public void setProfile_or_logo(String profile_or_logo) {
        this.profile_or_logo = profile_or_logo;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(email);
        dest.writeString(username);
        dest.writeInt(terms_conditions ? 1 : 0);
        dest.writeInt(is_logged_in ? 1 : 0);
        dest.writeString(last_logged_in_at);
        dest.writeInt(is_staff ? 1 : 0);
        dest.writeInt(is_active ? 1 : 0);
        dest.writeString(role);
        dest.writeInt(is_verified ? 1 : 0);
        dest.writeString(contact_number);
        dest.writeString(city);
        dest.writeString(postal_code);
        dest.writeString(address);
        dest.writeString(company_reg_no);
        dest.writeString(date_joined);
        dest.writeString(updated_at);
        dest.writeString(profile_or_logo);
    }

    public static final Parcelable.Creator<Company> CREATOR = new Parcelable.Creator<Company>(){

        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public Company createFromParcel(Parcel parcel) {
            return new Company(parcel);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[0];
        }
    };
}
