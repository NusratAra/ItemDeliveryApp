package com.example.nishikanto.itemdeliverapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Total_income_sum implements Parcelable {
    private float total_bill__sum;


    // Getter Methods

    protected Total_income_sum(Parcel in) {
        total_bill__sum = in.readFloat();
    }

    public static final Creator<Total_income_sum> CREATOR = new Creator<Total_income_sum>() {
        @Override
        public Total_income_sum createFromParcel(Parcel in) {
            return new Total_income_sum(in);
        }

        @Override
        public Total_income_sum[] newArray(int size) {
            return new Total_income_sum[size];
        }
    };

    public float getTotal_bill__sum() {
        return total_bill__sum;
    }

    // Setter Methods

    public void setTotal_bill__sum(float total_bill__sum) {
        this.total_bill__sum = total_bill__sum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(total_bill__sum);
    }
}
