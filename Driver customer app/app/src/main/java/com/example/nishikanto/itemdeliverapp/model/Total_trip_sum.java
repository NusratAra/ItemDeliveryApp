package com.example.nishikanto.itemdeliverapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Total_trip_sum implements Parcelable {
    private float total_trip__sum;


    // Getter Methods

    protected Total_trip_sum(Parcel in) {
        total_trip__sum = in.readFloat();
    }

    public static final Creator<Total_trip_sum> CREATOR = new Creator<Total_trip_sum>() {
        @Override
        public Total_trip_sum createFromParcel(Parcel in) {
            return new Total_trip_sum(in);
        }

        @Override
        public Total_trip_sum[] newArray(int size) {
            return new Total_trip_sum[size];
        }
    };

    public float getTotal_trip__sum() {
        return total_trip__sum;
    }

    // Setter Methods

    public void setTotal_trip__sum(float total_trip__sum) {
        this.total_trip__sum = total_trip__sum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(total_trip__sum);
    }
}
