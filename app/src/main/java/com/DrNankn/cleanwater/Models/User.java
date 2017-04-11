/* 
* This class represents a user of the app which has 
* an email, a name, a birth date, a role, and a address
*/
package com.DrNankn.cleanwater.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{

    private final String email;
    private final Role role;
    private String name;
    private String address;

    public User() {
        role = Role.User;
        email = "";
    }

    public User(String email, Role role) {
        this(email, role, "N/A", email);
    }

    private User(String email, Role role, String address, String name) {
        this.email = email;
        this.role = role;
        this.address = address;
        this.name = name;
    }

    private User(Parcel in) {
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
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
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

    public Role getRole() {
        return role;
    }

    public CharSequence getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public enum Role {
        User,
        Worker,
        Manager,
        Administrator
    }
}
