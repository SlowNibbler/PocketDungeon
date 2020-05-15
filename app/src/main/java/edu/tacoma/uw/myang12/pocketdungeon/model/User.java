package edu.tacoma.uw.myang12.pocketdungeon.model;

import java.io.Serializable;

/** A class for User object, a user has email and password. */
public class User implements Serializable {
    private String mEmail;
    private String mPassword;

    // fields for query database
    public static final String ID = "memberid";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

    public User(String email, String password) {
        this.mEmail = email;
        this.mPassword = password;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}
