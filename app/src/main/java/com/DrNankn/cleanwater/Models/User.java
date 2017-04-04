package com.DrNankn.cleanwater.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by oguzh_000 on 15/2/2017.
 */

public class User implements Parcelable{

    public String email;
    public String name;
    public String birthDate; //TODO: This needs to be a Date object, not String. Also add it to Parcelable methods
    public Role role;
    public String address;

    public User() {
        role = Role.User;
    }

    public User(String email) {
        this(email, Role.User, "N/A", email);
    }

    public User(String email, Role role) {
        this(email, role, "N/A", email);
    }

    public User(String email, Role role, String address, String name) {
        this.email = email;
        this.role = role;
        this.address = address;
        this.name = name;
    }

    public User(Parcel in) {
        this(in.readString(), Role.valueOf(in.readString()), in.readString(), in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(role.name());
        dest.writeString(address);
        dest.writeString(name);
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

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public Role getRole() {
        return role;
    }

    public String getAddress() {
        return address;
    }

    public enum Role {
        User,
        Worker,
        Manager,
        Administrator
    }
}
