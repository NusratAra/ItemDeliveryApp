package com.example.nishikanto.itemdeliverapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PaymentHistory implements Parcelable {
    Driver_stats driver_stats;



    // Getter Methods

    protected PaymentHistory(Parcel in) {
    }

    public static final Creator<PaymentHistory> CREATOR = new Creator<PaymentHistory>() {
        @Override
        public PaymentHistory createFromParcel(Parcel in) {
            return new PaymentHistory(in);
        }

        @Override
        public PaymentHistory[] newArray(int size) {
            return new PaymentHistory[size];
        }
    };

    public Driver_stats getDriver_stats() {
        return driver_stats;
    }

    // Setter Methods

    public void setDriver_stats(Driver_stats driver_statsObject) {
        this.driver_stats = driver_statsObject;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}


