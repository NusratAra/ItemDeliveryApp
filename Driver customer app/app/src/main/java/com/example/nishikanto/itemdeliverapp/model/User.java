package com.example.nishikanto.itemdeliverapp.model;

public class User {
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
    private String iqma_national_id;
    private String vehicale_plate_no;
    private String vehicale_model;
    private String iebn;
    private String date_joined;
    private String updated_at;




    // Getter Methods

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public boolean getTerms_conditions() {
        return terms_conditions;
    }

    public boolean getIs_logged_in() {
        return is_logged_in;
    }

    public String getLast_logged_in_at() {
        return last_logged_in_at;
    }

    public boolean getIs_staff() {
        return is_staff;
    }

    public boolean getIs_active() {
        return is_active;
    }

    public String getRole() {
        return role;
    }

    public boolean getIs_verified() {
        return is_verified;
    }

    public String getContact_number() {
        return contact_number;
    }

    public String getCity() {
        return city;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public String getAddress() {
        return address;
    }

    public String getIqma_national_id() {
        return iqma_national_id;
    }

    public String getVehicale_plate_no() {
        return vehicale_plate_no;
    }

    public String getVehicale_model() {
        return vehicale_model;
    }

    public String getIebn() {
        return iebn;
    }

    public String getDate_joined() {
        return date_joined;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    // Setter Methods

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTerms_conditions(boolean terms_conditions) {
        this.terms_conditions = terms_conditions;
    }

    public void setIs_logged_in(boolean is_logged_in) {
        this.is_logged_in = is_logged_in;
    }

    public void setLast_logged_in_at(String last_logged_in_at) {
        this.last_logged_in_at = last_logged_in_at;
    }

    public void setIs_staff(boolean is_staff) {
        this.is_staff = is_staff;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setIs_verified(boolean is_verified) {
        this.is_verified = is_verified;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setIqma_national_id(String iqma_national_id) {
        this.iqma_national_id = iqma_national_id;
    }

    public void setVehicale_plate_no(String vehicale_plate_no) {
        this.vehicale_plate_no = vehicale_plate_no;
    }

    public void setVehicale_model(String vehicale_model) {
        this.vehicale_model = vehicale_model;
    }

    public void setIebn(String iebn) {
        this.iebn = iebn;
    }

    public void setDate_joined(String date_joined) {
        this.date_joined = date_joined;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
