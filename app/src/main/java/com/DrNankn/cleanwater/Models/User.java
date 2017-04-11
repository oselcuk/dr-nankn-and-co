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

    /**
     * Creates a new user
     */
    public User() {
        role = Role.User;
        email = "";
    }

    /**
     * Creates a new user
     *
     * @param email     the user's email
     * @param role      the role of the user
     */
    public User(String email, Role role) {
        this(email, role, "N/A", email);
    }

    /**
     * Creates a new user
     *
     * @param email     the email of the user
     * @param role      the role of the user
     * @param address   the address of the user
     * @param name      the name of the user
     */
    private User(String email, Role role, String address, String name) {
        this.email = email;
        this.role = role;
        this.address = address;
        this.name = name;
    }

    /**
     * Creates a new user from information entered by the user
     *
     * @param in    information entered by the user
     */
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
