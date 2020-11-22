package com.example.nishikanto.itemdeliverapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Issues implements Parcelable {
    ArrayList<Issue> issues = new ArrayList<>();

    protected Issues(Parcel in) {
        issues = in.createTypedArrayList(Issue.CREATOR);
    }

    public static final Creator<Issues> CREATOR = new Creator<Issues>() {
        @Override
        public Issues createFromParcel(Parcel in) {
            return new Issues(in);
        }

        @Override
        public Issues[] newArray(int size) {
            return new Issues[size];
        }
    };

    public ArrayList<Issue> getIssues() {
        return issues;
    }

    public void setIssues(ArrayList<Issue> issues) {
        this.issues = issues;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(issues);
    }
}
