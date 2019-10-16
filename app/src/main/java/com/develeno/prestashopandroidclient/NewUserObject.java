package com.develeno.prestashopandroidclient;

public class NewUserObject {
    private String email;
    private String firstname;
    private int gender;
    private String lastname;
    private String password;

    public NewUserObject(String firstname2, String lastname2, String email2, String password2, int gender2) {
        this.firstname = firstname2;
        this.lastname = lastname2;
        this.email = email2;
        this.password = password2;
        this.gender = gender2;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname2) {
        this.firstname = firstname2;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname2) {
        this.lastname = lastname2;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email2) {
        this.email = email2;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password2) {
        this.password = password2;
    }

    public int getGender() {
        return this.gender;
    }

    public void setGender(int gender2) {
        this.gender = gender2;
    }
}
