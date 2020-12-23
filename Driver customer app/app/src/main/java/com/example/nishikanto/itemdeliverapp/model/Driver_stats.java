package com.example.nishikanto.itemdeliverapp.model;

import java.util.ArrayList;

public class Driver_stats {
    Total_trip_sum total_trip_sum;
    Total_income_sum total_income_sum;
    ArrayList<SinglePaymentHistory> date_wise = new ArrayList<>();


    // Getter Methods

    public Total_trip_sum getTotal_trip_sum() {
        return total_trip_sum;
    }

    public Total_income_sum getTotal_income_sum() {
        return total_income_sum;
    }

    // Setter Methods

    public void setTotal_trip_sum(Total_trip_sum total_trip_sumObject) {
        this.total_trip_sum = total_trip_sumObject;
    }

    public void setTotal_income_sum(Total_income_sum total_income_sumObject) {
        this.total_income_sum = total_income_sumObject;
    }

    public ArrayList<SinglePaymentHistory> getDate_wise() {
        return date_wise;
    }

    public void setDate_wise(ArrayList<SinglePaymentHistory> date_wise) {
        this.date_wise = date_wise;
    }
}
