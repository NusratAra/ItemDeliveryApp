package com.example.nishikanto.itemdeliverapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Issue implements Parcelable {
    private int id;
    private String type;
    private boolean is_active;
    private String created_at;
    private String updated_at;

    protected Issue(Parcel in) {
        id = in.readInt();
        type = in.readString();
        is_active = in.readByte() != 0;
        created_at = in.readString();
        updated_at = in.readString();
    }

    public static final Creator<Issue> CREATOR = new Creator<Issue>() {
        @Override
        public Issue createFromParcel(Parcel in) {
            return new Issue(in);
        }

        @Override
        public Issue[] newArray(int size) {
            return new Issue[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(type);
        dest.writeByte((byte) (is_active ? 1 : 0));
        dest.writeString(created_at);
        dest.writeString(updated_at);
    }
}
