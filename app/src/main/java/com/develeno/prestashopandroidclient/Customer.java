package com.develeno.prestashopandroidclient;

public class Customer {
    public static final int FEMALE = 0;
    public static final int MALE = 1;
    private String email;
    private String firstName;
    private int gender;

    /* renamed from: id */
    private int f23id;
    private String lastName;
    private String password;
    private String url;

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName2) {
        this.firstName = firstName2;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName2) {
        this.lastName = lastName2;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password2) {
        this.password = password2;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url2) {
        this.url = url2;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email2) {
        this.email = email2;
    }

    public int getGender() {
        return this.gender;
    }

    public void setGender(int gender2) {
        this.gender = gender2;
    }

    public Boolean isMale() {
        boolean z = true;
        if (this.gender != 1) {
            z = false;
        }
        return Boolean.valueOf(z);
    }

    public int getId() {
        return this.f23id;
    }

    public void setId(int id) {
        this.f23id = id;
    }
}
