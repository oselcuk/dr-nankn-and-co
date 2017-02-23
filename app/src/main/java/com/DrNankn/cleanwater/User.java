package com.DrNankn.cleanwater;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by oguzh_000 on 15/2/2017.
 */

public class User implements Parcelable{

    public String email;
    public Role role;

    public User() {
        role = Role.User;
    }

    public User(String email) {
        this(email, Role.User);
    }

    public User(String email, Role role) {
        this.email = email;
        this.role = role;
    }

    public User(Parcel in) {
        this(in.readString(), Role.valueOf(in.readString()));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(role.name());
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public enum Role {
        User,
        Worker,
        Manager,
        Administrator
    }
}
