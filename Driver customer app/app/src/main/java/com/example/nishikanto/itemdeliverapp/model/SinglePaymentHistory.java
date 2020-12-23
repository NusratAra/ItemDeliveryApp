package com.example.nishikanto.itemdeliverapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class SinglePaymentHistory implements Parcelable {
    private String created_at__date;
    private float total_trip;
    private float total_bill;
    ArrayList<Trip> trips = new ArrayList <Trip> ();


    // Getter Methods

    protected SinglePaymentHistory(Parcel in) {
        created_at__date = in.readString();
        total_trip = in.readFloat();
        total_bill = in.readFloat();
        trips = in.createTypedArrayList(Trip.CREATOR);
    }

    public static final Creator<SinglePaymentHistory> CREATOR = new Creator<SinglePaymentHistory>() {
        @Override
        public SinglePaymentHistory createFromParcel(Parcel in) {
            return new SinglePaymentHistory(in);
        }

        @Override
        public SinglePaymentHistory[] newArray(int size) {
            return new SinglePaymentHistory[size];
        }
    };

    public String getCreated_at__date() {
        return created_at__date;
    }

    public float getTotal_trip() {
        return total_trip;
    }

    public float getTotal_bill() {
        return total_bill;
    }

    // Setter Methods

    public void setCreated_at__date(String created_at__date) {
        this.created_at__date = created_at__date;
    }

    public void setTotal_trip(float total_trip) {
        this.total_trip = total_trip;
    }

    public void setTotal_bill(float total_bill) {
        this.total_bill = total_bill;
    }

    public ArrayList<Trip> getTrips() {
        return trips;
    }

    public void setTrips(ArrayList<Trip> trips) {
        this.trips = trips;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(created_at__date);
        dest.writeFloat(total_trip);
        dest.writeFloat(total_bill);
        dest.writeTypedList(trips);
    }
}
