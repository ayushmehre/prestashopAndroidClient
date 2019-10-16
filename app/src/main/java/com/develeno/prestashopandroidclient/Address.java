package com.develeno.prestashopandroidclient;

public class Address {

    /* renamed from: id */
    private int f20id;
    private String line1;
    private String line2;
    private String mobileNumber;
    private String phoneNumber;
    private String pinCode;
    private String title;

    public String getTitle() {
        if (this.title != null) {
            return this.title;
        }
        return "";
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getLine1() {
        if (this.line1 != null) {
            return this.line1;
        }
        return "";
    }

    public void setLine1(String line12) {
        this.line1 = line12;
    }

    public String getLine2() {
        if (this.line2 != null) {
            return this.line2;
        }
        return "";
    }

    public void setLine2(String line22) {
        this.line2 = line22;
    }

    public String getPinCode() {
        if (this.pinCode != null) {
            return this.pinCode;
        }
        return "";
    }

    public void setPinCode(String pinCode2) {
        this.pinCode = pinCode2;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber2) {
        this.phoneNumber = phoneNumber2;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public void setMobileNumber(String mobileNumber2) {
        this.mobileNumber = mobileNumber2;
    }

    public int getId() {
        return this.f20id;
    }

    public void setId(int id) {
        this.f20id = id;
    }
}
